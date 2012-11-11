package com.example.palhunter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<User> {
	
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
	    friendNameView.setText(myFriend.toString());
	    
	    return v;
	}

}
