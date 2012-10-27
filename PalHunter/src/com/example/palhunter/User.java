package com.example.palhunter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import com.palhunter.object.UserMessage;

public class User {

	Integer userId;
	String firstName, lastName;
	long createdTime;
	ArrayList<Integer> friendsID;
	ArrayList<User> friends;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public void getUser(InputStream responseStream) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    
	    try{
		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }
		   
		    JSONObject jo = new JSONObject(sb.toString());
		    userId = Integer.parseInt(jo.getString("PID").toString());
		    firstName = jo.getString("FIRST_NAME").toString();
		    lastName = jo.getString("LAST_NAME").toString();
		    createdTime = Long.parseLong(jo.getString("CREATED_TIME").toString());
		     
	    }catch (Exception e) {
			throw e;
		} 
	}
/*	
	public ArrayList<User> getUserFriends()
	{
		
	}
*/	
	
	public static int getTotalUserNumber(HttpResponse response) throws IOException
	{
		int userNum = 0;
		BufferedReader in;
        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        try {
	        while ((line = in.readLine()) != null) {
	            sb.append(line + NL);
	        }
	        in.close();
        }catch(IOException e){throw e;}
        
        String queryResponseStr = sb.toString();
        System.out.println(queryResponseStr);
        
        //{"TOTAL":"6"}
        String[] tokens1 = queryResponseStr.split(":");
        String[] tokens2 = tokens1[1].split("\"");
    	try
    	{
    	    userNum = Integer.parseInt(tokens2[1]);
    	    System.out.println("parse succeed! user number: "+ userNum);
    	}
    	catch (Exception e )
    	{
    		System.out.println("parse failed");
    	}
        
        return userNum;
	}
}
