package com.example.palhunter;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyLocation extends MapActivity {
	LocationItemizedOverlay itemizedoverlay;
	List<Overlay> mapOverlays;
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-palhunter");
	
    String httpPostURL = "http://hamedaan.usc.edu:8080/team17/QueryServlet?action=insertLocation&id=%d&lat_int=%d&long_int=%d&updated_time=%l";
    Integer userId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Intent intent = getIntent();
    	String message = intent.getStringExtra(CreateUserActivity.EXTRA_MESSAGE);
    	userId = Integer.parseInt(message);
    	
        setContentView(R.layout.activity_my_location);
        
 //       getActionBar().setDisplayHomeAsUpEnabled(true);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        itemizedoverlay = new LocationItemizedOverlay(drawable, this);
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_location, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private class myLocationListener implements LocationListener {
    	int latitudeValue, longitudeValue;
    	
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			latitudeValue = (int)(location.getLatitude()* 1000000);
			longitudeValue = (int)(location.getLongitude()*1000000);
			
			GeoPoint myPoint = new GeoPoint(latitudeValue,longitudeValue);
			OverlayItem overlayitem = new OverlayItem(myPoint, "hello!", "I'm in san mateo!");
			
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
//			myTimestamp = new Timestamp(myDate.getTime());
    		long pubDate = System.currentTimeMillis();

    		final String url = String.format(httpPostURL, userId, latitudeValue, longitudeValue,pubDate);
    		System.out.println(" send request:  " + url);
    		
    		final Runnable rr = new Runnable() {
				public void run() {
					HttpResponse responseStream;
		    		HttpPost httpPost = new HttpPost(url);
		    		try {
						responseStream = httpClient.execute(httpPost);
						System.out.println(responseStream.getEntity().getContent());
					} catch (Exception e) {
						System.out.println("cant connect to server to insert location information");
						e.printStackTrace();
					} 
				}
    		};
    		Thread submitDataThread = new Thread(rr);
    		submitDataThread.start();
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
}