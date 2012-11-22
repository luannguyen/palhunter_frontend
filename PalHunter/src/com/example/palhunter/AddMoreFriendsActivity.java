package com.example.palhunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    Map name_id_hash = new HashMap<String, String>();
    ArrayAdapter<String> adapter;
    ListView lview;
    String httpAddFriendList = "action=addFriend&pid1=%s&pid2=%s";
    String httpNonFriendList = "id=%d&action=findAllFriends";
    FriendListManagerHandler handlerMoreFriend;
    
    public void ViewFriends(View view)
    {
    	final String getFriendList = String.format(httpNonFriendList, myUser.userId);
    	DatabaseClient.get(getFriendList, null, handlerMoreFriend);
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_friends);
        
        handlerMoreFriend = new FriendListManagerHandler();
    	myUser = new User();
    	Intent intent = getIntent();
    	Bundle b = intent.getExtras();
    	myUser.userId = b.getInt("id");
    	myUser.firstName = b.getString("firstName");
    	myUser.lastName = b.getString("lastName");
    	String friendsArrayString = b.getString("nonfriends");
	    
    	JSONArray friendsArray = null;
    	try {
			friendsArray = new JSONArray(friendsArrayString);
			for(int i=0; i<friendsArray.length(); i++) {
				JSONObject friend = friendsArray.getJSONObject(i);
				String f_id = friend.getString("PID");
				String f_first_name = friend.getString("FIRST_NAME");
				String f_last_name = friend.getString("LAST_NAME");
				list.add(f_first_name+" "+f_last_name);
				name_id_hash.put(f_first_name+" "+f_last_name, f_id);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	    TextView tview = (TextView)findViewById(R.id.people_list);
	    tview.setText(myUser.firstName+ " "+myUser.lastName + " 's nonFriends");
	    lview = (ListView)findViewById(R.id.list_people);
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, list);
	    lview.setAdapter(adapter);
	    
	    
	    
	    /** Defining a click event listener for the button "Delete" */
	    OnClickListener listenerDel = new OnClickListener() {
	    	public void onClick(View v) {
	    		/** Getting the checked items from the listview */
	    		SparseBooleanArray checkedItemPositions = lview.getCheckedItemPositions();
	    		int itemCount = lview.getCount();
	    		String removed_friends = "";
	    		for(int i=itemCount-1; i >= 0; i--){
	    			if(checkedItemPositions.get(i)){
	    				removed_friends = removed_friends + name_id_hash.get(list.get(i)) + ",";
	    				adapter.remove(list.get(i));
	    				lview.setItemChecked(i, false);
	    			}
	    		}
	    		
	    		if(removed_friends.length()>0){
	    			removed_friends = removed_friends.substring(0,removed_friends.length()-1);
	    			String getFriendList = String.format(httpAddFriendList, myUser.userId,removed_friends);
	    			DatabaseClient.get(getFriendList, null, new JsonHttpResponseHandler());
	    		}
	        	adapter.notifyDataSetChanged();	    	    
	    	}
	    };
	    Button btnDel = (Button) findViewById(R.id.add_friends);
	    btnDel.setOnClickListener(listenerDel); 
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
	    	System.out.print(errorResponse.keys().toString());
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
	    
	    public void onSuccess(JSONObject locationObject) {
	    	System.out.println("get friend list on success jsonobject");
	    }
	    
		public void onSuccess(JSONArray friendsArray) {
			System.out.println("get my friend list handler on Success, there are "+ 
			friendsArray.length() + " friends");
			System.out.println(friendsArray.toString());
			Intent intent = new Intent(AddMoreFriendsActivity.this, FriendManagerActivity.class);
			intent.putExtra("id", myUser.userId);
			intent.putExtra("firstName", myUser.firstName);
			intent.putExtra("lastName", myUser.lastName);
			intent.putExtra("friends", friendsArray.toString());
			startActivity(intent);			

		}
	}
}

