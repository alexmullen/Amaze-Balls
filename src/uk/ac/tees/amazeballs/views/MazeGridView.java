package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.Tile;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * A custom implementation of a View for displaying a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeGridView extends View {

	protected static int TILESIZE = 40;
	private static final int X_TILECOUNT = 10;
	private static final int Y_TILECOUNT = 15;
	private static final Paint LINE_PAINT = new Paint();
	
	protected int xGridOffset;
	protected int yGridOffset;
	
	private boolean gridLinesShown = true;
	
	protected Maze currentMaze;
	
	public MazeGridView(Context context) {
		super(context);
		LINE_PAINT.setStyle(Style.STROKE);
	}
	
	public MazeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setMaze(new Maze(X_TILECOUNT, Y_TILECOUNT));
		LINE_PAINT.setStyle(Style.STROKE);
	}
	
	public void setMaze(Maze maze) {
		currentMaze = maze;
	}
	
	public void setShowGridLines(boolean show) {
		gridLinesShown = show;
		invalidate();
	}
	
	public boolean getShowGridLines() {
		return gridLinesShown;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// Calculate the ideal square size
        int tileSizeAcross = (int) Math.floor(w / X_TILECOUNT);
        int tileSizeDown = (int) Math.floor(h / Y_TILECOUNT);
        TILESIZE = Math.min(tileSizeAcross, tileSizeDown);
        
        // Calculate the offsets so we can centre align the grid.
        xGridOffset = (w - (TILESIZE * X_TILECOUNT)) / 2;
        yGridOffset = (h - (TILESIZE * Y_TILECOUNT)) / 2;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// To prevent the Activity viewer from throwing a NPE.
		if (currentMaze == null) {
			return;
		}

		for (int x = 0; x < X_TILECOUNT; x++) {
			for (int y = 0; y < Y_TILECOUNT; y++) {
				int xTileOffset = (x * TILESIZE) + xGridOffset;
				int yTileOffset = (y * TILESIZE) + yGridOffset;
				
				
				if (currentMaze.isTileAt(x, y)) {
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
				}
			}
		}
	}
	
}
