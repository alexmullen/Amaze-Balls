package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
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
		
		// Resolve collisions
		
		// Potentially scroll the screen
		
		// Notify any special blocks if the ball touched them
		
		// Update the view
		
	}

}
