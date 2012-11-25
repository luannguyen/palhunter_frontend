package com.example.palhunter;

import com.google.android.maps.GeoPoint;

public class UserLocation {

	GeoPoint locationPoint;
	long timeStamp;
	public UserLocation() {
		// TODO Auto-generated constructor stub
		timeStamp = 0;
	}
	public UserLocation(int la, int lo, long time) {
		locationPoint = new GeoPoint(la, lo);
		timeStamp = time;
	}
	
	public GeoPoint getLocationPoint()
	{
		return locationPoint;
	}

}
