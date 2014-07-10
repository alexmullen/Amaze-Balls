package uk.ac.tees.amazeballs.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;


/**
 * A custom implementation of a View for displaying a maze with grid lines and
 * the ability for users to interact with to design a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeEditorView extends MazeViewport implements OnGestureListener, OnScaleGestureListener {

	private static final Paint LINE_PAINT;
	static {
		LINE_PAINT = new Paint();
		LINE_PAINT.setStyle(Style.STROKE);
	}

	public interface OnTileTouchedListener {
		public void onTileTouched(int x, int y, boolean wasLongPress);
	}
	
	private OnTileTouchedListener onTileTouchedListener;
	private final GestureDetector gestureDetector;
	private final ScaleGestureDetector scaleGestureDetector;
	
	
	public MazeEditorView(Context context, AttributeSet attrs) {
		// Constructor used when this view is inflated from XML.
		super(context, attrs);
		gestureDetector = new GestureDetector(context, this);
		scaleGestureDetector = new ScaleGestureDetector(context, this);
	}
	
	public void setOnTileTouchedListener(OnTileTouchedListener listener) {
		onTileTouchedListener = listener;
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// This event needs to be handled by the GestureDetector.
		gestureDetector.onTouchEvent(event);
		scaleGestureDetector.onTouchEvent(event);
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent event) {
		// This needs to return true for the GestureDetector to detect gestures.
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}
	
	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		if (camera != null) {
			return handleTouch(event, false);
		} else {
			return true;
		}
	}

	@Override
	public void onLongPress(MotionEvent event) {
		if (camera != null) {
			handleTouch(event, true);
		}
	}
	
	private boolean handleTouch(MotionEvent event, boolean wasLongPress) {
		if (onTileTouchedListener != null) {
			int x_touched = (int) event.getX();
			int y_touched = (int) event.getY();
			
			// Ignore any touches that are within our view area but outside the displayed grid section.
			if (x_touched < scaledGridOffset_left || x_touched > scaledGridOffset_right ||
				y_touched < scaledGridOffset_top || y_touched > scaledGridOffset_bottom) {
				return true;
			}
			
			/*
			 * Retrieve the camera view coordinates touched. This is the location on the screen
			 * touched after we've adjusted for the scale it was being displayed at.
			 */
			int cameraCoordinateTouchedX = 
					(int) Math.floor(((x_touched - scaledGridOffset_left) / scale));
			int cameraCoordinateTouchedY = 
					(int) Math.floor(((y_touched - scaledGridOffset_top) / scale));

			/*
			 *  Convert the camera view coordinates into world coordinates. This is the location
			 *  in the 2D world that was touched.
			 */
			int worldCoordinateTouchedX = (camera.getLeft() + cameraCoordinateTouchedX);
			int worldCoordinateTouchedY = (camera.getTop() + cameraCoordinateTouchedY);
			
			/*
			 * Convert the world coordinates into tile grid coordinates.
			 */
			int tilesize = camera.world.tilesize;
			int worldGridTileX = (worldCoordinateTouchedX / tilesize);
			int worldGridTileY = (worldCoordinateTouchedY / tilesize);

			// !!!!!!!!!!!!!!!
			
			// Ignore any touches that are within our view area but outside the displayed grid.
//			if (worldGridTileX < 0 || worldGridTileY < 0 || 
//				worldGridTileX >= camera.world.maze.width || worldGridTileY >= camera.world.maze.height) {
//				return true;
//			}
			
			// Notify the listener
			onTileTouchedListener.onTileTouched(worldGridTileX, worldGridTileY, wasLongPress);
		}

		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (camera == null) {
			return true;
		}
		
		boolean invalidateNeeded = false;

		if (distanceX < 0) {
			camera.moveLeft((int) -distanceX);
			invalidateNeeded = true;
		}
		if (distanceX > 0) {
			camera.moveRight((int) distanceX);
			invalidateNeeded = true;
		}
		if (distanceY < 0) {
			camera.moveUp((int) -distanceY);
			invalidateNeeded = true;
		}
		if (distanceY > 0) {
			camera.moveDown((int) distanceY);
			invalidateNeeded = true;
		}
		
		
		if (invalidateNeeded) {
			invalidate();
		}
		
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		// For providing some form of feedback to the user.
	}
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		if (camera == null) {
			return true;
		}
		
		boolean invalidateNeeded = false;
		
		if (detector.getScaleFactor() <= 1.0f) {
			precalculatePositions();
			camera.zoomOut(5);
			invalidateNeeded = true;
		} 
		if (detector.getScaleFactor() >= 1.0f) {
			precalculatePositions();
			camera.zoomIn(5);
			invalidateNeeded = true;
		}
		
		if (invalidateNeeded) {
			invalidate();
		}

		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

//		// Don't draw a maze if there is no maze
//		if (currentMaze == null) {
//			return;
//		}
//		
//		// Draw vertical grid lines
//		for (int i = 0; i <= currentMaze.getWidth(); i++) {
//			int current_x = (gridOffset_x + (tileSize * i));
//			canvas.drawLine(
//					current_x, // start x
//					gridOffset_y, // start y
//					current_x, // stop x
//					(gridOffset_y + (currentMaze.getHeight() * tileSize)), // stop y
//					LINE_PAINT); // paint
//		}
//		
//		// Draw horizontal grid lines
//		for (int i = 0; i <= currentMaze.getHeight(); i++) {
//			int current_y = (gridOffset_y + (tileSize * i));
//			canvas.drawLine(
//					gridOffset_x, // start x
//					current_y, // start y
//					(gridOffset_x + (currentMaze.getWidth() * tileSize)), // stop x
//					current_y, // stop y
//					LINE_PAINT); // paint
//		}
	}

}
