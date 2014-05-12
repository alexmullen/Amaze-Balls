package uk.ac.tees.amazeballs.views;

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
	
//	
//	public MazeWorldCamera getCamera() {
//		return camera;
//	}
	
	public void setCamera(MazeWorldCamera camera) {
		this.camera = camera;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
//long startTime = System.currentTimeMillis();
		
		if (camera == null) {
			return;
		}

		/*
		 *  Calculate the scale required to make best use of the screen space we have
		 *  whilst keeping the camera's aspect ratio the same.
		 */
		float scale = Math.min((getWidth() / camera.getWidth()), (getHeight() / camera.getHeight()));

		// Calculate the offsets so we can centre align the grid.
		int gridOffset_x = (int) ((getWidth() - (camera.getWidth() * scale)) / 2);
		int gridOffset_y = (int) ((getHeight() - (camera.getHeight() * scale)) / 2);
		int scaledTileSize = (int) (camera.world.tilesize * scale);
      	
		// Create a clipping region to remove any tiles that cannot be seen
		canvas.clipRect(gridOffset_x, gridOffset_y, (getWidth() - gridOffset_x) , (getHeight() - gridOffset_y));


		// output array order = left, top, right, bottom
		camera.getVisibleRange(visibleGridRange);
		for (int x = visibleGridRange[0]; x <= visibleGridRange[2]; x++) {
			for (int y = visibleGridRange[1]; y <= visibleGridRange[3]; y++) {
				camera.world.getWorldCoords(x, y, worldCoords);
			
				// Scale the positions for the display
				int scaledViewPositionX = (int) ((worldCoords.x - camera.getLeft()) * scale);
				int scaledViewPositionY = (int) ((worldCoords.y - camera.getTop()) * scale);
				
				int bounds_x = (gridOffset_x + scaledViewPositionX);
				int bounds_y = (gridOffset_y + scaledViewPositionY);
				
				// Draw the tile
				Drawable tileImage = TileImageFactory.getImage(
						camera.world.maze.getTileAt(x, y));
				
				tileImage.setBounds(
						bounds_x, // left
						bounds_y, // top
						bounds_x + scaledTileSize, // right
						bounds_y + scaledTileSize); // bottom
	
				tileImage.draw(canvas);
			}
		}

		if (camera.world.ball != null) {
			Ball ball = camera.world.ball;
			int scaledBallSize = (int) (ball.size * scale);
			
			// Convert the tile's world coordinates into view/camera coordinates

			// Scale the positions for the display
			int scaledViewPositionX = (int) ((ball.position_x - camera.getLeft()) * scale);
			int scaledViewPositionY = (int) ((ball.position_y - camera.getTop()) * scale);
			
			
			int bounds_x = (gridOffset_x + scaledViewPositionX);
			int bounds_y = (gridOffset_y + scaledViewPositionY);
			
			// Draw the ball
			Drawable tileImage = TileImageFactory.getImage(TileType.Ball);
			tileImage.setBounds(
					bounds_x, // left
					bounds_y, // top
					bounds_x + scaledBallSize, // right
					bounds_y + scaledBallSize); // bottom
	
			tileImage.draw(canvas);
		}
		
//long elapsedTime = System.currentTimeMillis() - startTime;
//Log.d(getClass().getName(), String.valueOf(elapsedTime));

	}
}
