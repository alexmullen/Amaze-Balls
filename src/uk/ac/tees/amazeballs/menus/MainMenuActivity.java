package uk.ac.tees.amazeballs.menus;

import uk.ac.tees.amazeballs.MazeEditorActivity;
import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		Button btnLevelSelect = (Button) findViewById(R.id.button1);
		btnLevelSelect.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), LevelSelect.class);
                startActivity(nextScreen);
 
            }
        });
		
		Button btnHighscores = (Button) findViewById(R.id.button2);
		btnHighscores.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), Highscores.class);
                startActivity(nextScreen);
 
            }
        });
		
		Button btnLevelEditor = (Button) findViewById(R.id.button3);
		btnLevelEditor.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), MazeEditorActivity.class);
                startActivity(nextScreen);
 
            }
        });
		
		Button btnSettings = (Button) findViewById(R.id.button4);
		btnSettings.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent nextScreen = new Intent(getApplicationContext(), Settings.class);
                startActivity(nextScreen);
 
            }
        });
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main_menu, menu);
//		return true;
//	}

}
