package uk.ac.tees.amazeballs;

import android.os.Bundle;
import android.app.Activity;

/**
 * The main activity for displaying a game.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MainGameActivity extends Activity {

	private MazeGameView gameView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
		
		gameView = (MazeGameView) findViewById(R.id.main_game_view);
	}

}
