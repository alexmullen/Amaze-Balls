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
		MazeBall ball = view.getBall();
		ball.position_y += 5;
		
		// Resolve collisions
		while (ballHasCollided()) {
			ball.position_y--;
		}
		
		// Potentially scroll the screen
		int mazeAreaWidth = view.getTilesize() * model.getWidth();
		if (ball.position_y / mazeAreaWidth >= 0.75) {
			MazeSelection mazeSelection = (MazeSelection) model;
			int amountShiftedDown = mazeSelection.shiftDown(1);
			ball.position_y -= (amountShiftedDown * view.getTilesize());
		}
		
		// Notify any special blocks if the ball touched them

		// Update the view
		view.invalidate();
	}
	
	public boolean ballHasCollided() {
		
		int gridPositionTouchedX;
		int gridPositionTouchedY;
		int ballSize = (int)(view.getTilesize() * view.getBall().imageRelativeSize);
		
		// Check top left corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// Check top right corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x + ballSize) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// Check bottom right corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x + ballSize) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y + ballSize) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// Check bottom left corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y + ballSize) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) instanceof WallTile) {
			return true;
		}
		
		// No collisions
		return false;
	}

}
