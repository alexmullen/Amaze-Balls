package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.menus.SaveLevelAsDialogFragment;
import uk.ac.tees.amazeballs.views.MazeEditorView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;

public class MazeEditorActivity extends Activity {

	private Maze currentMaze;
	private MazeSelection currentMazeSelection;
	private MazeEditorView mazeEditorView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze_editor);
		
		loadTiles();
		
		mazeEditorView = (MazeEditorView) findViewById(R.id.maze_grid_view);
		
		
		if (savedInstanceState != null) {
			currentMaze = (Maze) savedInstanceState.getSerializable("maze");
		} else {
			// Create a bordered maze of the specified width and height
			currentMaze = MazeFactory.createBorderedMaze(25, 30);
		}
		
		/*
		 * Create a maze selection to view only a small portion of the maze so
		 * that we can have mazes that are much larger than most devices'
		 * displays. The size specified here represents the grid size displayed
		 * in the MazeEditorView.
		 */
		currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
		
		// Set the maze for the MazeEditorView to display
		mazeEditorView.setMaze(currentMazeSelection);
	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("maze", currentMaze);
		super.onSaveInstanceState(outState);
	}



	private void loadTiles() {
		TileImageFactory.registerImage(TileType.Floor, this.getResources().getDrawable(R.drawable.floor));
		TileImageFactory.registerImage(TileType.Wall, this.getResources().getDrawable(R.drawable.wall));
		TileImageFactory.registerImage(TileType.Ball, this.getResources().getDrawable(R.drawable.ball));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.maze_editor, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the options menu items
		switch (item.getItemId()) {			
			case R.id.editor_file_new:
				currentMaze = MazeFactory.createBorderedMaze(25, 30);
				currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
				mazeEditorView.setMaze(currentMazeSelection);
				return true;
			case R.id.editor_file_save:
				return true;
			case R.id.editor_file_saveas:
				new SaveLevelAsDialogFragment().show(this.getFragmentManager(), "savelevel");
				return true;
			case R.id.editor_play:
				Bundle b = new Bundle();
				b.putSerializable("maze", currentMaze);
				Intent i = new Intent(this, MainGameActivity.class);
				i.putExtras(b);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}
