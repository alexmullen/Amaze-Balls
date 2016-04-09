package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.dialogs.LevelChooseDialogFragment;
import uk.ac.tees.amazeballs.dialogs.LevelChooseDialogFragment.OnLevelChooseListener;
import uk.ac.tees.amazeballs.dialogs.NewLevelDialogFragment;
import uk.ac.tees.amazeballs.dialogs.NewLevelDialogFragment.OnNewLevelRequestListener;
import uk.ac.tees.amazeballs.dialogs.SaveLevelDialogFragment;
import uk.ac.tees.amazeballs.dialogs.SaveLevelDialogFragment.OnLevelSaveRequestListener;
import uk.ac.tees.amazeballs.dialogs.TileChooseDialogFragment;
import uk.ac.tees.amazeballs.dialogs.TileChooseDialogFragment.OnTileChooseListener;
import uk.ac.tees.amazeballs.maze.MazeNew;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeWorld;
import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import uk.ac.tees.amazeballs.views.MazeEditorView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;


/**
 * The main activity for editing a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class EditorActivity extends Activity 
		implements 	MazeEditorView.OnTileTouchedListener,
					OnLevelSaveRequestListener, 
					OnNewLevelRequestListener,
					OnLevelChooseListener,
					OnTileChooseListener {

	private MazeNew currentMaze;
	private String currentLevelName;
	private MazeEditorView mazeEditorView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze_editor);
		
		// Check whether we need to restore our state
		if (savedInstanceState != null) {
			// Restore our state
			currentMaze = (MazeNew) savedInstanceState.getSerializable("maze");
			currentLevelName = savedInstanceState.getString("level_name");
		} else {
			// Create a new blank maze ready to be edited
			currentMaze = MazeFactory.createBorderedMaze(25, 30);
		}

		// Create a world and camera
		MazeWorld mazeWorld = new MazeWorld(currentMaze, 20);
		MazeWorldCamera mazeWorldCamera = new MazeWorldCamera(mazeWorld, 0, 0,
				10 * mazeWorld.tilesize, 
				17 * mazeWorld.tilesize);
		
		mazeEditorView = (MazeEditorView) findViewById(R.id.maze_grid_view);
		mazeEditorView.setOnTileTouchedListener(this);
		mazeEditorView.setCamera(mazeWorldCamera);
		mazeEditorView.invalidate();
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		// TODO Persist the edited maze
	}



	@Override
	protected void onResume() {
		super.onResume();
		// TODO Restore the edited maze state
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("level_name", currentLevelName);
		outState.putParcelable("maze", currentMaze);
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
		new NewLevelDialogFragment().show(getFragmentManager(), "newlevel_dialogfragment");
	}
	
	@Override
	public void onNewLevelRequested(int width, int height) {
		currentLevelName = null;
		currentMaze = MazeFactory.createBorderedMaze(width, height);
		
		MazeWorld mazeWorld = new MazeWorld(currentMaze, 20);
		MazeWorldCamera mazeWorldCamera = new MazeWorldCamera(mazeWorld, 0, 0,
				10 * mazeWorld.tilesize, 
				17 * mazeWorld.tilesize);
		mazeEditorView.setCamera(mazeWorldCamera);

		mazeEditorView.invalidate();
	}
	
	private void handleOpenMenuOption() {
		String[] levels = LevelManager.getCustomLevels(this);
		if (levels.length == 0) {
			Toast.makeText(this, "There are no custom levels to open", Toast.LENGTH_LONG).show();
		} else {
			Bundle b = new Bundle();
			b.putStringArray("levels", levels);
			b.putString("title", "Choose a level to edit");
			LevelChooseDialogFragment chooseDialog = new LevelChooseDialogFragment();
			chooseDialog.setArguments(b);
			chooseDialog.show(getFragmentManager(), "chooseleveltoedit_dialogfragment");
		}
	}
	
	@Override
	public void onLevelChosen(String levelname) {
		MazeNew loadedCustomMaze = LevelManager.loadCustomLevel(this, levelname);
		if (loadedCustomMaze == null) {
			displayLevelOpenErrorDialog(levelname);
		} else {
			currentLevelName = levelname;
			currentMaze = loadedCustomMaze;

			MazeWorld mazeWorld = new MazeWorld(currentMaze, 20);
			MazeWorldCamera mazeWorldCamera = new MazeWorldCamera(mazeWorld, 0, 0,
					10 * mazeWorld.tilesize, 
					17 * mazeWorld.tilesize);
			mazeEditorView.setCamera(mazeWorldCamera);
			
			mazeEditorView.invalidate();
			Toast.makeText(this, "Level opened", Toast.LENGTH_SHORT).show();			
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
		LevelManager.saveCustomLevel(EditorActivity.this, levelname, currentMaze);
		Toast.makeText(EditorActivity.this, "Level saved", Toast.LENGTH_SHORT).show();
	}
	
	private void handlePlayMenuOption() {
		if (currentMaze == null) {
			Toast.makeText(this, "No level to play", Toast.LENGTH_SHORT).show();
		} else {
			Bundle b = new Bundle();
			b.putParcelable("maze", currentMaze);
			b.putBoolean("test-mode", true);
			Intent i = new Intent(this, GameActivity.class);
			i.putExtras(b);
			startActivity(i);
		}
	}
	
	@Override
	public void onTileTouched(int x, int y, boolean wasLongPress) {
		// Prevent the edges of the maze being modified
		if (currentMaze.isTileAtAnEdge(x, y)) {
			return;
		}
		
		if (wasLongPress) {
			Bundle b = new Bundle();
			b.putInt("x", x);
			b.putInt("y", y);
			b.putString("title", "Choose a block");
			TileChooseDialogFragment chooseDialog = new TileChooseDialogFragment();
			chooseDialog.setArguments(b);
			chooseDialog.show(getFragmentManager(), "choosetiletoplace_dialogfragment");
		} else {
			// Toggle the tile.
			int tileToPlace = MazeNew.FLOOR_TILE;
			if (currentMaze.getTileAt(x, y) == MazeNew.FLOOR_TILE) {
				tileToPlace = MazeNew.WALL_TILE;
			}
			currentMaze.setTileAt(x, y, tileToPlace);
			
			// Repaint the view
			mazeEditorView.invalidate();
		}
	}

	@Override
	public void onTileChosen(TileChooseDialogFragment dialog, int type) {
		Bundle args = dialog.getArguments();
		currentMaze.setTileAt(args.getInt("x"), args.getInt("y"), type);
		mazeEditorView.invalidate();
	}
	
	private void displayLevelOpenErrorDialog(String levelname) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("Level open error");
		builder.setMessage("Couldn't open the level '" + levelname + "'.");
		builder.setNeutralButton("OK", null);
		builder.create().show();
	}

}
