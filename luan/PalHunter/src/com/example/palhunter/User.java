package com.example.palhunter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class User implements Parcelable{

	Integer userId;
	String firstName, lastName;
	long createdTime;
	ArrayList<User> friendList;
	ArrayList<UserLocation> myPastLocations;
	LocationItemizedOverlay myLocationOveraly;
	PathOverlay myPathOverlay;
	LocationItemizedOverlay myCurrentLocationOverlay;
	HashMap<Overlay, Integer> myOverlayIndexMap;
	
	Context myContext;
	Drawable myDrawable;
	MapView myMapView;
	
	boolean locationInMemory;
	boolean friendsInMemory;
	boolean mainUser;
	
	MyLocation.MyLocationHandler locationHandler;
	static final String USER_TYPE = "my user";
	
	static final int MAX_HISTORY_LOCATION_NUM = 100;
	public User(Drawable drawable, Context context, MyLocation.MyLocationHandler handler, MapView mapView) {
		// TODO Auto-generated constructor stub
		myPastLocations = new ArrayList<UserLocation>();
		friendList = new ArrayList<User>();		
		Drawable pathLocationDrawable = context.getResources().getDrawable(R.drawable.google_maps_marker);
		myLocationOveraly = new LocationItemizedOverlay(pathLocationDrawable, context);
		
		myCurrentLocationOverlay = new LocationItemizedOverlay(drawable, context);
		myOverlayIndexMap = new HashMap<Overlay, Integer>();
		myContext = context;
		myDrawable = drawable;
		myMapView = mapView;
		
		locationInMemory = false;
		friendsInMemory = false;
		locationHandler = handler; 
		mainUser = false;
	}
	
	public User() {
		myPastLocations = new ArrayList<UserLocation>();
		friendList = new ArrayList<User>();	
		myOverlayIndexMap = new HashMap<Overlay, Integer>();
		locationInMemory = false;
		mainUser = false;
	}
	
    private User(Parcel in) {
    	this();
    	userId = in.readInt();
    	firstName = in.readString();
    	lastName = in.readString();
    	createdTime = in.readLong();
    	locationInMemory  = (in.readByte() == 1);
    	mainUser = (in.readByte() == 1);
    	
    	friendList = new ArrayList<User>();
    	in.readTypedList(friendList,User.CREATOR);
    	myPastLocations = new ArrayList<UserLocation>();
    	in.readTypedList(myPastLocations, UserLocation.CREATOR);
    	
    	myOverlayIndexMap = new HashMap<Overlay, Integer>();
    }
	
	public void getUser(JSONObject jo) throws Exception {
		try {
			userId = Integer.parseInt(jo.getString("PID").toString());
			firstName = jo.getString("FIRST_NAME").toString();
			lastName = jo.getString("LAST_NAME").toString();
			createdTime = Long.parseLong(jo.getString("CREATED_TIME").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//overwrite
	public String toString()
	{
		return firstName + " " + lastName;
	}
	
	public String getFullName()
	{
		return firstName + " " + lastName;
	}
	public boolean addFriend(User user1)
	{
		if(friendList.contains(user1)) {
			System.out.println("already have this friend in my friendlist");
			return false;
		}
		else {
			friendList.add(user1);
			return true;
		}
	}
	
	public boolean removeFriend(User user1) {
		if(friendList.contains(user1)) {
			friendList.remove(user1);
			return true;
		} else {
			System.out.println("removing non-exsiting friend");
			if(user1 == null ) {
				System.out.println("user1 == null");
			} else {
				System.out.println("user name: " + user1.firstName);
			}
			return false;
		}
	}
	
	public boolean removeFriend(Integer removeId) {
		for(int i=0; i<friendList.size(); i++) {
			if(friendList.get(i).userId == removeId) {
				friendList.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public void addLocation(int latitudeValue, int longitudeValue, long time)
	{
		UserLocation currentLocation = new UserLocation(latitudeValue,longitudeValue, time);
		myPastLocations.add(currentLocation);
		GeoPoint myPoint = currentLocation.getLocationPoint();
		OverlayItem overlayitem = new OverlayItem(myPoint, getFullName(), currentLocation.getTime()); 
		
		if(myPathOverlay == null) {
			myPathOverlay = new PathOverlay(myPastLocations, myMapView, myContext, mainUser);
		} 
		
		if(!myCurrentLocationOverlay.isEmpty()) {
			myLocationOveraly.addOverlay(myCurrentLocationOverlay.getItem(0));
		}
		
		myCurrentLocationOverlay.clear();
		myCurrentLocationOverlay.addOverlay(overlayitem);
	}
	
	public void addAllLocations(Drawable drawable, Context context, MapView mapView)
	{
		myDrawable = drawable;
		myContext = context;
		myMapView = mapView;

		Drawable pathLocationDrawable = context.getResources().getDrawable(R.drawable.google_maps_marker);
		myLocationOveraly = new LocationItemizedOverlay(pathLocationDrawable, context);

		myCurrentLocationOverlay = new LocationItemizedOverlay(drawable, context);
		GeoPoint myPoint = null;
		OverlayItem overlayitem = null;
		if(myPastLocations.size() > 0) {
			myPathOverlay =  new PathOverlay(myPastLocations, myMapView, myContext, mainUser);
		}
		
		for(int i=0; i<myPastLocations.size(); i++) {
			myPoint = myPastLocations.get(i).getLocationPoint();
			overlayitem = new OverlayItem(myPoint, getFullName(), myPastLocations.get(i).getTime());
			myLocationOveraly.addOverlay(overlayitem);
			if(i==0) {
				myPathOverlay =  new PathOverlay(myPastLocations, myMapView, myContext, mainUser);
			} 
		}
		if(overlayitem != null){
			myCurrentLocationOverlay.addOverlay(overlayitem);
		}
		
		for(int i=0; i<friendList.size(); i++) {
			friendList.get(i).addAllLocations(drawable, context, mapView);
		}
	}
	
	
	public PathOverlay getPathOverlay() {
		return myPathOverlay;
	}
	
	public LocationItemizedOverlay getLocationOverlay() {
		return myLocationOveraly;
	}
	
	public LocationItemizedOverlay getCurrentLocation() {
		return myCurrentLocationOverlay;
	}

	public void setCurrentLocationOverlayIndex(Integer index) {
		myOverlayIndexMap.put(myCurrentLocationOverlay, index);
	}
	
	public void setPathOverlayIndex(Integer index) {
		myOverlayIndexMap.put(myPathOverlay, index);
		myOverlayIndexMap.put(myLocationOveraly, index + 1);
	}
	
	public void removeCurrentLocationOverlayIndex() {
		myOverlayIndexMap.remove(myCurrentLocationOverlay);
	}
	
	public void removePathOverlayIndex() {
		myOverlayIndexMap.remove(myPathOverlay);
		myOverlayIndexMap.remove(myLocationOveraly);
	}
	
	public int getCurrentLocationOverlayIndex() {
		return myOverlayIndexMap.get(myCurrentLocationOverlay);
	}
	
	public int getPathOverlayIndex() {
		return myOverlayIndexMap.get(myPathOverlay);
	}
	/*
	public void getLocations(InputStream responseStream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				responseStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		int i;
		int longtitude, latitude;
		long time;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONArray joa = new JSONArray(sb.toString());
			for (i = 0; i < joa.length(); i++) {
				longtitude = joa.getJSONObject(i).getInt("LONG_INT");
				latitude = joa.getJSONObject(i).getInt("LAT_INT");
				time = joa.getJSONObject(i).getLong("UPDATED_TIME");
				myPastLocations.add(new UserLocation(longtitude, latitude, time));
			}
		} catch (Exception e) {
			System.out.println("failed to get user past locations");
			throw e;
		}
	}
*/
	public static void getTotalUserNumber(final JsonHttpResponseHandler myHandler) throws Exception {

		DatabaseClient.get("action=getTotalPeople", null, myHandler);

	}
	
	public void queryPastLocationFromServer()
	{
	    String httpGetMyLocations = "id=%d&action=queryPastLocations";
    	final String getMyLocationsURL = String.format(httpGetMyLocations, userId);
    	DatabaseClient.get(getMyLocationsURL, null, locationHandler);
    	locationInMemory = true;
	}

    public int describeContents() {
        return 0;
    }
    
    public GeoPoint getCurrentLocationPoint() {
    	if( myPastLocations.size() > 0 )
    		return (myPastLocations.get(myPastLocations.size()-1).getLocationPoint());
    	
    	System.out.println("current location == null");
    	return new GeoPoint(0,0);
    }
    
    private double distance(User user) {
    	double x = this.getCurrentLocationPoint().getLatitudeE6() / 1e6;
    	double y = this.getCurrentLocationPoint().getLongitudeE6() / 1e6;
    	
    	double u_x = user.getCurrentLocationPoint().getLatitudeE6()/ 1e6;
    	double u_y = user.getCurrentLocationPoint().getLongitudeE6()/ 1e6;
    	
    	System.out.println("user: " + user.getFullName() + " x:y = " + u_x + " : " + u_y);
    	
    	float results[] = new float[3]; 
    	Location.distanceBetween(x, y, u_x, u_y, results);
    	
    	System.out.println("distance: " + user.getFullName() + " "+ results[0]);
    	return results[0];
    }
    
    public ArrayList<User> findFriendsWithinDistance(double distanceValue) {
    	ArrayList<User> closeFriend = new ArrayList<User>();
    	for(int i=0; i<friendList.size(); i++) {
    		if(distance(friendList.get(i)) < distanceValue * 1000) {
    			closeFriend.add(friendList.get(i));
    		}
    	}
    	return closeFriend;
    }
    
    /*
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     * 	Integer userId;
	String firstName, lastName;
	long createdTime;
	ArrayList<User> friendList;
	ArrayList<UserLocation> myPastLocations;
	LocationItemizedOverlay myLocationOveraly;
	Context myContext;
	Drawable myDrawable;
	boolean locationInMemory;
	boolean friendsInMemory;
	MyLocation.MyLocationHandler locationHandler;
     */
    public void writeToParcel(Parcel out, int flags) {
    	out.writeInt(userId);
    	out.writeString(firstName);
    	out.writeString(lastName);
    	out.writeLong(createdTime);
    	out.writeByte((byte) (locationInMemory ? 1 : 0));
    	out.writeByte((byte) (mainUser ? 1 : 0));
    	out.writeTypedList(friendList);
    	out.writeTypedList(myPastLocations); 
    }
    


    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };    

}
