package uk.ac.tees.amazeballs.menus;

import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;


/**
 * The activity for displaying and allowing the changing of settings.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class Settings extends Activity {

	public static final String SETTINGS_PREFS_NAME = "settings";
	public static final String SETTINGS_MUSIC = "music";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		SharedPreferences sp = 
				getSharedPreferences(SETTINGS_PREFS_NAME, Activity.MODE_PRIVATE);
		
		ToggleButton musicToggle = (ToggleButton) findViewById(R.id.togglebutton_music);
		
		musicToggle.setChecked(sp.getBoolean(SETTINGS_MUSIC, true));
	}	

	public void onMusicToggle(View view) {
		SharedPreferences.Editor spEditor = 
				getSharedPreferences(SETTINGS_PREFS_NAME, Activity.MODE_PRIVATE).edit();
		
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			spEditor.putBoolean(SETTINGS_MUSIC, true);
		} else {
			spEditor.putBoolean(SETTINGS_MUSIC, false);
		}
		spEditor.apply();
	}
	
}
