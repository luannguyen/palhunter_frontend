package com.example.palhunter;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

public class LogInActivity extends FragmentActivity {

	EditText firstNameText, lastNameText;
	Date myDate;
	Timestamp myTimestamp;
	String userName = "team17";
	String password = "palhunter1";
	String firstName, lastName;
	int userId;
	LogInHandler handler;
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-palhunter");
    String httpQueryURL = "id=%d&action=queryPeopleId";
    String httpQueryUserByIdURL = "first_name=%s&last_name=%s&action=queryPeopleName";
	
    public void LogIn(View view) {

    	try {

			
	    	firstNameText = (EditText)findViewById(R.id.first_name_login);
	    	firstName = firstNameText.getText().toString().trim();
	    	
	    	lastNameText = (EditText)findViewById(R.id.last_name_login);
	    	lastName = lastNameText.getText().toString().trim();
	    
			final String url = String.format(httpQueryUserByIdURL, firstName, lastName);
			
			DatabaseClient.get(url, null, handler);
    		
    	} catch (Exception e) {
			e.printStackTrace();
			return;
		} 
   }
    
   public void CreateNewAccount(View view)
   {
		Intent intent = new Intent(this, CreateUserActivity.class);
		startActivity(intent);
   }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        handler = new LogInHandler();
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_in, menu);
        return true;
    }
    
	private class LogInHandler extends JsonHttpResponseHandler {

	    public LogInHandler()
	    {
	        super();
	    }
	    
		public void onSuccess(JSONArray userArray) {
			System.out.println("log in handler on Success");
			if(userArray.length() == 1) {
				try {			
					
					JSONObject userObject = userArray.getJSONObject(0);
					userId = userObject.getInt("PID");
					System.out.println("login got userId = " + userId);								
				} catch (JSONException e) {
					System.out.println("login handler on success failed to get user id");
				}
				SharedPreferences settings = getSharedPreferences(MainActivity.myPrefence,0);
				SharedPreferences.Editor e = settings.edit();
				e.putBoolean("logged", true);
				e.putInt("id", userId);
				e.putString("firstName", firstName);
				e.putString("lastName", lastName);
				e.commit();
				
				Intent intent = new Intent(LogInActivity.this, MyLocation.class);
				intent.putExtra("id", userId);
				intent.putExtra("firstName", firstName);
				intent.putExtra("lastName", lastName);
				startActivity(intent);	
			}
			else {
				MyAlertDialogFragment alertDialogFragment = new MyAlertDialogFragment();
				alertDialogFragment.show(getSupportFragmentManager(), "no account alert");
			}
		}
	}  
}
