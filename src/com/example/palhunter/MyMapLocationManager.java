package com.example.palhunter;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.MapController;
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
		int index = mapOverlays.size();
		mapOverlays.add(user.getCurrentLocation());
		user.setCurrentLocationOverlayIndex(index);
		mapview.invalidate();		
	}
	
	public void showUserCurrentLocaiton(ArrayList<User> users) {
		for(int i=0; i<users.size(); i++) {
			showUserCurrentLocation(users.get(i));
		}
	}
	
	public void hideUserCurrentLocation(User user) {
		mapOverlays.remove(user.getCurrentLocation());
		user.removeCurrentLocationOverlayIndex();
		mapview.invalidate();
	}

	public void showUserPath(User user) {
		int index = mapOverlays.size();
		mapOverlays.add(user.getPathOverlay());
		user.setPathOverlayIndex(index);
		mapview.invalidate();
	}
	
	public void hideUserPath(User user) {
		//need to keep track of the index of overlay in mapoverlay list
		mapOverlays.remove(user.getPathOverlay());
		user.removePathOverlayIndex();
		mapview.invalidate();
	}
	
	
	public void zoomInToUser(User user) {
		MapController controller = mapview.getController();
        controller.setZoom(17);
        controller.animateTo(user.getCurrentLocation().getCenter());
	}
	
	public void clear(User user) {
		mapOverlays.clear(); 
		mapOverlays.add(user.getCurrentLocation());
		mapview.invalidate();
	}
}
