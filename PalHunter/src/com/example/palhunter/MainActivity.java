package com.example.palhunter;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {	
	
	public void CreateUser(View view) {
		Intent intent = new Intent(this, CreateUserActivity.class);
		startActivity(intent);
	}
    public void LogIn(View view){
		Intent intent = new Intent(this, LogInActivity.class);
		startActivity(intent);
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }
    
    public void updateButton(Button button) {
    	button.setText("clicked");
    }
    
    public void onClick(View button) {
    	updateButton((Button) button);
    }
}
