package com.example.palhunter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<User> {
	User[] myUserList; 
	
	private LayoutInflater myInflater;
	public FriendListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	public FriendListAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
		myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public FriendListAdapter(Context context, int textViewResourceId,
			User[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myUserList = objects;
	}

	public FriendListAdapter(Context context, int textViewResourceId,
			List<User> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public FriendListAdapter(Context context, int resource,
			int textViewResourceId, User[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	public FriendListAdapter(Context context, int resource,
			int textViewResourceId, List<User> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent)
	{       
		System.out.println("getView " + pos);
	    View v=myInflater.inflate(R.layout.list_friend_item_layout, parent, false);
	    
	    /* Get the item in the adapter */
	    User myFriend = getItem(pos);
	    
	    TextView friendNameView = (TextView)v.findViewById(R.id.friendNameView);
	    RadioButton currentLocationButton = (RadioButton)v.findViewById(R.id.currentLocationRadioBtn);
	    RadioButton pastLocationButton = (RadioButton)v.findViewById(R.id.pastLocationRadioBtn);
	    friendNameView.setText(myFriend.toString());
	    
	    friendNameView.setTag(pos);
	    currentLocationButton.setTag(pos);
	    currentLocationButton.setOnClickListener(mOnCurrentLocationClickListener);
	    pastLocationButton.setTag(pos);
	    pastLocationButton.setOnClickListener(mPastLocationClickListener);
	    
	    return v;
	    
	}
/*	
	private class RowHolder {
		String userName;
		RadioButton currentLocationButton;
		RadioButton pastLocationButton;
	}
*/
	    private OnClickListener mOnCurrentLocationClickListener = new OnClickListener() {
	        public void onClick(View view) {
	        	int position =  (Integer)view.getTag();
	        	User user = myUserList[position];
	            System.out.println(String.format("current location radio button clicked, row %d", position));
	  
	            boolean checked = ((RadioButton) view).isChecked();
	            
                if (checked) {
                    MyMapLocationManager.showUserCurrentLocation(user);
                } else {
                	MyMapLocationManager.hideUserCurrentLocation(user);
                }
	        }
	    };

	    private OnClickListener mPastLocationClickListener = new OnClickListener() {
	        public void onClick(View view) {
	        	int position = (Integer)view.getTag();
	        	User user = myUserList[position];
	        	if(user == null) {
	        		System.out.println("user == null");
	        	} else {
	        		System.out.println("user id = " + user.userId);
	        	}
	        	
	            System.out.println(String.format("past location radio button clicked, row %d", position));
	            boolean checked = ((RadioButton) view).isChecked();
                if (checked) {
                    MyMapLocationManager.showUserPastLocation(user);
                } else {
                	MyMapLocationManager.hideUserPastLocation(user);
                }
	        }
	    };

}
