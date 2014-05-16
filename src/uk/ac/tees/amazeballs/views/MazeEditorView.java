package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.MazeSelection;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
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
public class MazeEditorView extends MazeView implements OnGestureListener, OnScaleGestureListener {

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
		if (currentMaze != null) {
			return handleTouch(event, false);
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		if (currentMaze != null) {
			handleTouch(event, true);
		}
	}
	
	private boolean handleTouch(MotionEvent event, boolean wasLongPress) {
		// Normalize the coordinates touched into grid coordinates.
		int gridPositionTouchedX = (int) Math.floor(((event.getX() - gridOffset_x) / tileSize));
		int gridPositionTouchedY = (int) Math.floor(((event.getY() - gridOffset_y) / tileSize));
		
		// Ignore any touches that are within our view area but outside the displayed grid.
		if (gridPositionTouchedX < 0 || gridPositionTouchedY < 0 || 
			gridPositionTouchedX >= currentMaze.getWidth() || gridPositionTouchedY >= currentMaze.getHeight()) {
			return true;
		}
		
		if (onTileTouchedListener != null) {
			onTileTouchedListener.onTileTouched(gridPositionTouchedX, gridPositionTouchedY, wasLongPress);
		}
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (currentMaze == null) {
			return true;
		}
		
		boolean invalidateNeeded = false;
		MazeSelection mazeSelection = (MazeSelection) currentMaze;

		if (distanceX < -5) {
			mazeSelection.shiftLeft(1);
			invalidateNeeded = true;
		}
		if (distanceX > 5) {
			mazeSelection.shiftRight(1);
			invalidateNeeded = true;
		}
		if (distanceY < -5) {
			mazeSelection.shiftUp(1);
			invalidateNeeded = true;
		}
		if (distanceY > 5) {
			mazeSelection.shiftDown(1);
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
		if (currentMaze == null) {
			return true;
		}
		
		boolean invalidateNeeded = false;
		MazeSelection mazeSelection = (MazeSelection) currentMaze;
		
		if (detector.getScaleFactor() <= 1.0f) {
			mazeSelection.expandUp(1);
			mazeSelection.expandDown(1);
			mazeSelection.expandRight(1);
			mazeSelection.expandLeft(1);
			invalidateNeeded = true;
		} 
		if (detector.getScaleFactor() >= 1.0f) {
			mazeSelection.contractUp(1);
			mazeSelection.contractDown(1);
			mazeSelection.contractRight(1);
			mazeSelection.contractLeft(1);
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

		// Don't draw a maze if there is no maze
		if (currentMaze == null) {
			return;
		}
		
		// Draw vertical grid lines
		for (int i = 0; i <= currentMaze.getWidth(); i++) {
			int current_x = (gridOffset_x + (tileSize * i));
			canvas.drawLine(
					current_x, // start x
					gridOffset_y, // start y
					current_x, // stop x
					(gridOffset_y + (currentMaze.getHeight() * tileSize)), // stop y
					LINE_PAINT); // paint
		}
		
		// Draw horizontal grid lines
		for (int i = 0; i <= currentMaze.getHeight(); i++) {
			int current_y = (gridOffset_y + (tileSize * i));
			canvas.drawLine(
					gridOffset_x, // start x
					current_y, // start y
					(gridOffset_x + (currentMaze.getWidth() * tileSize)), // stop x
					current_y, // stop y
					LINE_PAINT); // paint
		}
	}

}
