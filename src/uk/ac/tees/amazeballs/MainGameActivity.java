package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.views.MazeGridView;
import android.os.Bundle;
import android.app.Activity;

/**
 * The main activity for displaying a game.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MainGameActivity extends Activity {

	private Maze currentMaze;
	private MazeSelection mazeSelection;
	private MazeGridView gameView;
	private MazeBallController ballController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
		
		loadTiles();
		
		gameView = (MazeGridView) findViewById(R.id.main_game_view);

		// Create a bordered maze of the specified width and height
		currentMaze = MazeFactory.createBorderedMaze(25, 30);
		
		/*
		 * Create a maze selection to view only a small portion of the maze so
		 * that we can have mazes that are much larger than most devices'
		 * displays. The size specified here represents the grid size displayed
		 * in the MazeEditorView.
		 */
		mazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
		
		// Set the maze for the MazeEditorView to display
		gameView.setMaze(mazeSelection);

		// Create a MazeBallController for handling the moving, collisions and physics for the ball
		ballController = new MazeBallController(mazeSelection, gameView);
	}
	
	private void loadTiles() {
		TileFactory.registerTile(TileType.Floor, this.getResources().getDrawable(R.drawable.floor));
		TileFactory.registerTile(TileType.Wall, this.getResources().getDrawable(R.drawable.wall));
		TileFactory.registerTile(TileType.Ball, this.getResources().getDrawable(R.drawable.ball));
	}

}
