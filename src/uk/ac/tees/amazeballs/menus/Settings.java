package uk.ac.tees.amazeballs.menus;

import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

public class Settings extends Activity {
	
	MediaPlayer mp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
	}	

	public void onMusicToggle(View view) {
		
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
            // Turn music on    
		} else {
			// Turn music off
		}
	}
	
	public void onWeatherToggle(View view) {
		
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			// Turn weather on
		} else {
			// Turn weather off
		}
	}
	
}
