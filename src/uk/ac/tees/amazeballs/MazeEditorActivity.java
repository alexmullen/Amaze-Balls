package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.dialogs.NewLevelDialogFragment;
import uk.ac.tees.amazeballs.dialogs.SaveLevelDialogFragment;
import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.views.MazeEditorView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;


public class MazeEditorActivity extends Activity 
		implements 	SaveLevelDialogFragment.OnLevelSaveRequestListener, 
					NewLevelDialogFragment.OnNewLevelRequestListener {

	private Maze currentMaze;
	private MazeSelection currentMazeSelection;
	private MazeEditorView mazeEditorView;
	
	private String currentLevelName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze_editor);

		mazeEditorView = (MazeEditorView) findViewById(R.id.maze_grid_view);
	
		// Check whether we need to restore our state
		if (savedInstanceState != null) {
			// Restore our state
			currentMaze = (Maze) savedInstanceState.getSerializable("maze");
			// Check there was a maze to load from the previous state
			if (currentMaze != null) {
				currentLevelName = savedInstanceState.getString("level_name");
				/*
				 * Create a maze selection to view only a small portion of the maze so
				 * that we can have mazes that are much larger than most devices'
				 * displays. The size specified here represents the grid size displayed
				 * in the MazeEditorView.
				 */
				currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
			} else {
				currentMaze = MazeFactory.createBorderedMaze(25, 30);
				currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
			}
		} else {
			currentMaze = MazeFactory.createBorderedMaze(25, 30);
			currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
		}
		
		// Set the maze for the MazeEditorView to display
		mazeEditorView.setMaze(currentMazeSelection);
		mazeEditorView.invalidate();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("level_name", currentLevelName);
		outState.putSerializable("maze", currentMaze);
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
				handleNewMenuOption();			
				return true;
			case R.id.editor_file_open:
				handleOpenMenuOption();
				return true;
			case R.id.editor_file_save:
				if (currentMaze != null) {
					// Check it already has a name that it has been saved by
					if (currentLevelName != null) {
						// Overwrite
						saveCurrentLevel(currentLevelName);
					} else {
						handleSaveAsMenuOption();
					}
				}
				return true;
			case R.id.editor_file_saveas:
				if (currentMaze != null) {
					handleSaveAsMenuOption();
				}
				return true;
			case R.id.editor_play:
				handlePlayMenuOption();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void handleNewMenuOption() {
		new NewLevelDialogFragment().show(getFragmentManager(), "savelevel_dialogfragment");
	}
	
	@Override
	public void onNewLevelRequested(int width, int height) {
		currentLevelName = null;
		currentMaze = MazeFactory.createBorderedMaze(width, height);
		currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
		mazeEditorView.setMaze(currentMazeSelection);
		mazeEditorView.invalidate();
	}
	
	private void handleOpenMenuOption() {
		final String[] levels = LevelManager.getCustomLevels(this);
		if (levels.length == 0) {
			Toast.makeText(this, "There are no custom levels to open", Toast.LENGTH_LONG).show();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Choose a level to edit");
			builder.setItems(levels, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					currentLevelName = levels[which];
					currentMaze = LevelManager.loadCustomLevel(MazeEditorActivity.this, levels[which]);
					currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
					mazeEditorView.setMaze(currentMazeSelection);
					mazeEditorView.invalidate();
					Toast.makeText(MazeEditorActivity.this, "Level opened", Toast.LENGTH_SHORT).show();
				}
			});
			builder.create().show();
		}
	}

	private void handleSaveAsMenuOption() {
		new SaveLevelDialogFragment().show(getFragmentManager(), "savelevel_dialogfragment");
	}
	
	@Override
	public void onLevelSaveRequested(String levelname) {
		currentLevelName = levelname;
		saveCurrentLevel(levelname);
	}

	private void saveCurrentLevel(String levelname) {
		LevelManager.saveCustomLevel(MazeEditorActivity.this, levelname, currentMaze);
		Toast.makeText(MazeEditorActivity.this, "Level saved", Toast.LENGTH_SHORT).show();
	}
	
	private void handlePlayMenuOption() {
		if (currentMaze == null) {
			Toast.makeText(this, "No level to play", Toast.LENGTH_SHORT).show();
		} else {
			Bundle b = new Bundle();
			b.putSerializable("maze", currentMaze);
			Intent i = new Intent(this, MainGameActivity.class);
			i.putExtras(b);
			startActivity(i);
		}
	}
}
