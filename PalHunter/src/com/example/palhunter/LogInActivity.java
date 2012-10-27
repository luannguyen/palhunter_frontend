package com.example.palhunter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONObject;

import com.palhunter.object.UserMessage;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity {

	EditText firstNameText, lastNameText;
	Date myDate;
	int userId;
	Timestamp myTimestamp;
	String userName = "team17";
	String password = "palhunter1";
	User myUser;
	
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-palhunter");
    String httpQueryURL = "http://hamedaan.usc.edu:8080/team17/QueryServlet?id=%d&action=queryPeopleId";
    String httpQueryUserIDURL = "http://hamedaan.usc.edu:8080/team17/QueryServlet?" +
    		"first_name=%s&last_name=%s&action=queryPeopleName";
	
    public void LogIn(View view) {
		final Runnable rr = new Runnable() {
			public void run() {
		    	try {
		    	InputStream responseStream ;
				String firstName, lastName;
				
		    	firstNameText = (EditText)findViewById(R.id.first_name_login);
		    	firstName = firstNameText.getText().toString().trim();
		    	
		    	lastNameText = (EditText)findViewById(R.id.last_name_login);
		    	lastName = lastNameText.getText().toString().trim();
		    	
				final String url = String.format(httpQueryUserIDURL, firstName, lastName);
				HttpGet httpGet = new HttpGet(url);
		
		    		responseStream = httpClient.execute(httpGet).getEntity().getContent();
		    		myUser.getUser(responseStream);
		    		
		    	} catch (Exception e) {
					e.printStackTrace();
					return;
				} 
			}
		};
		Thread retriveDataThread = new Thread(rr);
		retriveDataThread.start();
   }
    
   public void CreateNewAccount(View view)
   {
		Intent intent = new Intent(this, CreateUserActivity.class);
		intent.putExtra("id", myUser.userId);
		intent.putExtra("firstName", myUser.firstName);
		intent.putExtra("lastName", myUser.lastName);
		startActivity(intent);
   }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        myUser = new User();
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_in, menu);
        return true;
    }
    
    
}
