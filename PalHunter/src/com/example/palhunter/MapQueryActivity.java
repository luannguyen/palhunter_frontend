package com.example.palhunter;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapQueryActivity extends Activity {

	User myUser; 
	MapView mapView;
	List<Overlay> mapOverlays;
	MapController mapController;
	HashMap<Integer, User>userList;
	MyMapLocationManager myLocationManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_query);
    	Intent intent = getIntent();
    	Bundle b = intent.getExtras();
    	myUser = b.getParcelable(User.USER_TYPE);
    	
        mapView = (MapView) findViewById(R.id.mapview2);
        mapView.setBuiltInZoomControls(true);

        mapController = mapView.getController();
        mapOverlays = mapView.getOverlays();
        myLocationManager = new MyMapLocationManager(mapOverlays, mapView);
        
        TextView nameField = (TextView)findViewById(R.id.nameField2);
        nameField.setText(myUser.firstName + " " + myUser.lastName);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_query, menu);
        return true;
    }
    
    public void queryFriendWithinDistance(View view) {
    	
    }
}
