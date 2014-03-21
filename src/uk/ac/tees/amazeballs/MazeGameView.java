package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


/**
 * A custom implementation of a View for displaying a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeGameView extends View {

	private static int TILESIZE = 32;
	private static final int X_TILECOUNT = 10;
	private static final int Y_TILECOUNT = 10;
	
	private int xGridOffset;
	private int yGridOffset;
	
	private Maze currentMaze;
	
	public MazeGameView(Context context) {
		super(context);
	}
	
	public MazeGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setMaze(Maze maze) {
		currentMaze = maze;
	}
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		

		xGridOffset = ((w / (TILESIZE * X_TILECOUNT)) / 2);
		yGridOffset = ((h / (TILESIZE * Y_TILECOUNT)) / 2);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		
	}
	
}
