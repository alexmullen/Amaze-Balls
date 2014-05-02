package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.Tile;
import uk.ac.tees.amazeballs.maze.TileFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;


/**
 * A custom implementation of a View for displaying a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeGridView extends View {

	protected static int TILESIZE = 40;
	private static final Paint LINE_PAINT = new Paint();
	
	protected int xGridOffset;
	protected int yGridOffset;
	
	private int ballOffset_x;
	private int ballOffset_y;
	
	private boolean gridLinesShown = false;
	private boolean showBall = false;
	
	protected Maze currentMaze;
	
	public MazeGridView(Context context) {
		super(context);
		LINE_PAINT.setStyle(Style.STROKE);
	}
	
	public MazeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setMaze(new Maze(10, 15));		// For the Activity viewer to display something
		LINE_PAINT.setStyle(Style.STROKE);
	}
	
	public void setMaze(Maze maze) {
		currentMaze = maze;
		onSizeChanged(this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        invalidate();
	}
	
	public void setShowGridLines(boolean show) {
		gridLinesShown = show;
		invalidate();
	}
	
	public boolean getShowGridLines() {
		return gridLinesShown;
	}
	
	public void setShowBall(boolean show) {
		showBall = show;
	}
	
	public boolean getShowBall() {
		return showBall;
	}
	
	public void setBallPosition(Point p) {
		ballOffset_x = p.x;
		ballOffset_y = p.y;
	}
	
	public Point getBallPosition() {
		return new Point(ballOffset_x, ballOffset_y);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// Calculate the ideal square size to use for the current view dimensions
        int tileSizeAcross = (int) Math.floor(w / currentMaze.getWidth());
        int tileSizeDown = (int) Math.floor(h / currentMaze.getHeight());
        TILESIZE = Math.min(tileSizeAcross, tileSizeDown);
        
        // Calculate the offsets so we can centre align the grid.
        xGridOffset = (w - (TILESIZE * currentMaze.getWidth())) / 2;
        yGridOffset = (h - (TILESIZE * currentMaze.getHeight())) / 2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// To prevent the Activity viewer from throwing a NPE.
		if (currentMaze == null) {
			return;
		}

		for (int x = 0; x < currentMaze.getWidth(); x++) {
			for (int y = 0; y < currentMaze.getHeight(); y++) {
				int xTileOffset = (x * TILESIZE) + xGridOffset;
				int yTileOffset = (y * TILESIZE) + yGridOffset;

				// Draw the current tile.
				Tile tile = currentMaze.getTileAt(x, y);
				if (tile != null) {
					Drawable tileImage = tile.getImage();
					tileImage.setBounds(
							xTileOffset, // left
							yTileOffset, // top
							xTileOffset + TILESIZE,  // right
							yTileOffset + TILESIZE); // bottom

					tileImage.draw(canvas);
				}

				// Draw the grid lines separating each tile if turned on.
				if (gridLinesShown) {
					canvas.drawRect(
							xTileOffset, // left			
							yTileOffset, // top
							xTileOffset + TILESIZE, // right
							yTileOffset + TILESIZE, // bottom
							LINE_PAINT);
				}
				
				// Draw the ball if it needs to be shown.
				if (showBall) {
					Drawable ballImage = TileFactory.createTile(TileType.Ball).getImage();
					ballImage.setBounds(
							xGridOffset + ballOffset_x, // left
							yGridOffset + ballOffset_y, // top
							xGridOffset + ballOffset_x + ballImage.getBounds().width(),  // right
							yGridOffset + ballOffset_y + ballImage.getBounds().height()); // bottom

					ballImage.draw(canvas);
				}
			}
		}
	}
	
}
