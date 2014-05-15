package uk.ac.tees.amazeballs.menus;

import java.util.Random;

import uk.ac.tees.amazeballs.LevelManager;
import uk.ac.tees.amazeballs.MainGameActivity;
import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.maze.Maze;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * The activity for selecting a level to play.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class LevelSelect extends Activity{
	
	private Spinner levelChoiceSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select);
		
		levelChoiceSpinner = (Spinner) findViewById(R.id.spinner_standard_levels);
		levelChoiceSpinner.setAdapter(
				new ArrayAdapter<String>(this, R.layout.simplerow, LevelManager.getLevels(this)));
	}
	
	public void onPlayLevelButtonClicked(View v) {
		Object selectedItem = levelChoiceSpinner.getSelectedItem();
		// Make sure something was selected
		if (selectedItem != null) {
			loadAndPlayLevel((String)selectedItem);
		}
	}
	
	public void onRandomLevelButtonClicked(View v) {
		// Choose a random array index value then choose the level name at that location
		String[] standardLevels = LevelManager.getLevels(this);
		int randomIndex = new Random().nextInt((standardLevels.length));
		loadAndPlayLevel(standardLevels[randomIndex]);
	}
	
	public void onCustomLevelsButtonClicked(View v) {
		final String[] customLevels = LevelManager.getCustomLevels(this);
		if (customLevels.length == 0) {
			Toast.makeText(this, "There are no custom levels.", Toast.LENGTH_LONG).show();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Choose a custom level to play");
			builder.setItems(customLevels, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Maze customLoadedMaze = LevelManager.loadCustomLevel(LevelSelect.this, customLevels[which]);
					if (customLoadedMaze != null) {
						playLevel(customLoadedMaze);
					} else {
						// For some reason we couldn't load the level so display a dialog telling the user
						displayLevelLoadErrorDialog(customLevels[which]);
					}
				}
			});
			builder.create().show();
		}
	}
	
	/**
	 * Loads a level then sends an intent to the game activity to start playing
	 * the loaded level.
	 * 
	 * @param levelname the name of the level to load
	 */
	private void loadAndPlayLevel(String levelname) {
		// Load the maze then check it was loaded
		Maze loadedMaze = LevelManager.loadLevel(this, levelname);
		if (loadedMaze != null) {
			playLevel(loadedMaze);
		} else {
			// For some reason we couldn't load the level so display a dialog telling the user
			displayLevelLoadErrorDialog(levelname);
		}
	}
	
	private void playLevel(Maze loadedMaze) {
		// Send an intent containing the maze to play
		Bundle b = new Bundle();
		b.putSerializable("maze", loadedMaze);
		Intent i = new Intent(this, MainGameActivity.class);
		i.putExtras(b);
		startActivity(i);
	}
	
	private void displayLevelLoadErrorDialog(String levelname) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("Level load error");
		builder.setMessage("Couldn't load the level " + levelname + ".");
		builder.setNeutralButton("OK", null);
		builder.create().show();
	}

}
