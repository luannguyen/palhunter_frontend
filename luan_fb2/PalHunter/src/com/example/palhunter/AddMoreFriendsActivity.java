package com.example.palhunter;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

public class AddMoreFriendsActivity extends Activity {

    User myUser;
    ArrayList<String> list = new ArrayList<String>();
    HashMap<String, Integer> name_id_hash = new HashMap<String, Integer>();
    ArrayAdapter<String> adapter;
    ArrayList<User> candidates;
    
    ListView lview;
    String httpAddFriendList = "action=addFriend&pid1=%s&pid2=%s";
    String httpNonFriendList = "id=%d&action=findAllFriends";
    FriendListManagerHandler handlerMoreFriend;
    
    public void ViewFriends(View view)
    {
/*
    	Intent intent = getIntent();
		setResult(RESULT_OK, intent);	
		finish();
*/		
    	final String getFriendList = String.format(httpNonFriendList, myUser.userId);
    	DatabaseClient.get(getFriendList, null, handlerMoreFriend);
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_friends);
        
        handlerMoreFriend = new FriendListManagerHandler();
    	Intent intent = getIntent();
    	Bundle b = intent.getExtras();
    	if(b!=null)
    		myUser = (User)b.getParcelable(User.USER_TYPE);
    	
    	String nonFriendsArrayString = b.getString("nonfriends");  
    	JSONArray nonFriendsArray = null;
    	candidates = new ArrayList<User>();
    	try {
			nonFriendsArray = new JSONArray(nonFriendsArrayString);
			for(int i=0; i<nonFriendsArray.length(); i++) {
				JSONObject friend = nonFriendsArray.getJSONObject(i);
				User friendCandidate = new User();
				friendCandidate.userId = Integer.parseInt(friend.getString("PID"));
				friendCandidate.firstName = friend.getString("FIRST_NAME");
				friendCandidate.lastName = friend.getString("LAST_NAME");
				list.add(friendCandidate.getFullName());
				name_id_hash.put(friendCandidate.getFullName(), friendCandidate.userId);
				candidates.add(friendCandidate);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  /*
    	for(int i=0; i<myUser.friendList.size(); i++) {
    		User myFriend = myUser.friendList.get(i);
    		list.add(myFriend.getFullName());
    		name_id_hash.put(myFriend.getFullName(), myFriend.userId.toString());
    	}
	*/   
	    TextView tview = (TextView)findViewById(R.id.people_list);
	    tview.setText(myUser.getFullName() + " 's nonFriends");
	    lview = (ListView)findViewById(R.id.list_people);
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
	    lview.setAdapter(adapter);
	    
	    
	    
	    /** Defining a click event listener for the button "Delete" */
	    OnClickListener listenerAdd = new OnClickListener() {
	    	public void onClick(View v) {
	    		/** Getting the checked items from the listview */
	    		SparseBooleanArray checkedItemPositions = lview.getCheckedItemPositions();
	    		int itemCount = lview.getCount();
	    		
	    		String add_candidates = "";
	    		for(int i=itemCount-1; i >= 0; i--){
	    			if(checkedItemPositions.get(i)){
	    				add_candidates = add_candidates + name_id_hash.get(list.get(i)) + ",";
	    				//add i as a friend, load friend location into memory
	    				myUser.addFriend(candidates.get(i));
	    				adapter.remove(list.get(i));
	    				lview.setItemChecked(i, false);
	    			}
	    		}
	    		
	    		if(add_candidates.length()>0){
	    			add_candidates = add_candidates.substring(0, add_candidates.length()-1);
	    			String addFriendRequest = String.format(httpAddFriendList, myUser.userId, add_candidates);
	    			DatabaseClient.get(addFriendRequest, null, null);
	    		}
	        	adapter.notifyDataSetChanged();	    	    
	    	}
	    };
	    Button btnDel = (Button) findViewById(R.id.add_friends);
	    btnDel.setOnClickListener(listenerAdd); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_more_friends, menu);
        return true;
    }
    
    private final class FriendListManagerHandler extends JsonHttpResponseHandler {

	    public void onFailure(Throwable e,
                JSONObject errorResponse) {
	    	System.out.println("get friend list on failure jsonobject");
	    }
	    public void onFailure(Throwable e, JSONArray errorResponse) {
	    	System.out.println("get friend list on failure jsonarray");
	    	try {
		    	for(int i=0; i<errorResponse.length(); i++) {
		    		System.out.print(errorResponse.getJSONObject(i).keys().toString());
	    	}
	    	}catch (JSONException ee) {
				System.out.println("jsonarray failed to get friend list");
			}
	    }
	    
	    public void onSuccess(JSONObject friend) {
	    	System.out.println("add more friend success return jason object");
	    	setResult(RESULT_OK, getIntent());		
			finish();
	    	
	    }
		public void onSuccess(JSONArray friendsArray) {
			System.out.println("add more friend success return jason array");			
			Intent dataIntent = new Intent();
			Bundle dataBundle = new Bundle();
			
			for(int i=0; i<myUser.friendList.size(); i++) {
				System.out.println(myUser.friendList.get(i).getFullName());
				adapter.add(myUser.friendList.get(i).getFullName());
			}
			System.out.println("return to friend manager");

			dataBundle.putParcelable(User.USER_TYPE, myUser);
			dataIntent.putExtras(dataBundle);
			
			setResult(RESULT_OK, dataIntent);		
			finish();
		}
	}
}

