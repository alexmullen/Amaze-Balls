package uk.ac.tees.amazeballs.menus;

import java.util.Random;

import uk.ac.tees.amazeballs.LevelManager;
import uk.ac.tees.amazeballs.MainGameActivity;
import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LevelSelect extends Activity{
	
	private Spinner levelSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_select);
		
		levelSpinner = (Spinner) findViewById(R.id.spinner_standard_levels);
		levelSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.simplerow, LevelManager.getLevels(this)));
	}
	
	public void onPlayLevelButtonClicked(View v) {
		Object selectedItem = levelSpinner.getSelectedItem();
		if (selectedItem != null) {
			Bundle b = new Bundle();
			b.putSerializable("maze", LevelManager.loadLevel(this, (String)selectedItem));
			Intent i = new Intent(this, MainGameActivity.class);
			i.putExtras(b);
			startActivity(i);
		}
	}
	
	public void onRandomLevelButtonClicked(View v) {
		String[] standardLevels = LevelManager.getLevels(this);
		Random rand = new Random(); 
		int randomIndex = rand.nextInt(standardLevels.length);
		
		Bundle b = new Bundle();
		b.putSerializable("maze", LevelManager.loadLevel(this, standardLevels[randomIndex]));
		Intent i = new Intent(this, MainGameActivity.class);
		i.putExtras(b);
		startActivity(i);
	}
	
	public void onCustomLevelsButtonClicked(View v) {
		
	}

}
