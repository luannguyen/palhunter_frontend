package com.example.palhunter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class LocationItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> myOverlays = new ArrayList<OverlayItem>();
	Context myContext;
	public LocationItemizedOverlay(Drawable arg0, Context context) {
		// TODO Auto-generated constructor stub
		super(boundCenterBottom(arg0));
		myContext = context;
	}

	public List<OverlayItem> getOverlays()
	{
		return myOverlays;
	}
	
	public void addOverlay(OverlayItem overlay) {
	    myOverlays.add(overlay);
	    populate();
	}
	
	public OverlayItem getOverlay(int i) {
		return myOverlays.get(i);
	}
	
	public void removeOverlay(OverlayItem overlay) {
		myOverlays.remove(overlay);
		populate();
	}
	
	public void clear() {
		myOverlays.clear();
	}
	
	public boolean isEmpty() {
		return myOverlays.isEmpty();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return myOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return myOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = myOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
}