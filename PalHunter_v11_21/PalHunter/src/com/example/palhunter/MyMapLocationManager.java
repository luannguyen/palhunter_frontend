package com.example.palhunter;

import java.util.List;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapLocationManager {
	static List<Overlay> mapOverlays;
	static MapView mapview;
	
	public MyMapLocationManager(List<Overlay> map_overlays, MapView map_view) {
		// TODO Auto-generated constructor stub
		mapOverlays = map_overlays;
		mapview = map_view;
	}
	public static void showUserCurrentLocation(User user) {
		mapOverlays.add(user.getCurrentLocation());
		mapview.postInvalidate();
		//show last location as currentlocation;
		
	}
	
	public static void hideUserCurrentLocation(User user) {
		
	}

	public static void showUserPastLocation(User user) {
		if(mapOverlays == null) {
			System.out.println("mapoverlay == null");
		} 
		if(user.getLocationItemizedOverlay() == null) {
			System.out.println("user.getLocationItemizedOverlay() == null");
		}
		mapOverlays.add(user.getLocationItemizedOverlay());
	//	mapview.postInvalidate();
	}
	
	public static void hideUserPastLocation(User user) {
		mapOverlays.remove(user.getLocationItemizedOverlay());
	//	mapview.postInvalidate();
	}

}
