package com.example.palhunter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {	
	public static final String myPrefence = "MY_PREF";
	
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
    
    public void onStart() {
    	super.onStart();
    	 
		SharedPreferences settings = getSharedPreferences(myPrefence, 0);
        boolean logged = settings.getBoolean("logged", false);
        if(logged == true) {
			Intent intent = new Intent(MainActivity.this, MyLocation.class);
			intent.putExtra("id", settings.getInt("id", 0));
			intent.putExtra("firstName", settings.getString("firstName", ""));
			intent.putExtra("lastName", settings.getString("lastName", ""));
			startActivity(intent);
        }  else { 
        	setContentView(R.layout.activity_main);
        }
    }
}
