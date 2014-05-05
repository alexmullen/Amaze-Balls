package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileType;
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

public class MazeEditorView extends MazeView implements OnGestureListener, OnScaleGestureListener {

	private static final Paint LINE_PAINT;
	
	static {
		LINE_PAINT = new Paint();
		LINE_PAINT.setStyle(Style.STROKE);
	}
	
	private final GestureDetector gestureDetector;
	private final ScaleGestureDetector scaleGestureDetector;
	
	public MazeEditorView(Context context) {
		super(context);
		gestureDetector = new GestureDetector(context, this);
		scaleGestureDetector = new ScaleGestureDetector(context, this);
	}
	
	public MazeEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureDetector = new GestureDetector(context, this);
		scaleGestureDetector = new ScaleGestureDetector(context, this);
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
	public void onLongPress(MotionEvent event) {
		Log.d(getClass().getName(), "long press");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		boolean invalidateNeeded = false;
		MazeSelection mazeSelection = (MazeSelection) getMaze();

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
	public boolean onSingleTapUp(MotionEvent event) {
		// Normalize the coordinates touched into grid coordinates.
		int gridPositionTouchedX = (int)Math.floor(((event.getX() - gridOffset_x) / getTilesize()));
		int gridPositionTouchedY = (int)Math.floor(((event.getY() - gridOffset_y) / getTilesize()));
		
		// Ignore any touches that are within our view area but outside the displayed grid.
		if (gridPositionTouchedX < 0 || 
			gridPositionTouchedY < 0 || 
			gridPositionTouchedX >= getMaze().getWidth() ||
			gridPositionTouchedY >= getMaze().getHeight()) {
			return true;
		}
		
		handleTileTouched(gridPositionTouchedX, gridPositionTouchedY);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		// For providing some form of feedback to the user.
	}
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		boolean invalidateNeeded = false;
		MazeSelection mazeSelection = (MazeSelection) getMaze();
		
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
	

	private void handleTileTouched(int x, int y) {
		// Prevent the edges of the maze being modified
		if (getMaze().isTileAtAnEdge(x, y)) {
			return;
		}
		
		// Toggle the tile.
		if (getMaze().getTileAt(x, y) == TileType.Floor) {
			getMaze().setTileAt(x, y, TileType.Wall);
		} else {
			getMaze().setTileAt(x, y, TileType.Floor);
		}
		
		// Repaint the view
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	
		int tilesize = getTilesize();

		// Draw vertical grid lines
		for (int i = 0; i <= getMaze().getWidth(); i++) {
			int current_x = (gridOffset_x + (tilesize * i));
			canvas.drawLine(
					current_x, // start x
					gridOffset_y, // start y
					current_x, // stop x
					(gridOffset_y + (getMaze().getHeight() * tilesize)), // stop y
					LINE_PAINT); // paint
		}
		
		// Draw horizontal grid lines
		for (int i = 0; i <= getMaze().getHeight(); i++) {
			int current_y = (gridOffset_y + (tilesize * i));
			canvas.drawLine(
					gridOffset_x, // start x
					current_y, // start y
					(gridOffset_x + (getMaze().getWidth() * tilesize)), // stop x
					current_y, // stop y
					LINE_PAINT); // paint
		}
	}

}
