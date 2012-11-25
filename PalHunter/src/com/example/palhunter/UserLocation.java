package com.example.palhunter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.maps.GeoPoint;

public class UserLocation implements Parcelable{

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
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(locationPoint.getLatitudeE6());
		dest.writeInt(locationPoint.getLongitudeE6());
		dest.writeLong(timeStamp);
	}
	
	private UserLocation(Parcel in) {
		this();
		int latitute = in.readInt();
		int longtitude = in.readInt();
		timeStamp = in.readLong();
		
		locationPoint = new GeoPoint(latitute, longtitude);
	}
	
    public static final Parcelable.Creator<UserLocation> CREATOR
    = new Parcelable.Creator<UserLocation>() {
		public UserLocation createFromParcel(Parcel in) {
		    return new UserLocation(in);
		}
		public UserLocation[] newArray(int size) {
		    return new UserLocation[size];
		}
	};   
}
