package uk.ac.tees.amazeballs.views;

import java.util.List;

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
	
	public MazeViewport(Context context) {
		super(context);
	}

	public MazeViewport(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	public MazeWorldCamera getCamera() {
		return camera;
	}
	
	public void setCamera(MazeWorldCamera camera) {
		this.camera = camera;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
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
		int scaledTileSize = (int) (camera.getWorld().getTilesize() * scale);
      	
		// Create a clipping region to remove any tiles that cannot be seen
		canvas.clipRect(gridOffset_x, gridOffset_y, (getWidth() - gridOffset_x) , (getHeight() - gridOffset_y));

		List<Point> visibleTiles = camera.getVisibleTiles();
		for (Point gridPosition : visibleTiles) {
			
			// Get the type of tile it is
			TileType tileType = 
					camera.getWorld().getMaze().getTileAt(gridPosition.x, gridPosition.y);
			
			// Get its world coordinates
			Point worldPosition = camera.getWorld().getWorldCoords(gridPosition.x, gridPosition.y);
			
			// Convert the tile's world coordinates into view/camera coordinates
			int viewPositionX = (worldPosition.x - camera.getLeft());
			int viewPositionY = (worldPosition.y - camera.getTop());
			
			// Scale the positions for the display
			int scaledViewPositionX = (int) (viewPositionX * scale);
			int scaledViewPositionY = (int) (viewPositionY * scale);
			
			// Draw the tile
			Drawable tileImage = TileImageFactory.getImage(tileType);
			tileImage.setBounds(
					gridOffset_x + scaledViewPositionX, // left
					gridOffset_y + scaledViewPositionY, // top
					gridOffset_x + scaledViewPositionX + scaledTileSize, // right
					gridOffset_y + scaledViewPositionY + scaledTileSize); // bottom

			tileImage.draw(canvas);
		}
		
		int scaledBallSize = (int) (camera.getWorld().getBall().size * scale);
		
		// Convert the tile's world coordinates into view/camera coordinates
		int viewPositionX = (camera.getWorld().getBall().position_x - camera.getLeft());
		int viewPositionY = (camera.getWorld().getBall().position_y - camera.getTop());
		
		// Scale the positions for the display
		int scaledViewPositionX = (int) (viewPositionX * scale);
		int scaledViewPositionY = (int) (viewPositionY * scale);
		
		
		// Draw the ball
		Drawable tileImage = TileImageFactory.getImage(TileType.Ball);
		tileImage.setBounds(
				gridOffset_x + scaledViewPositionX, // left
				gridOffset_y + scaledViewPositionY, // top
				gridOffset_x + scaledViewPositionX + scaledBallSize, // right
				gridOffset_y + scaledViewPositionY + scaledBallSize); // bottom

		tileImage.draw(canvas);
		
	}
}
