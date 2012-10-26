package com.example.palhunter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

public class User {

	Integer userId;
	String firstName, lastName;
	long createdTime;
	ArrayList<Integer> friendsID;
	ArrayList<User> friends;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
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
