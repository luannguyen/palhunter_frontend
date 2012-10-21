package com.example.palhunter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView latitute;
	TextView longitute;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        latitute = (TextView)findViewById(R.id.editText1);
        longitute = (TextView)findViewById(R.id.editText2);
        
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new myLocationListener();
        
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll); 
    }
    
    private class myLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Double latituteValue = location.getLatitude();
			Double longituteValue = location.getLongitude();
			
			latitute.setText(latituteValue.toString());
			longitute.setText(longituteValue.toString());
		
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void updateButton(Button button) {
    	button.setText("clicked");
    }
    
    public void onClick(View button) {
    	updateButton((Button) button);
    }
}
