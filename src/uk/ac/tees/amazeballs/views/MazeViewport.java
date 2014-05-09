package uk.ac.tees.amazeballs.views;

import java.util.List;

import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeWorld;
import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MazeViewport extends View {

	private MazeWorldCamera camera;
	
	public MazeViewport(Context context) {
		super(context);
	}

	public MazeViewport(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		loadTiles();
		
		Maze maze = MazeFactory.createBorderedMaze(10, 15);
		MazeWorld mazeWorld = new MazeWorld(maze, 10);
		
		
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
		
		camera = new MazeWorldCamera(mazeWorld, 0, 0, 100, 150);
		
	}

	public MazeViewport(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
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
		
		float xScale = getWidth() / (camera.getRight() - camera.getLeft());
		float yScale = getHeight() / (camera.getBottom() - camera.getTop());
		float scale = Math.min(xScale, yScale);
		canvas.scale(scale, scale);
		
		List<Point> visibleTiles = camera.getVisibleTiles();
		for (Point gridPosition : visibleTiles) {
			
			TileType tileType = 
					camera.getWorld().getMaze().getTileAt(gridPosition.x, gridPosition.y);
			
			Point worldPosition = camera.getWorld().getWorldCoords(gridPosition.x, gridPosition.y);
			
			Drawable tileImage = TileImageFactory.getImage(tileType);
			tileImage.setBounds(
					worldPosition.x - camera.getLeft(), // left
					worldPosition.y - camera.getTop(), // top
					worldPosition.x - camera.getLeft() + camera.getWorld().getTilesize(), // right
					worldPosition.y - camera.getTop() + camera.getWorld().getTilesize()); // bottom

			tileImage.draw(canvas);
		}
	}
	
	private void loadTiles() {
		TileImageFactory.registerImage(TileType.Floor, this.getResources().getDrawable(R.drawable.floor));
		TileImageFactory.registerImage(TileType.Wall, this.getResources().getDrawable(R.drawable.wall));
		TileImageFactory.registerImage(TileType.Ball, this.getResources().getDrawable(R.drawable.ball));
		
		TileImageFactory.registerImage(TileType.Chest, this.getResources().getDrawable(R.drawable.chest));
		TileImageFactory.registerImage(TileType.Door, this.getResources().getDrawable(R.drawable.door));
		TileImageFactory.registerImage(TileType.Goal, this.getResources().getDrawable(R.drawable.goal));
		TileImageFactory.registerImage(TileType.Ice, this.getResources().getDrawable(R.drawable.ice));
		TileImageFactory.registerImage(TileType.Key, this.getResources().getDrawable(R.drawable.key));
		TileImageFactory.registerImage(TileType.Penalty, this.getResources().getDrawable(R.drawable.penalty));
		TileImageFactory.registerImage(TileType.Rain, this.getResources().getDrawable(R.drawable.rain));
		TileImageFactory.registerImage(TileType.Start, this.getResources().getDrawable(R.drawable.start));
	}
	
}
