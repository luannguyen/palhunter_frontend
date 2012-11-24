package com.example.palhunter;

import java.util.List;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapLocationManager {
	List<Overlay> mapOverlays;
	MapView mapview;
	
	public MyMapLocationManager(List<Overlay> map_overlays, MapView map_view) {
		// TODO Auto-generated constructor stub
		mapOverlays = map_overlays;
		mapview = map_view;
	}
	public void showUserCurrentLocation(User user) {
		mapOverlays.add(user.getCurrentLocation());
		mapview.invalidate();
		//show last location as currentlocation;
		
	}
	
	public void hideUserCurrentLocation(User user) {
		mapOverlays.remove(user.getCurrentLocation());
		mapview.invalidate();
	}

	public void showUserPastLocation(User user) {
		mapOverlays.add(user.getLocationItemizedOverlay());
		mapview.invalidate();
	}
	
	public void hideUserPastLocation(User user) {
		mapOverlays.remove(user.getLocationItemizedOverlay());
		mapview.invalidate();
	}

}
