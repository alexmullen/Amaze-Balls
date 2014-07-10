package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.MazeNew;
import uk.ac.tees.amazeballs.maze.MazeWorld;
import uk.ac.tees.amazeballs.maze.MazeWorld.Ball;
import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * A View implementation for displaying a MazeWorld from the view of a
 * MazeWorldCamera.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeViewport extends View {

	private static final Drawable cachedBallDrawable = TileImageFactory.getDrawable(MazeNew.BALL_TILE);
	protected MazeWorldCamera camera;
	
	protected float scale;
	protected int scaledGridOffset_left;
	protected int scaledGridOffset_top;
	protected int scaledGridOffset_right;
	protected int scaledGridOffset_bottom;
	private float unscaledGridOffset_left;
	private float unscaledGridOffset_top;
	
	// Re-usable objects for performance
	private final int[] visibleGridRange = new int[4];
	
	public MazeViewport(Context context) {
		super(context);
	}

	public MazeViewport(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	public void setCamera(MazeWorldCamera camera) {
		this.camera = camera;
		precalculatePositions();
	}
	
	
	protected void precalculatePositions() {
		/*
		 *  Calculate the scale required to make best use of the screen space we have
		 *  whilst keeping the camera's aspect ratio the same.
		 */
		scale = Math.min(
				((float)getWidth() / (float)camera.getWidth()), 
				((float)getHeight() / (float)camera.getHeight()));
		
		// Calculate the scaled offsets so we can centre align the grid.
		scaledGridOffset_left = (int) ((getWidth() - (camera.getWidth() * scale)) / 2);
		scaledGridOffset_top = (int) ((getHeight() - (camera.getHeight() * scale)) / 2);
		scaledGridOffset_right = (getWidth() - scaledGridOffset_left);
		scaledGridOffset_bottom = (getHeight() - scaledGridOffset_top);
		
		// Calculate the non-scaled top and left offset
		unscaledGridOffset_left = scaledGridOffset_left / scale;
		unscaledGridOffset_top = scaledGridOffset_top / scale;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		precalculatePositions();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/*
		 * This needs to be as streamlined as possible. No objection allocations
		 * in here preferably.
		 * 
		 */
		
		// Only need to render if we have a camera
//		if (camera == null) {
//			return;
//		}
		
		// Create a clipping region to remove any tiles that cannot be seen
		canvas.clipRect(scaledGridOffset_left, scaledGridOffset_top, scaledGridOffset_right , 
				scaledGridOffset_bottom);
		
		// Scale the canvas, and translate it the origin to the calculated non-scaled offset
		canvas.scale(scale, scale);
		canvas.translate(unscaledGridOffset_left, unscaledGridOffset_top);
		
		
		
		MazeWorld world = camera.world;
		Ball ball = world.ball;
		int tilesize = world.tilesize;
		int cameraLeft = camera.getLeft();
		int cameraTop = camera.getTop();
		
		
		int left = visibleGridRange[0];
		int top = visibleGridRange[1];
		int right = visibleGridRange[2];
		int bottom = visibleGridRange[3];
		//Log.d("vis range l,t,r,b", "" + left + "," + top + "," + right + right + "," + bottom);
		Log.d("w/h", "" + (right - left) + "," + (bottom - top));
		
		// output array order = left, top, right, bottom
		camera.getVisibleRange(visibleGridRange);
		for (int x = visibleGridRange[0]; x <= visibleGridRange[2]; x++) {
			for (int y = visibleGridRange[1]; y <= visibleGridRange[3]; y++) {
				// Convert the tile's world coordinates into view/camera coordinates
				int bounds_x = ((x * tilesize) - cameraLeft);
				int bounds_y = ((y * tilesize) - cameraTop);
				// Draw the tile
				Drawable tileImage = TileImageFactory.getDrawable(world.maze.getTileAt(x, y));
				// left, top, right, bottom
				tileImage.setBounds(bounds_x, bounds_y, (bounds_x + tilesize), (bounds_y + tilesize));
				tileImage.draw(canvas);
			}
		}
		
		if (ball != null) {
			int ballsize = (int) (ball.size);
			// Convert the ball's world coordinates into view/camera coordinates
			int bounds_x = (ball.position_x - cameraLeft);
			int bounds_y = (ball.position_y - cameraTop);
			// Draw the ball
			// left, top, right, bottom
			cachedBallDrawable.setBounds(bounds_x, bounds_y, (bounds_x + ballsize), (bounds_y + ballsize));
			cachedBallDrawable.draw(canvas);
		}

	}
}
