package com.example.palhunter;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class CreateUserActivity extends Activity {
	
	EditText firstNameText, lastNameText;
	Date myDate;
	Integer userId;
	boolean getUserIdFlag = false;
	Timestamp myTimestamp;
	String userName = "team17";
	String password = "palhunter1";
	
	
	public final static String EXTRA_MESSAGE = "com.example.palhunter.MESSAGE";
    String httpInserUserURL = "id=%d&first_name=%s&last_name=%s&created_time=%d&action=insertPeople";
    final String httpGetUserNumQuery = "action=getTotalPeople";
    String firstName, lastName;
    UserAccountHandler handler; 
	HttpClient httpClient = new DefaultHttpClient();
	
	private class UserAccountHandler extends JsonHttpResponseHandler {

	    public UserAccountHandler()
	    {
	        super();
	    }
	    
		public void onSuccess(JSONArray userNumArray) {
			int totalUserNum = 0;
			System.out.println("here on Success");
			try {
				
				JSONObject firstValue = userNumArray.getJSONObject(0);
				totalUserNum = firstValue.getInt("TOTAL");
				userId = totalUserNum+1;
				getUserIdFlag = true;
				System.out.println("got userId = " + userId);
		    	long pubDate = System.currentTimeMillis();
		    	final String url = String.format(httpInserUserURL, userId, firstName, lastName, pubDate);
		    	DatabaseClient.get(url, null, null);
		    	
				Intent intent = new Intent(CreateUserActivity.this, MyLocation.class);
				System.out.println("id " + userId + " firstName " + firstName + " lastName " + lastName);
				intent.putExtra("id", userId);
				intent.putExtra("firstName", firstName);
				intent.putExtra("lastName", lastName);
				startActivity(intent);	
								
			} catch (JSONException e) {
				System.out.println("databaseClient failed to get total user number");
			}
			// Do something with the response
			System.out.println("databaseClient total user number " + totalUserNum);
		}
	}
	
    public void SubmitData(View view) throws InterruptedException {
    	System.out.println("submit data");
    	
    	firstNameText = (EditText)findViewById(R.id.first_name);
    	firstName = firstNameText.getText().toString().trim();
    	
    	lastNameText = (EditText)findViewById(R.id.last_name);
    	lastName = lastNameText.getText().toString().trim();
    	
    	//random user id??
    	userId = 10;
    	if (firstName.length() <= 0) {
    		Toast.makeText(this, "please input your name", Toast.LENGTH_SHORT).show();
    	} else {
    		System.out.println("got message: " + firstName + "  " + lastName);	
		    try{
		    	DatabaseClient.get(httpGetUserNumQuery, null, handler);
		    	//	User.getTotalUserNumber(handler) ; 
		    }catch(Exception e) {
		    	System.out.println("can't get total user number");
		    	e.printStackTrace();
		    }
		    
		   
/*		    
		    try{
		    	long pubDate = System.currentTimeMillis();
		    	final String url = String.format(httpInserUserURL, userId, firstName, lastName, pubDate);
		    	DatabaseClient.get(url, null, null);
		    	//	User.getTotalUserNumber(handler) ; 
		    }catch(Exception e) {
		    	System.out.println("databaseClient failed to insert new user");
		    	e.printStackTrace();
		    }
		    
    		final Runnable rr = new Runnable() {
				public void run() {
	    			HttpGet getUserNumUrl = new HttpGet(httpGetUserNumQuery);
		    		HttpResponse response;
		    		try {
						response = httpClient.execute(getUserNumUrl);
						//userId = User.getTotalUserNumber(response) + 1;

		                
					//	System.out.println("get user number response: " + response.getEntity().getContent().toString());
					//	userId = Integer.parseInt(response.getEntity().getContent().toString());
						
						response.getEntity().consumeContent();
					} catch (Exception e) {
						System.out.println("cant get total user number");
						e.printStackTrace();
					} 
		    		long pubDate = System.currentTimeMillis();
		    		final String url = String.format(httpInserUserURL, userId, firstName, lastName, pubDate);
		    		System.out.println("send request:  " + url);
		    		
					HttpResponse responseStream;
					HttpGet httpPost = new HttpGet(url);
		    		try {
						responseStream = httpClient.execute(httpPost);
						System.out.println(EntityUtils.toString(responseStream.getEntity()));
						responseStream.getEntity().consumeContent();
					} catch (ClientProtocolException e) {
				        // TODO Auto-generated catch block
						System.out.println("can't connect to insert ppl");
				    } catch (IOException e) {
				        // TODO Auto-generated catch block
				    } 
				}
    		};
    	//	Thread submitDataThread = new Thread(rr);
    	//	submitDataThread.start();
    	}
  */  	
		
	
    	}
    }
    
    public void BackToLogin(View view){
		Intent intent = new Intent(this, LogInActivity.class);
		startActivity(intent);
    }
    
 /*   
	public void retrieveData(View view) {
		final Runnable rr = new Runnable() {
			public void run() {
				new RetrieveDataTask().execute();
			}
		};
    	handler.post(rr);
    	
    }
	*/  
	  
	
	
    @SuppressLint({ "NewApi", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    handler = new UserAccountHandler();
	    setContentView(R.layout.activity_create_user);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_create_user, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*        
    	switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
*/        
        return super.onOptionsItemSelected(item);
    }
}
