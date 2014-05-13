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
	public static final String SETTINGS_WEATHER = "weather";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		SharedPreferences sp = 
				getSharedPreferences(SETTINGS_PREFS_NAME, Activity.MODE_PRIVATE);
		
		ToggleButton musicToggle = (ToggleButton) findViewById(R.id.togglebutton_music);
		ToggleButton weatherToggle = (ToggleButton) findViewById(R.id.togglebutton_weather);
		
		musicToggle.setChecked(sp.getBoolean(SETTINGS_MUSIC, true));
		weatherToggle.setChecked(sp.getBoolean(SETTINGS_WEATHER, true));
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
	
	public void onWeatherToggle(View view) {
		SharedPreferences.Editor spEditor = 
				getSharedPreferences(SETTINGS_PREFS_NAME, Activity.MODE_PRIVATE).edit();
		
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			spEditor.putBoolean(SETTINGS_WEATHER, true);
		} else {
			spEditor.putBoolean(SETTINGS_WEATHER, false);
		}
		spEditor.apply();
	}
	
}
