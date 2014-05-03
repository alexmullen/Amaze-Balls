package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.Tile;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
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

	private static final Paint LINE_PAINT;
	
	static {
		LINE_PAINT = new Paint();
		LINE_PAINT.setStyle(Style.STROKE);
	}
	
	
	private int tileSize;
	protected int gridOffset_x;
	protected int gridOffset_y;
	
	private Maze currentMaze;
	private MazeBall ball;
	
	private boolean displayGridLines = false;
	private boolean displayBall = false;
	
	
	public MazeGridView(Context context) {
		// Constructor used when this view is created via code.
		super(context);
	}
	
	public MazeGridView(Context context, AttributeSet attrs) {
		// Constructor used when this view is inflated from XML.
		super(context, attrs);
		//this.setMaze(new Maze(10, 15));		// For the Activity viewer to display something and not throw an NPE
	}
	
	
	
	
	public void setMaze(Maze maze) {
		currentMaze = maze;
		//onSizeChanged(this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        invalidate();
	}
	
	public Maze getMaze() {
		return currentMaze;
	}
	
	public void setGridLinesDisplayed(boolean show) {
		displayGridLines = show;
		invalidate();
	}
	
	public boolean getGridLinesDisplayed() {
		return displayGridLines;
	}
	
	public void setBallDisplayed(boolean show) {
		displayBall = show;
		invalidate();
	}
	
	public boolean getBallDisplayed() {
		return displayBall;
	}
	
	public void setBall(MazeBall ball) {
		this.ball = ball;
	}
	
	public MazeBall getBall() {
		return ball;
	}
	
	public int getTilesize() {
		return tileSize;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// Calculate the ideal square size to use for the current view dimensions
        int tileSizeAcross = (int) Math.floor(w / currentMaze.getWidth());
        int tileSizeDown = (int) Math.floor(h / currentMaze.getHeight());
        tileSize = Math.min(tileSizeAcross, tileSizeDown);
        
        // Calculate the offsets so we can centre align the grid.
        gridOffset_x = (w - (tileSize * currentMaze.getWidth())) / 2;
        gridOffset_y = (h - (tileSize * currentMaze.getHeight())) / 2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		for (int x = 0; x < currentMaze.getWidth(); x++) {
			for (int y = 0; y < currentMaze.getHeight(); y++) {
				int xTileOffset = (x * tileSize) + gridOffset_x;
				int yTileOffset = (y * tileSize) + gridOffset_y;

				// Draw the current tile.
				Tile tile = currentMaze.getTileAt(x, y);
				if (tile != null) {
					Drawable tileImage = tile.getImage();
					tileImage.setBounds(
							xTileOffset, // left
							yTileOffset, // top
							xTileOffset + tileSize,  // right
							yTileOffset + tileSize); // bottom

					tileImage.draw(canvas);
				}

				// Draw the grid lines separating each tile if turned on.
				if (displayGridLines) {
					canvas.drawRect(
							xTileOffset, // left			
							yTileOffset, // top
							xTileOffset + tileSize, // right
							yTileOffset + tileSize, // bottom
							LINE_PAINT);
				}
				
				// Draw the ball if it needs to be shown.
				if (displayBall) {
					Drawable ballImage = ball.image.getImage();
					ballImage.setBounds(
							gridOffset_x + ball.position_x, // left
							gridOffset_y + ball.position_y, // top
							gridOffset_x + ball.position_x + (int)(tileSize * ball.imageRelativeSize),  // right
							gridOffset_y + ball.position_y + (int)(tileSize * ball.imageRelativeSize)); // bottom

					ballImage.draw(canvas);
				}
			}
		}
	}
	
}
