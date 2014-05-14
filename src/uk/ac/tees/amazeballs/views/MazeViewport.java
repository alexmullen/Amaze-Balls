package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.MazeWorld;
import uk.ac.tees.amazeballs.maze.MazeWorld.Ball;
import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * A View implementation for displaying a MazeWorld from the view of a
 * MazeWorldCamera.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeViewport extends View {

	private MazeWorldCamera camera;
	
	// Re-usable objects for performance
	private final int[] visibleGridRange;
	private final Point worldCoords;
	
	public MazeViewport(Context context) {
		super(context);
		visibleGridRange = new int[4];
		worldCoords = new Point();
	}

	public MazeViewport(Context context, AttributeSet attrs) {
		super(context, attrs);
		visibleGridRange = new int[4];
		worldCoords = new Point();
	}
	
	public void setCamera(MazeWorldCamera camera) {
		this.camera = camera;
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
		if (camera == null) {
			return;
		}


		/*
		 *  Calculate the scale required to make best use of the screen space we have
		 *  whilst keeping the camera's aspect ratio the same.
		 */
		float scale = Math.min(
				((float)getWidth() / (float)camera.getWidth()), 
				((float)getHeight() / (float)camera.getHeight()));
		
		// Calculate the scaled offsets so we can centre align the grid.
		int scaledGridOffset_x = (int) ((getWidth() - (camera.getWidth() * scale)) / 2);
		int scaledGridOffset_y = (int) ((getHeight() - (camera.getHeight() * scale)) / 2);
      	
		// Create a clipping region to remove any tiles that cannot be seen
		canvas.clipRect(scaledGridOffset_x, scaledGridOffset_y, 
				(getWidth() - scaledGridOffset_x) , (getHeight() - scaledGridOffset_y));
		
		// Scale the canvas, and translate it the origin to the calculated non-scaled offset
		canvas.scale(scale, scale);
		canvas.translate((scaledGridOffset_x /= scale), (scaledGridOffset_y /= scale));
		
		
		
		MazeWorld world = camera.world;
		int tilesize = world.tilesize;
		
		// output array order = left, top, right, bottom
		camera.getVisibleRange(visibleGridRange);
		for (int x = visibleGridRange[0]; x <= visibleGridRange[2]; x++) {
			for (int y = visibleGridRange[1]; y <= visibleGridRange[3]; y++) {
				// Convert the tile's world coordinates into view/camera coordinates
				world.getWorldCoords(x, y, worldCoords);

				int bounds_x = (worldCoords.x - camera.getLeft());
				int bounds_y = (worldCoords.y - camera.getTop());
				
				// Draw the tile
				Drawable tileImage = TileImageFactory.getImage(world.maze.getTileAt(x, y));
				// left, top, right, bottom
				tileImage.setBounds(bounds_x, bounds_y, (bounds_x + tilesize), (bounds_y + tilesize));
				tileImage.draw(canvas);
			}
		}

		Ball ball = world.ball;
		if (ball != null) {
			int ballsize = (int) (ball.size);
			
			// Convert the ball's world coordinates into view/camera coordinates
			int bounds_x = (ball.position_x - camera.getLeft());
			int bounds_y = (ball.position_y - camera.getTop());
			
			// Draw the ball
			Drawable tileImage = TileImageFactory.getImage(TileType.Ball);
			// left, top, right, bottom
			tileImage.setBounds(bounds_x, bounds_y, (bounds_x + ballsize), (bounds_y + ballsize));
			tileImage.draw(canvas);
		}

	}
}
