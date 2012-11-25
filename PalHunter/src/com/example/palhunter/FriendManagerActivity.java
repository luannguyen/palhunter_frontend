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

public class FriendManagerActivity extends Activity {

    User myUser;
    ArrayList<String> list;
    HashMap<String, Integer> name_id_hash;
    ArrayAdapter<String> adapter;
    ListView lview;
    String httpRemoveFriendList = "action=removeFriend&pid1=%s&pid2=%s";
    String httpNonFriendList = "action=findAllNonFriends&id=%d";
    AddMoreFriendsHandler handlerMoreFriend;
    Intent intentFromMyLocation;
    
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
	    intentFromMyLocation = getIntent();
    	Bundle b = intentFromMyLocation.getExtras();
    	list = new ArrayList<String>();
    	name_id_hash = new HashMap<String, Integer>();
    	if(b!=null)
    	    myUser = (User)(b.getParcelable(User.USER_TYPE));
    	
    	for(int i=0; i<myUser.friendList.size(); i++) {
    		User friend = myUser.friendList.get(i);
			list.add(friend.getFullName());
			name_id_hash.put(friend.getFullName(), friend.userId);
    	}
    	
    	System.out.println("here");
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
	    				System.out.println("selected: " + i + " name: " + list.get(i) + " user id: " + name_id_hash.get(list.get(i)));
	    				myUser.removeFriend(name_id_hash.get(list.get(i)));
	    				adapter.remove(list.get(i));
	    				lview.setItemChecked(i, false);
	    			}
	    		}
	    		
	    		if(removed_friends.length()>0){
	    			removed_friends = removed_friends.substring(0,removed_friends.length()-1);
	    			String getFriendList = String.format(httpRemoveFriendList, myUser.userId, removed_friends);
	    			DatabaseClient.get(getFriendList, null, null);		
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
    
    public void BackToMyMap(View v) {		
    	Intent dataIntent = new Intent();
    	dataIntent.putExtra(User.USER_TYPE, myUser);
    	
		setResult(RESULT_OK, dataIntent);
		finish();
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
			Bundle b = new Bundle();
			b.putParcelable(User.USER_TYPE, myUser);
			b.putString("nonfriends", friendsArray.toString());
			intent.putExtras(b);
			
			startActivityForResult(intent,0);			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) { 
		case RESULT_OK:
			Bundle b = data.getExtras();  
			myUser = (User)b.getParcelable(User.USER_TYPE);
			adapter.clear();
			for(int i=0; i<myUser.friendList.size(); i++) {
				System.out.println(myUser.friendList.get(i).getFullName());
				adapter.add(myUser.friendList.get(i).getFullName());
			}
			System.out.println("return from add more friend ok");
            break;
            
		default:
	        break;
		}
	}
}

