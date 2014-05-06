package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.views.MazeBallView;
import uk.ac.tees.amazeballs.views.MazeBallView.Ball;

/**
 * A controller class for managing a ball in a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class GameController {
	
	private final static int GRID_SCROLLING_AMOUNT = 1;
	private final static double TILT_SENSITIVITY = 0.75;
	
	public volatile float lastAccelerometerReading_x;
	public volatile float lastAccelerometerReading_y;
	
	private final Maze model;
	private final MazeBallView view;
	

	public GameController(Maze mazeModel, MazeBallView mazeView) {
		this.model = mazeModel;
		this.view = mazeView;
	}
	
	/**
	 * Simulates one tick in the ball simulation where gravity and collisions are
	 * applied.
	 */
	public void update() {
		
		Ball ball = view.getBall();
		MazeSelection mazeSelection = (MazeSelection) model;
		
		// Get the accelerometer reading
		if (lastAccelerometerReading_y > TILT_SENSITIVITY) {
			ball.position_y += 5;		
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_y--;
			}
		} 
		if (lastAccelerometerReading_y < -TILT_SENSITIVITY) {
			ball.position_y -= 5;		
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_y++;
			}
		} 
		if (lastAccelerometerReading_x > TILT_SENSITIVITY) {
			ball.position_x -= 5;
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_x++;
			}
		}
		if (lastAccelerometerReading_x < -TILT_SENSITIVITY) {
			ball.position_x += 5;
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_x--;
			}
		}
		


		// Potentially scroll the screen down
		int mazeAreaHeight = view.getTilesize() * model.getHeight();
		if ((double)ball.position_y / mazeAreaHeight >= 0.75) {
			int amountShiftedDown = mazeSelection.shiftDown(GRID_SCROLLING_AMOUNT);
			ball.position_y -= (amountShiftedDown * view.getTilesize());
		}
		
		// Potentially scroll the screen up
		if ((double)ball.position_y / mazeAreaHeight <= 0.25) {
			int amountShiftedUp = mazeSelection.shiftUp(GRID_SCROLLING_AMOUNT);
			ball.position_y += (amountShiftedUp * view.getTilesize());
		}
		
		// Potentially scroll the screen right
		int mazeAreaWidth = view.getTilesize() * model.getWidth();
		if ((double)ball.position_x / mazeAreaWidth >= 0.75) {
			int amountShiftedRight = mazeSelection.shiftRight(GRID_SCROLLING_AMOUNT);
			ball.position_x -= (amountShiftedRight * view.getTilesize());
		}
		
		// Potentially scroll the screen left
		if ((double)ball.position_x / mazeAreaWidth <= 0.25) {
			int amountShiftedLeft = mazeSelection.shiftLeft(GRID_SCROLLING_AMOUNT);
			ball.position_x += (amountShiftedLeft * view.getTilesize());
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
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// Check top right corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x + ballSize) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// Check bottom right corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x + ballSize) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y + ballSize) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// Check bottom left corner of ball
		gridPositionTouchedX = (int)(view.getBall().position_x) / view.getTilesize();
		gridPositionTouchedY = (int)(view.getBall().position_y + ballSize) / view.getTilesize();
		if (model.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// No collisions
		return false;
	}

}
