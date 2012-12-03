package com.example.palhunter;

import com.facebook.FacebookActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FacebookActivity {	
	public static final String myPrefence = "MY_PREF";
    Button buttonLoginActivity;
	
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
        this.openSession();
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
    
    @Override
    protected void onSessionStateChange(SessionState state, Exception exception) {
      // user has either logged in or not ...
      if (state.isOpened()) {
        // make request to the /me API
        Request request = Request.newMeRequest(
          this.getSession(),
          new Request.GraphUserCallback() {
            // callback after Graph API response with user object
  
            public void onCompleted(GraphUser user, Response response) {
              if (user != null) {
      			Intent intent = new Intent(MainActivity.this, MyLocation.class);
      			intent.putExtra("id", Integer.parseInt(user.getId()));
    			String[] names = user.getName().split(" ");
    			intent.putExtra("firstName", names[0]);
    			intent.putExtra("lastName", names[1]);
    			intent.putExtra("access_token", Session.getActiveSession().getAccessToken());
    			startActivity(intent);
//                TextView welcome = (TextView) findViewById(R.id.authButton);
//                welcome.setText("Hello " + user.getName() + "!");
              }
            }
          }
        );
        Request.executeBatchAsync(request);
      }
    }
}
