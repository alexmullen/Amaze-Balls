package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.TileFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import android.os.Bundle;
import android.app.Activity;

/**
 * The main activity for displaying a game.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MainGameActivity extends Activity {
	
	public int testing;
	
	private Maze currentMaze;
	private MazeGameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
		
		gameView = (MazeGameView) findViewById(R.id.main_game_view);
		
		TileFactory.registerTile(TileType.Floor, this.getResources().getDrawable(R.drawable.greenstar));
		
		currentMaze = new Maze(10, 15);
		for (int x = 0; x < currentMaze.getWidth(); x++) {
			for (int y = 0; y < currentMaze.getHeight(); y++) {
				currentMaze.setTileAt(x, y, TileFactory.createTile(TileType.Floor));
			}
		}
		
		gameView.setMaze(currentMaze);
	}

}
