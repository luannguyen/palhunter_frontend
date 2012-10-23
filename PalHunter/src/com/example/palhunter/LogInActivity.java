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
	
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-palhunter");
    String httpQueryURL = "http://hamedaan.usc.edu:8080/team17/QueryServlet?id=%d&action=queryPeopleId";
	
    private class RetrieveDataTask extends AsyncTask <String, Void, List<UserMessage>> {
    	InputStream responseStream ;
		@Override
		protected List<UserMessage> doInBackground(String... arg0) {
			final String url = String.format(httpQueryURL, userId);
			HttpGet httpGet = new HttpGet(url);
	    	
	    	try {
	    		
	    		responseStream = httpClient.execute(httpGet).getEntity().getContent();
	    		
	    		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
	    	    StringBuilder sb = new StringBuilder();
	    	    String line = null;

	    	    while ((line = reader.readLine()) != null) {
	    	        sb.append(line);
	    	    }
	    	    
	    	    JSONArray jsonArray = new JSONArray(sb.toString());
	    	    int size = jsonArray.length();
	    	    
	    	    List<UserMessage> records = new ArrayList<UserMessage>();
	    	    
	    	    System.out.println("USER LIST");
	    	    for (int i = 0; i < size; i++) {
	    	    	JSONObject jo = jsonArray.getJSONObject(i);
	    	            records.add(new UserMessage(jo.getString("username").toString(),jo.getString("message").toString(), Long.parseLong(jo.getString("pubdate").toString())));
	    	            System.out.println(jo.getString("username").toString() + " " + jo.getString("message").toString() + " " + Long.parseLong(jo.getString("pubdate").toString()));
	    	    }
	    	    
	    		
	    		return records;
	    	} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
			
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
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_log_in, menu);
        return true;
    }
    
    
}
