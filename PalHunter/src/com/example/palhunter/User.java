package com.example.palhunter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

public class User {

	Integer userId;
	String firstName, lastName;
	long createdTime;
	ArrayList<User> friends;
	ArrayList<UserLocation> myPastLocations;
	static final int MAX_HISTORY_LOCATION_NUM = 100;
	public User() {
		// TODO Auto-generated constructor stub
		myPastLocations = new ArrayList<UserLocation>();
	}

	public void addUserLoctaion(UserLocation loc) {
		if(myPastLocations.size() < MAX_HISTORY_LOCATION_NUM) {
			myPastLocations.add(loc);
		}
		else {
			//location buffer is full, delete the oldest location history
		}
	}
	
	public void getUser(InputStream responseStream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				responseStream));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONArray joa = new JSONArray(sb.toString());
			JSONObject jo = joa.getJSONObject(0);
			userId = Integer.parseInt(jo.getString("PID").toString());
			firstName = jo.getString("FIRST_NAME").toString();
			lastName = jo.getString("LAST_NAME").toString();
			createdTime = Long.parseLong(jo.getString("CREATED_TIME")
					.toString());

		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * public void showPastLocations(LocationItemizedOverlay itemizedoverlay) {
	 * int i; for(i=0; i<myPastLocations.) }
	 */
	/*
	 * public ArrayList<User> getUserFriends() {
	 * 
	 * }
	 */
	public void getLocations(InputStream responseStream) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				responseStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		int i;
		int longtitude, latitude;
		long time;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			JSONArray joa = new JSONArray(sb.toString());
			for (i = 0; i < joa.length(); i++) {
				longtitude = joa.getJSONObject(i).getInt("LONG_INT");
				latitude = joa.getJSONObject(i).getInt("LAT_INT");
				time = joa.getJSONObject(i).getLong("UPDATED_TIME");
				myPastLocations.add(new UserLocation(longtitude, latitude, time));
			}
		} catch (Exception e) {
			System.out.println("failed to get user past locations");
			throw e;
		}
	}

	public static void getTotalUserNumber(final JsonHttpResponseHandler myHandler) throws Exception {

		DatabaseClient.get("action=getTotalPeople", null, myHandler);

		/*
		 * int userNum = 0; BufferedReader in; in = new BufferedReader(new
		 * InputStreamReader(response.getEntity().getContent())); StringBuffer
		 * sb = new StringBuffer(""); String line = ""; String NL =
		 * System.getProperty("line.separator"); try { while ((line =
		 * in.readLine()) != null) { sb.append(line + NL); } in.close();
		 * }catch(IOException e){throw e;}
		 * 
		 * String queryResponseStr = sb.toString();
		 * System.out.println(queryResponseStr);
		 * 
		 * //{"TOTAL":"6"} String[] tokens1 = queryResponseStr.split(":");
		 * String[] tokens2 = tokens1[1].split("\""); try { userNum =
		 * Integer.parseInt(tokens2[1]);
		 * System.out.println("parse succeed! user number: "+ userNum); } catch
		 * (Exception e ) { System.out.println("parse failed"); }
		 * 
		 * return userNum;
		 */}

}
