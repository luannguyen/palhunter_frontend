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

	private GeoPoint start;
	private MapView mapView;
	Projection projection;
	Path path;

	public PathOverlay(GeoPoint start_, MapView map_view) {
		start = start_;
		mapView = map_view;
		
		//add path start point
		path = new Path();
		Point p1 = new Point();
		projection.toPixels(start, p1);
		path.moveTo(p1.x, p1.y);
		
		projection = mapView.getProjection();

	}

	public void addPath(GeoPoint from, GeoPoint to) {
		Point p1 = new Point();
		Point p2 = new Point();

		Path newPath = new Path();
		projection.toPixels(from, p1);
		projection.toPixels(to, p2);
		
		newPath.moveTo(p2.x, p2.y);
		newPath.lineTo(p1.x, p1.y);
		
		path.addPath(newPath);
	}
	
	public void addPoint(GeoPoint to) {
		Point p1 = new Point();
		projection.toPixels(to, p1);
		path.lineTo(p1.x, p1.y);
		path.setLastPoint(p1.x, p1.y);
	}
	
	public void close() {
		path.close();
	}

	public void draw(Canvas canvas, MapView mapv, boolean shadow) {
		super.draw(canvas, mapv, shadow);

		Paint mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(2);
		canvas.drawPath(path, mPaint);
	}
}
