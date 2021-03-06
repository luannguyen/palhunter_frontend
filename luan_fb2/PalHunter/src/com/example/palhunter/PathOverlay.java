package com.example.palhunter;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PathOverlay extends Overlay {

	private ArrayList<UserLocation> myPastLocations;
	private MapView mapView;
	Projection projection;
	boolean mainUser;
	static int Colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY};
	static int colorId = 0;
	int myColor;
	
	public PathOverlay(ArrayList<UserLocation> pastLocations, MapView map_view, Context context, boolean isMainUser) {
		myPastLocations = pastLocations;
		mapView = map_view;
		projection = mapView.getProjection();
		mainUser = isMainUser;
		myColor = Colors[(colorId%Colors.length)];
		colorId++;
	}

	public boolean isEmpty() {
		return myPastLocations.isEmpty();
	}

	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		super.draw(canvas, mapv, shadow);
		if(shadow == false && myPastLocations.size() > 1) {
			Path path = new Path();
			
			Paint mPaint = new Paint();
			mPaint.setDither(true);
			
			if(mainUser)
				mPaint.setColor(Color.DKGRAY);
			else {
				mPaint.setColor(myColor);
			}
			
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			mPaint.setStrokeJoin(Paint.Join.ROUND);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStrokeWidth(4);
			
			Point start = new Point();
			projection.toPixels(myPastLocations.get(0).getLocationPoint(), start);
			
			for(int i=1; i<myPastLocations.size(); i++) {
				GeoPoint point = myPastLocations.get(i).getLocationPoint();
				
				Point end = new Point();
				
				projection.toPixels(point, end);
				path.moveTo(end.x, end.y);
				path.lineTo(start.x, start.y);
				canvas.drawPath(path, mPaint);
				start = end;
			}
		}
	}
}
