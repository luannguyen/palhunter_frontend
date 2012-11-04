package com.example.palhunter;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MyLocation extends MapActivity {
	MapView mapView;
	LocationItemizedOverlay itemizedoverlay;
	List<Overlay> mapOverlays;
	MapController mapController;
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-palhunter");
    String httpPostURL = "action=insertLocation&id=%d&lat_int=%d&long_int=%d&updated_time=%d";
    String httpGetMyLocations = "id=%d&action=queryPastLocations";
    User myUser;
    Integer userId;
    MyLocationHandler handler;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        
    	myUser = new User();
    	Intent intent = getIntent();
    	Bundle b = intent.getExtras();
    	myUser.userId = b.getInt("id");
    	myUser.firstName = b.getString("firstName");
    	myUser.lastName = b.getString("lastName");
    	userId = myUser.userId;
    	handler = new MyLocationHandler();
        
 //       getActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        mapController = mapView.getController();
        mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedoverlay = new LocationItemizedOverlay(drawable, this);
        
        loadMyPastLocations();
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60*1000,10,ll); 
    }
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_location, menu);
        return true;
    }

    
    @Override
 
    public boolean onOptionsItemSelected(MenuItem item) {
/*
    	switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
*/        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    protected void loadMyPastLocations() {
    	final String getMyLocationsURL = String.format(httpGetMyLocations, myUser.userId);
    	DatabaseClient.get(getMyLocationsURL, null, handler);
    }
    
    private class myLocationListener implements LocationListener {
    	int latitudeValue, longitudeValue;
    	
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//System.out.println("latitude: " + location.getLatitude());
			//System.out.println("longtitude: " + location.getLongitude());

			latitudeValue = (int)(location.getLatitude()* 1000000);
			longitudeValue = (int)(location.getLongitude()*1000000);
			
			GeoPoint myPoint = new GeoPoint(latitudeValue,longitudeValue);
			OverlayItem overlayitem = new OverlayItem(myPoint, "hello!", "my location");
			
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
			
			mapController.animateTo(myPoint);
			mapController.setZoom(17);
//			myTimestamp = new Timestamp(myDate.getTime());
    		long pubTime = System.currentTimeMillis();

			UserLocation currentLocation = new UserLocation(latitudeValue,longitudeValue,pubTime);
			myUser.addUserLoctaion(currentLocation);
			
    		final String url = String.format(httpPostURL, userId, latitudeValue, longitudeValue,pubTime);

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
    
    private final class MyLocationHandler extends JsonHttpResponseHandler {

	    public MyLocationHandler()
	    {
	        super();
	    }
	    public void onFailure(Throwable e,
                JSONObject errorResponse) {
	    	System.out.println("get past location on failure jsonobject");
	    	System.out.print(errorResponse.keys().toString());
	    }
	    public void onFailure(Throwable e,
                JSONArray errorResponse) {
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
			int latitudeValue,longitudeValue;
			long time; 
			System.out.println("get my past location handler on Success");
			try {
				for(i=0; i<locationArray.length(); i++) {
					
					JSONObject location = locationArray.getJSONObject(i);
					
					latitudeValue = location.getInt("LAT_INT");
					longitudeValue = location.getInt("LONG_INT");
					time = location.getLong("UPDATED_TIME");
					
					System.out.println("add a new geoPoint lat :" + latitudeValue
							+ " long: " + longitudeValue);
					UserLocation currentLocation = new UserLocation(latitudeValue,longitudeValue,time);
					myUser.myPastLocations.add(currentLocation);

				//	GeoPoint myPoint = new GeoPoint(latitudeValue,longitudeValue);
					OverlayItem overlayitem = new OverlayItem((currentLocation.getLocationPoint()), "point" + i, "");			
					itemizedoverlay.addOverlay(overlayitem);
				}
				mapOverlays.add(itemizedoverlay);
				mapView.invalidate();
			} catch (JSONException e) {
				System.out.println("jsonarray failed to get location points");
			}
		}
	}
}
