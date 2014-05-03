package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.WallTile;
import uk.ac.tees.amazeballs.views.MazeBall;
import uk.ac.tees.amazeballs.views.MazeGridView;

/**
 * A controller class for managing a ball in a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeBallController {
	
	private final Maze model;
	private final MazeGridView view;
	
	public MazeBallController(Maze mazeModel, MazeGridView mazeView) {
		this.model = mazeModel;
		this.view = mazeView;
	}
	
	/**
	 * Simulates one tick in the ball simulation where gravity and collisions are
	 * applied.
	 */
	public void update() {
		
		
		// Get the accelerometer reading
		
		// Apply gravity to the ball
		
		if (!ballHasCollided()) {
			MazeBall ball = view.getBall();
			ball.position_y += 5;
		}
		

		// Resolve collisions
		
		// Potentially scroll the screen
		
		// Notify any special blocks if the ball touched them

		// Update the view
		view.invalidate();
	}
	
	public boolean ballHasCollided() {
		
		int gridPositionTouchedX;
		int gridPositionTouchedY;
		int ballSize = (int)(view.getTilesize() * view.getBall().imageRelativeSize);
		
		// Top left corner of ball
		gridPositionTouchedX = (int)Math.floor(((view.getBall().position_x) / view.getTilesize()));
		gridPositionTouchedY = (int)Math.floor(((view.getBall().position_y) / view.getTilesize()));
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// Top right corner of ball
		gridPositionTouchedX = (int)Math.floor(((view.getBall().position_x + ballSize) / view.getTilesize()));
		gridPositionTouchedY = (int)Math.floor(((view.getBall().position_y) / view.getTilesize()));
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// Bottom right corner of ball
		gridPositionTouchedX = (int)Math.floor(((view.getBall().position_x + ballSize) / view.getTilesize()));
		gridPositionTouchedY = (int)Math.floor(((view.getBall().position_y + ballSize) / view.getTilesize()));
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// Bottom left corner of ball
		gridPositionTouchedX = (int)Math.floor(((view.getBall().position_x) / view.getTilesize()));
		gridPositionTouchedY = (int)Math.floor(((view.getBall().position_y + ballSize) / view.getTilesize()));
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		return false;
	}

}
