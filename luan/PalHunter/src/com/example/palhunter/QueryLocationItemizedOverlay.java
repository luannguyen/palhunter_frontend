package com.example.palhunter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.google.android.maps.MapActivity;
import com.google.android.maps.OverlayItem;

public class QueryLocationItemizedOverlay extends LocationItemizedOverlay {

	private MapQueryActivity mapContext;
	private User myUser;
	public QueryLocationItemizedOverlay(Drawable arg0, Context context) {
		super(arg0, context);
		// TODO Auto-generated constructor stub
	}
	
	
	
	protected boolean onTap(int index) {
		  OverlayItem item = super.getMyOverlays().get(index);
		  
			LayoutInflater layoutInflater = (LayoutInflater) mapContext.getBaseContext()
					.getSystemService(mapContext.LAYOUT_INFLATER_SERVICE);
			final View popupView = layoutInflater.inflate(
					R.layout.popout_distance_selection, null);

			final PopupWindow popupWindow = new PopupWindow(popupView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setFocusable(true);
			
			Button search = (Button) popupView.findViewById(R.id.confirmBtn);
			
			EditText valueView = (EditText) popupView
					.findViewById(R.id.distanceValue);
			Double value = Double.parseDouble(valueView.getText()
					.toString());
			RadioGroup radioGroup = (RadioGroup) popupView
					.findViewById(R.id.measureGroup);
			popupWindow.dismiss();

			int id = radioGroup.getCheckedRadioButtonId();
			// default kms
			if (id == R.id.milesRadio) {
				value = value * 1.609;
			}
		/*
			mapContext.myLocationManager.clear(myUser);
			ArrayList<User> closeFriends = myUser
					.findFriendsWithinDistance(value);
			mapContext.myLocationManager.showUserCurrentLocaiton(closeFriends);
		 */
		
//		  mapContext.myLocationManager.showLocationsWithinDistance(item.getPoint(), value, myUser);
/*		  
		  AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.show();
*/
		  return true;
		}

}
