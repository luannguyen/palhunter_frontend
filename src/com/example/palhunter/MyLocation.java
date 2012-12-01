package com.example.palhunter;

import java.util.HashMap;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MyLocation extends MapActivity implements OnClickListener {
	MapView mapView;
	List<Overlay> mapOverlays;
	MapController mapController;
	HashMap<Integer, User>userList;
	MyMapLocationManager mapLocationManager;
	
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-palhunter");
    String httpPostURL = "action=insertLocation&id=%d&lat_int=%d&long_int=%d&updated_time=%d";
    String httpGetMyLocations = "id=%d&action=queryPastLocations";
    String httpGetMyFriends = "id=%d&action=findAllFriends";
    FriendListAdapter friendListAdapter;
    User myUser;

    MyFriendsHandler friendsHander;
    MyLocationHandler locationHandler;
    Drawable drawable;
    
    LocationManager locationManager;
    LocationListener locationListener;
    
    static final int ON_CREATE = 1;
    static final int ON_RESUME = 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        
        drawable = this.getResources().getDrawable(R.drawable.androidmarker);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        mapController = mapView.getController();
        mapOverlays = mapView.getOverlays();
        
        mapLocationManager = new MyMapLocationManager(mapOverlays, mapView);
        
    	locationHandler = new MyLocationHandler();
    	friendsHander = new MyFriendsHandler();
    	userList = new HashMap<Integer, User>();
    	
    	myUser = new User(drawable, this, locationHandler, mapView);
    	myUser.mainUser = true;
    	
    	Intent intent = getIntent();
    	Bundle b = intent.getExtras();
    	myUser.userId = b.getInt("id");
    	myUser.firstName = b.getString("firstName");
    	myUser.lastName = b.getString("lastName");
    	
        userList.put(myUser.userId, myUser);
    	TextView textView = (TextView)findViewById(R.id.nameFeild);
    	textView.setText(myUser.firstName + " " + myUser.lastName);
    	
        loadMyPastLocations(ON_CREATE);
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new myLocationListener();
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60*1000,10, locationListener); 
        
        loadMyFriendList(ON_CREATE);
        
    	textView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mapLocationManager.zoomInToUser(myUser);
			}
		});

    }

    public void stopUpdate(View view)
    {
    	locationManager.removeUpdates(locationListener);
    }
    
    public void startUpdate(View view)
    {
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60*1000,10, locationListener); 
    }
    

    @Override
    public void onPause()
    {
    	super.onPause();
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	//test whether android keep variables 
    	if(myUser != null) {
    		System.out.println("on resume my user " + myUser.firstName + " still in memory");
    		
    	}
        loadMyPastLocations(ON_RESUME);        
        loadMyFriendList(ON_RESUME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_location, menu);
        menu.add(R.string.log_out);
        menu.add(R.string.friend_list);
        menu.add(R.string.my_past_location);
        
        return super.onCreateOptionsMenu(menu);
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
            case R.string.log_out:
            	System.out.println("log out clicked");
            	SharedPreferences settings = getSharedPreferences(MainActivity.myPrefence, 0);
            	SharedPreferences.Editor e = settings.edit();
            	e.putBoolean("logged", false);
            	e.commit();
            	Intent intent = new Intent(this, MainActivity.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            	break;
            	
            case R.string.my_past_location:
            	//switch to my past location activity
            	break;
            case R.string.friend_list:
            	//switch to add/delete friend activity
            	break;
        }
        return super.onOptionsItemSelected(item);

     }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private class myLocationListener implements LocationListener {
    	int latitudeValue, longitudeValue;
    	long pubTime;
		public void onLocationChanged(Location location) {

			System.out.println("on location change");
			latitudeValue = (int)(location.getLatitude()* 1000000);
			longitudeValue = (int)(location.getLongitude()*1000000);
    		pubTime = System.currentTimeMillis();

			myUser.addLocation(latitudeValue,longitudeValue, pubTime);
			
			//insert new location to database
    		final String url = String.format(httpPostURL, myUser.userId, latitudeValue, longitudeValue,pubTime);
    		DatabaseClient.get(url, null, null);

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
      
    public void loadMyFriendList(int status)
    {
    	if(status == ON_CREATE) {
	    	final String getMyFriendsURL = String.format(httpGetMyFriends, myUser.userId);
	    	DatabaseClient.get(getMyFriendsURL, null, friendsHander);
    	} else if(status == ON_RESUME) {
    		
    	}
    }
    
    private final class MyFriendsHandler extends JsonHttpResponseHandler {
	    public void onSuccess(JSONObject locationObject) {
	    	System.out.println("get past location on success jsonobject");
	    }
	    
		public void onSuccess(JSONArray friendListArray) {
			int i = 0;
			try {
				for(i=0; i<friendListArray.length(); i++) {
				
					User friend = new User(drawable, MyLocation.this, locationHandler, mapView);
					JSONObject friendObj = friendListArray.getJSONObject(i);
					friend.getUser(friendObj);
					friend.queryPastLocationFromServer();
					myUser.addFriend(friend);
					System.out.println("friend " + i + " id: " + friend.userId);
					userList.put(friend.userId, friend);
				}
			} catch (Exception e) {
				System.out.println("jsonarray failed to get friend list");
			}
			
			final ListView friendList = (ListView)findViewById(R.id.listView1);
			// First paramenter - Context
			// Second parameter - Layout for the row
			// Third parameter - ID of the TextView to which the data is written
			// Forth - the Array of data
			//User[] myfriendsContents = new User[myUser.friendList.size()];
			//myUser.friendList.toArray(myfriendsContents);
			friendListAdapter = new FriendListAdapter(MyLocation.this,
					android.R.layout.simple_list_item_1, myUser.friendList);
			friendListAdapter.mapLocationManager = mapLocationManager;
			// Assign adapter to ListView
			friendList.setAdapter(friendListAdapter); 
		}
	
    }
    
    protected void loadMyPastLocations(int status) {
    	if(status == ON_CREATE) {
	    	myUser.queryPastLocationFromServer();
	    	
    	} else if(status == ON_RESUME) {
    		
    	}
    }
	  public final class MyLocationHandler extends JsonHttpResponseHandler {

		    public void onFailure(Throwable e,
	                JSONObject errorResponse) {
		    	System.out.println("get past location on failure jsonobject");
		    	System.out.print(errorResponse.keys().toString());
		    }
		    public void onFailure(Throwable e, JSONArray errorResponse) {
		    	System.out.println("get past location on failure jsonarray");
		    	try {
		    	for(int i=0; i<errorResponse.length(); i++) {
		    		System.out.print(errorResponse.getJSONObject(i).keys().toString());
		    	}
		    	}catch (JSONException ee) {
					System.out.println("jsonarray failed to get location points");
				}
		    }
		    
		    public void onSuccess(JSONObject locationObject) {
		    	System.out.println("get past location on success jsonobject");
		    }
		    
			public void onSuccess(JSONArray locationArray) {
				int i = 0;
				int latitudeValue,longitudeValue, pid;
				long time; 
				System.out.println("get my past location handler on Success, there are "+ 
				locationArray.length() + " past locations");
				User user = myUser;
				try {
					for(i=0; i<locationArray.length(); i++) {
						
						JSONObject location = locationArray.getJSONObject(i);
						pid = location.getInt("PID");
						latitudeValue = location.getInt("LAT_INT");
						longitudeValue = location.getInt("LONG_INT");
						time = location.getLong("UPDATED_TIME");
						user = userList.get(pid);	
						System.out.println(user.getFullName() + " add a new geoPoint lat :" + latitudeValue
								+ " long: " + longitudeValue);
						user.addLocation(latitudeValue, longitudeValue, time);	
					}	

				} catch (JSONException e) {
					System.out.println("jsonarray failed to get location points");
				}
				mapLocationManager.showUserCurrentLocation(user);
				mapLocationManager.showUserPath(user);
			}
		}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case(R.id.friendNameView) :
			
		}
		
	}
	
	public void ManageFriends(View v) {
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putParcelable(User.USER_TYPE, myUser);
		i.putExtras(b);
		i.setClass(this, FriendManagerActivity.class);
	//	startActivity(i);
		startActivityForResult(i, 0);
	}
	
	public void QueryLocations(View v) {
		Intent i = new Intent();
		Bundle b = new Bundle();
		b.putParcelable(User.USER_TYPE, myUser);
		i.putExtras(b);
		i.setClass(this, MapQueryActivity.class);
		startActivityForResult(i, 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
		switch (resultCode) { 
		case RESULT_OK:
			if(dataIntent.hasExtra(User.USER_TYPE)) {
				myUser = (User)dataIntent.getExtras().get(User.USER_TYPE);
			}
			friendListAdapter.clear();
			mapOverlays.clear();
			
			//add my locations to map
			myUser.addAllLocations(drawable, this, mapView);
			mapLocationManager.showUserCurrentLocation(myUser);
			mapLocationManager.showUserPath(myUser);
			
			for(int i=0; i<myUser.friendList.size(); i++) {
				User friend = myUser.friendList.get(i);
				friendListAdapter.add(friend);
				
				//get locations for new friends
				if(!friend.locationInMemory) {
					friend.locationHandler = locationHandler;
					friend.queryPastLocationFromServer();
				} else {
					//add old friends locations to map
					friend.addAllLocations(drawable, this, mapView);
					mapLocationManager.showUserCurrentLocation(friend);
					mapLocationManager.showUserPath(friend);
				}
			}
            break;
		default:
	        break;
		}
	}
	
	public void LogOut(View view) {
    	System.out.println("log out clicked");
    	SharedPreferences settings = getSharedPreferences(MainActivity.myPrefence, 0);
    	SharedPreferences.Editor e = settings.edit();
    	e.putBoolean("logged", false);
    	e.commit();
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);	
	}
}
