package com.example.palhunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapQueryActivity extends MapActivity {

	User myUser;
	MapView mapView;
	List<Overlay> mapOverlays;
	MapController mapController;
	HashMap<Integer, User> userList;
	MyMapLocationManager myLocationManager;
	Drawable drawable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_map_query);
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);

		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		myUser = b.getParcelable(User.USER_TYPE);
		myUser.addAllLocations(drawable, this, mapView);

		mapView = (MapView) findViewById(R.id.mapview2);
		mapView.setBuiltInZoomControls(true);

		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
		myLocationManager = new MyMapLocationManager(mapOverlays, mapView);

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

				LocationItemizedOverlay locationOveraly = new LocationItemizedOverlay(
						drawable, this);
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
}