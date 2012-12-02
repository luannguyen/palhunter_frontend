package com.example.palhunter;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

import com.google.android.maps.GeoPoint;
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
		if(!user.getCurrentLocation().isEmpty())
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
//		mapOverlays.add(user.getPathOverlay());
//		mapOverlays.add(user.getLocationOverlay());
		user.setPathOverlayIndex(index);
		
		if(user.getPathOverlay() != null && !user.getPathOverlay().isEmpty()) 
			mapOverlays.add(user.getPathOverlay());
		if(user.getLocationOverlay() != null && !user.getLocationOverlay().isEmpty())
			mapOverlays.add(user.getLocationOverlay());;  

		mapview.invalidate();
	}
	
	public void hideUserPath(User user) {
		//need to keep track of the index of overlay in mapoverlay list
		if(user.getPathOverlay() != null) {
			mapOverlays.remove(user.getPathOverlay());
			mapOverlays.remove(user.getLocationOverlay());
			user.removePathOverlayIndex();
		}
		mapview.invalidate();
	}
	
	
	public void zoomInToUser(User user) {
		MapController controller = mapview.getController();
        controller.setZoom(19);
        controller.animateTo(user.getCurrentLocation().getCenter());
	}
	
	public void clear(User user) {
		mapOverlays.clear(); 
		mapOverlays.add(user.getCurrentLocation());
		mapview.invalidate();
	}
	
    private double distance(GeoPoint p1, GeoPoint p2) {
    	double x = p1.getLatitudeE6() / 1e6;
    	double y = p1.getLongitudeE6() / 1e6;
    	
    	double u_x = p2.getLatitudeE6()/ 1e6;
    	double u_y = p2.getLongitudeE6()/ 1e6;
    	    	
    	float results[] = new float[3]; 
    	Location.distanceBetween(x, y, u_x, u_y, results);
    	
    	return results[0];
    }
}
