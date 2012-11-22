package com.example.palhunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FriendManagerActivity extends Activity {

    User myUser;
    ArrayList<String> list = new ArrayList<String>();
    Map name_id_hash = new HashMap<String, String>();
    ArrayAdapter<String> adapter;
    ListView lview;
    String httpRemoveFriendList = "action=removeFriend&pid1=%s&pid2=%s";
    String httpNonFriendList = "action=findAllNonFriends&id=%d";
    AddMoreFriendsHandler handlerMoreFriend;
    
    public void AddMoreFriends(View view)
    {
    	final String getFriendList = String.format(httpNonFriendList, myUser.userId);
    	DatabaseClient.get(getFriendList, null, handlerMoreFriend);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	    setContentView(R.layout.activity_friend_manager);
	    handlerMoreFriend = new AddMoreFriendsHandler();
    	Bundle b = this.getIntent().getExtras();
    	
    	if(b!=null)
    	    myUser = b.getParcelable(User.USER_TYPE);
    	
    	for(int i=0; i<myUser.friendList.size(); i++) {
    		User friend = myUser.friendList.get(i);
			String f_id = friend.userId.toString();
			String f_first_name = friend.firstName;
			String f_last_name = friend.lastName;
			list.add(f_first_name+" "+f_last_name);
			name_id_hash.put(f_first_name+" "+f_last_name, f_id);
    	}
  /*  	
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
  */  	
	    TextView tview = (TextView)findViewById(R.id.myname);
	    tview.setText(myUser.firstName+ " "+myUser.lastName + " 's Friends");
	    lview = (ListView)findViewById(R.id.list);
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
	    			String getFriendList = String.format(httpRemoveFriendList, myUser.userId,removed_friends);
	    			DatabaseClient.get(getFriendList, null, null);
	    			myUser.removeFriend(Integer.parseInt(removed_friends));
	    		}
	    		
	        	adapter.notifyDataSetChanged();	
	        	
	    	}
	    };
	    Button btnDel = (Button) findViewById(R.id.remove_friends);
	    btnDel.setOnClickListener(listenerDel); 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_friend_list, menu);
        return true;
    }
    
    private final class AddMoreFriendsHandler extends JsonHttpResponseHandler {

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
			Intent intent = new Intent(FriendManagerActivity.this, AddMoreFriendsActivity.class);	
			intent.putExtra("id", myUser.userId);
			intent.putExtra("firstName", myUser.firstName);
			intent.putExtra("lastName", myUser.lastName);
			intent.putExtra("nonfriends", friendsArray.toString());
			startActivity(intent);			

		}
	}
}

