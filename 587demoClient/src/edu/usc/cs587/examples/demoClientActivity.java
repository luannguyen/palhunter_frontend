package edu.usc.cs587.examples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;


import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.*;

import edu.usc.cs587.examples.objects.UserMessage;

public class demoClientActivity extends Activity {
	
	protected static String SERVER_ADDRESS = "";
	protected static String POST_SERVLET = "";
	protected static String QUERY_SERVLET = "";
	
	private EditText edtName;
	private EditText edtResult;
	private Button submit;
	private Button query;
	
	HttpClient httpClient = AndroidHttpClient.newInstance("Android-iCampus");
    String httpPostURL = "http://hamedaan.usc.edu:8080/587demo/PostServlet?username=%s&random=%s&pubdate=%d";
    String httpQueryURL = "http://hamedaan.usc.edu:8080/587demo/QueryServlet";
	Handler handler; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        edtName = (EditText)findViewById(R.id.editText1);
        submit = (Button)findViewById(R.id.button1);
        query = (Button)findViewById(R.id.button2);
        
        edtResult = (EditText)findViewById(R.id.editText2);
        handler = new Handler();
    }

    public void submitData(View view) {
    	String username = edtName.getText().toString().trim();
    	if (username.length() <= 0) {
    		Toast.makeText(this, "please input your name", Toast.LENGTH_SHORT).show();
    	} else {
    		SecureRandom r = new SecureRandom();
    		String random = new BigInteger(100, r).toString(32);
    		long pubDate = System.currentTimeMillis();
    		final String url = String.format(httpPostURL, username, random, pubDate);
    		Thread submitDataThread = new Thread(new Runnable() {

				@Override
				public void run() {
					InputStream responseStream;
		    		HttpPost httpPost = new HttpPost(url);
		    		try {
						responseStream = httpClient.execute(httpPost).getEntity().getContent();
					} catch (Exception e) {
						e.printStackTrace();
					} 
					
				}
    			
    		});
    		submitDataThread.start();
    	}
    }
    
    
    
	public void retrieveData(View view) {
    	handler.post(new Runnable(){

			@Override
			public void run() {
				new RetrieveDataTask().execute();
			}
    		
    	});
//    	Thread retrieveDataThread = new Thread (new Runnable() {
//
//			@Override
//			public void run() {
//				
//			}
//    		
//    	});
//    	retrieveDataThread.start();
    	
    }
    
    private void updateResultList(List<UserMessage> results) {
    	edtResult.getText().clear();
    	int i=0;
    	if (results == null || results.size() == 0) {
    		edtResult.setText("Received no result. \n");
    	}else if (results.size() <= 1) {
    		edtResult.setText("Received "+ results.size() + " record. \n");
    	}else {
    		edtResult.setText("Received "+ results.size() + " records. \n");
    	}
    	for (UserMessage um : results) {
			edtResult.getText().append("R"+i+"=" + um.toString()+"\n");
			i++;
		}
    }
    
    private void errorHappened(String errorMsg) {
    	edtResult.setText(errorMsg);
    }
    private class RetrieveDataTask extends AsyncTask <String, Void, List<UserMessage>> {
    	InputStream responseStream ;
		@Override
		protected List<UserMessage> doInBackground(String... arg0) {
			HttpGet httpGet = new HttpGet(httpQueryURL);
	    	
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
				errorHappened("Exception happened. Please check the DDMS.");
				return null;
			} 
			
		}

		@Override
		protected void onPostExecute(List<UserMessage> result) {
			if (responseStream != null) {
				Log.d("", "TRUCK_RESPNONSE:"+ "Finish parsing foodtruck feeds.");
				try {
					responseStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}catch (Exception e) {
					e.printStackTrace();
				} 
				if (result !=null && result.size()>0) {
					updateResultList(result);	
				}
				
			}
		}
    	
    }
}