package com.example.palhunter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.loopj.android.http.JsonHttpResponseHandler;

public class User {

	Integer userId;
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
	
	static final int MAX_HISTORY_LOCATION_NUM = 100;
	public User(Drawable drawable, Context context,MyLocation.MyLocationHandler handler) {
		// TODO Auto-generated constructor stub
		myPastLocations = new ArrayList<UserLocation>();
		friendList = new ArrayList<User>();		
		myLocationOveraly = new LocationItemizedOverlay(drawable, context);
		myContext = context;
		myDrawable = drawable;
		locationInMemory = false;
		locationHandler = handler; 
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
	
	public void addLocation(int latitudeValue, int longitudeValue, long time)
	{
		UserLocation currentLocation = new UserLocation(latitudeValue,longitudeValue, time);
		myPastLocations.add(currentLocation);
		GeoPoint myPoint = new GeoPoint(latitudeValue, longitudeValue);
		OverlayItem overlayitem = new OverlayItem(myPoint, firstName, lastName); 
		myLocationOveraly.addOverlay(overlayitem);
	}
	
	public LocationItemizedOverlay getLocationItemizedOverlay() {
		return myLocationOveraly;
	}
	
	public LocationItemizedOverlay getCurrentLocation() {
		System.out.println("get current location");
		LocationItemizedOverlay curLocationOverlay = new LocationItemizedOverlay(myDrawable, myContext);
		if(myPastLocations.size() > 0) {
			System.out.println("my last location");
			GeoPoint myLastLocation = myPastLocations.get(myPastLocations.size()-1).getLocationPoint();		
			curLocationOverlay.addOverlay(new OverlayItem(myLastLocation, firstName, lastName));
		}
		return curLocationOverlay;
	}
	
	/*
	 * public void showPastLocations(LocationItemizedOverlay itemizedoverlay) {
	 * int i; for(i=0; i<myPastLocations.) }
	 */
	/*
	 * public ArrayList<User> getUserFriends() {
	 * 
	 * }
	 */
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

	public static void getTotalUserNumber(final JsonHttpResponseHandler myHandler) throws Exception {

		DatabaseClient.get("action=getTotalPeople", null, myHandler);

	}
	
	public void queryPastLocationFromServer()
	{
	    String httpGetMyLocations = "id=%d&action=queryPastLocations";
    	final String getMyLocationsURL = String.format(httpGetMyLocations, userId);
    	DatabaseClient.get(getMyLocationsURL, null, locationHandler);
	}


}
