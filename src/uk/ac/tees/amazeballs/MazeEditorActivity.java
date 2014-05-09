package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.views.MazeEditorView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MazeEditorActivity extends Activity {

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
			currentLevelName = savedInstanceState.getString("level_name");
			currentMaze = (Maze) savedInstanceState.getSerializable("maze");
			
			/*
			 * Create a maze selection to view only a small portion of the maze so
			 * that we can have mazes that are much larger than most devices'
			 * displays. The size specified here represents the grid size displayed
			 * in the MazeEditorView.
			 */
			currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
			
			// Set the maze for the MazeEditorView to display
			mazeEditorView.setMaze(currentMazeSelection);
			mazeEditorView.invalidate();
		} else {
			// Create a new bordered maze of the specified width and height
			//currentMaze = MazeFactory.createBorderedMaze(25, 30);
			handleNewMenuOption();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("level_name", currentLevelName);
		outState.putSerializable("maze", currentMaze);
		super.onSaveInstanceState(outState);
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
				// Check it already has a name that it has been saved by
				if (currentLevelName != null) {
					// Overwrite
					saveCurrentLevel(currentLevelName);
				} else {
					handleSaveAsMenuOption();
				}
				return true;
			case R.id.editor_file_saveas:
				handleSaveAsMenuOption();
				return true;
			case R.id.editor_play:
				handlePlayMenuOption();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	private void handleNewMenuOption() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View inflatedView = this.getLayoutInflater().inflate(R.layout.dialog_newmazesize, null);
		
		final SeekBar seekWidthBar = (SeekBar) inflatedView.findViewById(R.id.seekbar_maze_width);
		final SeekBar seekHeightBar = (SeekBar) inflatedView.findViewById(R.id.seekbar_maze_height);
		final TextView seekWidthValue = (TextView) inflatedView.findViewById(R.id.width_value);
		final TextView seekHeightValue = (TextView) inflatedView.findViewById(R.id.height_value);

		seekWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekWidthValue.setText(String.valueOf(Math.max(8, progress)));
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
		});
		
		seekHeightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekHeightValue.setText(String.valueOf(Math.max(8, progress)));
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
		});
		
		builder.setTitle("Choose the maze dimensions");
		builder.setView(inflatedView);
		builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentLevelName = null;
				currentMaze = MazeFactory.createBorderedMaze(
						Math.max(8, seekWidthBar.getProgress()), 
						Math.max(8, seekHeightBar.getProgress()));
				
				currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
				mazeEditorView.setMaze(currentMazeSelection);
				mazeEditorView.invalidate();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}
	
	private void handleOpenMenuOption() {
		final String[] levels = LevelManager.getCustomLevels(this);
		if (levels.length == 0) {
			Toast.makeText(this, "There are no custom levels to open", Toast.LENGTH_SHORT).show();
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View inflatedView = this.getLayoutInflater().inflate(R.layout.dialog_level_save, null);
		builder.setView(inflatedView);
		builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText levelNameTextView = (EditText) inflatedView.findViewById(R.id.dialog_save_levelname_edittext);
				currentLevelName = levelNameTextView.getText().toString();
				saveCurrentLevel(currentLevelName);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
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
