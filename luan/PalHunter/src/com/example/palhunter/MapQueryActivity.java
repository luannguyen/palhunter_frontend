package com.example.palhunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MapQueryActivity extends MapActivity {

	User myUser;
	MapView mapView;
	List<Overlay> mapOverlays;
	MapController mapController;
	HashMap<Integer, User> userList;
	MyMapLocationManager myLocationManager;
	Drawable drawable;
	
	String httpKnnQuery = "id=%d&action=queryKNN&kfriends=%d&lat=%d&lon=%d";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_query);
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);

		mapView = (MapView) findViewById(R.id.mapview2);
		mapView.setBuiltInZoomControls(true);

		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		myLocationManager = new MyMapLocationManager(mapOverlays, mapView);

		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		myUser = b.getParcelable(User.USER_TYPE);
		myUser.addAllLocations(drawable, this, mapView);
		
		TextView nameField = (TextView) findViewById(R.id.nameField2);
		nameField.setText(myUser.firstName + " " + myUser.lastName);
		
		nameField.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				myLocationManager.showUserCurrentLocation(myUser);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map_query, menu);
		return true;
	}

	public void queryFriendsWithinDistance(View view) {
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View popupView = layoutInflater.inflate(
				R.layout.popout_distance_selection, null);

		final PopupWindow popupWindow = new PopupWindow(popupView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		Button search = (Button) popupView.findViewById(R.id.confirmBtn);

		search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
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
				myLocationManager.clear(myUser);
				ArrayList<User> closeFriends = myUser
						.findFriendsWithinDistance(value);
				myLocationManager.showUserCurrentLocaiton(closeFriends);
			}
		});
		
		Button knnSearch = (Button) popupView.findViewById(R.id.knnConfirmBtn);
		knnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText valueView = (EditText) popupView
						.findViewById(R.id.kValue);
				Integer kValue = Integer.parseInt(valueView.getText().toString());
				popupWindow.dismiss();
				
				GeoPoint currentPoint = myUser.getCurrentLocationPoint();
				String strKnnQuery = String.format(httpKnnQuery, myUser.userId ,kValue, currentPoint.getLatitudeE6(), currentPoint.getLongitudeE6());

				DatabaseClient.get(strKnnQuery, null, new MyKNNHandler());
			}
		});
		
		Button btnOpenPopup = (Button) findViewById(R.id.queryUser);
		popupWindow.showAsDropDown(btnOpenPopup, 50, -30);
	}

	public void BackToMyMap(View view) {
		Intent dataIntent = new Intent();
		setResult(RESULT_CANCELED, dataIntent);
		finish();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void QueryAddress(View view) {
		EditText addressText = (EditText) findViewById(R.id.address);
		Geocoder geocoder = new Geocoder(this);
		try {
			List<Address> addresses = geocoder.getFromLocationName(addressText
					.getText().toString(), 5);
			if (addresses.size() > 0) {
				GeoPoint p = new GeoPoint(
						(int) (addresses.get(0).getLatitude() * 1E6),
						(int) (addresses.get(0).getLongitude() * 1E6));

				mapController.animateTo(p);
				mapController.setZoom(12);

				LocationItemizedOverlay locationOveraly = new LocationItemizedOverlay(drawable, this);				
				locationOveraly.addOverlay(new OverlayItem(p, null, null));
				mapOverlays.clear();
				mapOverlays.add(locationOveraly);

				mapView.invalidate();
				addressText.setText("");
			} else {
				AlertDialog.Builder adb = new AlertDialog.Builder(this);
				adb.setTitle("Google Map");
				adb.setMessage("Please Provide the Proper Place");
				adb.setPositiveButton("Close", null);
				adb.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private final class MyKNNHandler extends JsonHttpResponseHandler {
	    public void onSuccess(JSONObject locationObject) {
	    	System.out.println("get past location on success jsonobject");
	    }
	    
		public void onSuccess(JSONArray locationArray) {
			int i = 0;
			int latitudeValue,longitudeValue, pid;
			long time; 
			Drawable drawable = getResources().getDrawable(R.drawable.androidmarker);

			LocationItemizedOverlay itemizedOverlay = new LocationItemizedOverlay(drawable, getBaseContext());
			mapOverlays.clear();
			
			System.out.println("get knn on Success, there are "+ locationArray.length() + " past locations");
			mapOverlays.add(myUser.getCurrentLocation());
			
			try {
				for(i=0; i<locationArray.length(); i++) {
					
					JSONObject location = locationArray.getJSONObject(i);
					pid = location.getInt("PID");
					latitudeValue = location.getInt("LAT_INT");
					longitudeValue = location.getInt("LONG_INT");
					time = location.getLong("UPDATED_TIME");
				//	user.addLocation(latitudeValue, longitudeValue, time);	
					OverlayItem overlayitem = new OverlayItem(new GeoPoint(latitudeValue, longitudeValue), String.valueOf(pid), String.valueOf(time));   
					itemizedOverlay.addOverlay(overlayitem);
				}	
				mapOverlays.add(itemizedOverlay);
				mapView.postInvalidate();
				
			} catch (JSONException e) {
				System.out.println("jsonarray failed to get location points");
			}
		}
    }
}