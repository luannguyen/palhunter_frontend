package com.example.palhunter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FriendListAdapter extends ArrayAdapter<User> /*implements
		OnClickListener */{
	ArrayList<User> myUserList;
	MyMapLocationManager mapLocationManager;
	final static int ROW = 1;
	final static int COLUMN = 2;
	final static int CURRENT_BUTTON = 3;
	final static int PAST_BUTTON = 4;

	private LayoutInflater myInflater;

	public FriendListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}


	public FriendListAdapter(Context context, int textViewResourceId,
			User[] objects) {
		super(context, textViewResourceId, objects);
		myInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myUserList = new ArrayList<User>(Arrays.asList(objects));
	}

	public FriendListAdapter(Context context, int textViewResourceId,
			ArrayList<User> objects) {
		super(context, textViewResourceId, objects);
		myInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myUserList = objects;
	}

	public FriendListAdapter(Context context, int resource,
			int textViewResourceId, User[] objects) {
		super(context, resource, textViewResourceId, objects);
		myInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myUserList = new ArrayList<User>(Arrays.asList(objects));
	}

	public FriendListAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<User> objects) {
		super(context, resource, textViewResourceId, objects);
		myInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		myUserList = objects;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		System.out.println("getView " + pos);
		View v = myInflater.inflate(R.layout.list_friend_item_layout, parent,
				false);
		/* Get the item in the adapter */
		User myFriend = getItem(pos);

		TextView friendNameView = (TextView) v
				.findViewById(R.id.friendNameView);
		CheckBox currentLocationButton = (CheckBox) v
				.findViewById(R.id.currentLocationRadioBtn);
		CheckBox pastLocationButton = (CheckBox) v
				.findViewById(R.id.pastLocationRadioBtn);
		friendNameView.setText(myFriend.getFullName());

		friendNameView.setTag(pos);
		friendNameView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				User user = myUserList.get((Integer)v.getTag());
				mapLocationManager.zoomInToUser(user);
			}
		});
		
		currentLocationButton.setTag(pos);
		currentLocationButton.setChecked(true);
		currentLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				User user = myUserList.get((Integer)v.getTag());
				CheckBox checkBox = (CheckBox) v;
				if (checkBox.isChecked()) {
					mapLocationManager.showUserCurrentLocation(user);
				} else {
					mapLocationManager.hideUserCurrentLocation(user);
				}
			}
		});
		
		
		pastLocationButton.setTag(pos);
		pastLocationButton.setChecked(true);
		pastLocationButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				User user = myUserList.get((Integer)v.getTag());
				CheckBox checkBox = (CheckBox) v ; 
				if (checkBox.isChecked()) {
					mapLocationManager.showUserPastLocation(user);
				} else {
					mapLocationManager.hideUserPastLocation(user);
				}
			}
		});

		return v;

	}
	
	
/*
	public void onClick(View view) {

		int position = (Integer) view.getTag();
		System.out.println("view position: "+ position);
		int buttonType = (Integer) view.getTag(R.integer.LocationBtnId);
		User user = myUserList[position];
		final CheckBox checkBox = (CheckBox) view;
		boolean checked = checkBox.isChecked();
//		checkBox.setChecked(!checkBox.isChecked());
		System.out.println("checked = " + checked);
		if (buttonType == CURRENT_BUTTON) {
			if (checked) {
				System.out.println("current button checked");
				checkBox.setChecked(false);
				mapLocationManager.showUserCurrentLocation(user);
			} else {
				System.out.println("current button unchecked");
				checkBox.setChecked(true);
				mapLocationManager.hideUserCurrentLocation(user);
			}
		} else if (buttonType == PAST_BUTTON) {
			if (checked) {
				System.out.println("past button checked");
				checkBox.setChecked(false);
				mapLocationManager.showUserPastLocation(user);
			} else {
				System.out.println("past button unchecked");
				checkBox.setChecked(true);
				mapLocationManager.hideUserPastLocation(user);
			}
		}
	
	}
*/	
}
