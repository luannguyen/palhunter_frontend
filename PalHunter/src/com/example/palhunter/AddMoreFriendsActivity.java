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
    HashMap<String, Integer> name_id_hash = new HashMap<String, Integer>();
    ArrayAdapter<String> adapter;
    ArrayList<User> candidates;
    
    ListView lview;
    String httpAddFriendList = "action=addFriend&pid1=%s&pid2=%s";
    String httpNonFriendList = "id=%d&action=findAllFriends";
    FriendListManagerHandler handlerMoreFriend;
    
    public void ViewFriends(View view)
    {
    	Intent intent = getIntent();
		setResult(RESULT_OK, intent);	
		finish();
		
  //  	final String getFriendList = String.format(httpNonFriendList, myUser.userId);
 //  	DatabaseClient.get(getFriendList, null, handlerMoreFriend);
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_friends);
        
        handlerMoreFriend = new FriendListManagerHandler();
    	myUser = new User();
    	Intent intent = getIntent();
    	Bundle b = intent.getExtras();
    	if(b!=null)
    		myUser = b.getParcelable(User.USER_TYPE);
    	
    	String nonFriendsArrayString = b.getString("nonfriends");
   /* 	
    	myUser.userId = b.getInt("id");
    	myUser.firstName = b.getString("firstName");
    	myUser.lastName = b.getString("lastName");
    	String friendsArrayString = b.getString("nonfriends");
	*/    
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
	    				myUser.addFriend(candidates.get(i));
	    				adapter.remove(list.get(i));
	    				lview.setItemChecked(i, false);
	    			}
	    		}
	    		
	    		if(add_candidates.length()>0){
	    			add_candidates = add_candidates.substring(0, add_candidates.length()-1);
	    			String getFriendList = String.format(httpAddFriendList, myUser.userId, add_candidates);
	    			DatabaseClient.get(getFriendList, null, null);
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
/*		
			Bundle b = new Bundle();
			b.putParcelable(User.USER_TYPE, myUser);
			intent.putExtras(b);
*/			
	//		setResult(RESULT_OK, getIntent());
	//		startActivity(intentFromFriendManager);		
			
			finish();
		}
	}
}

