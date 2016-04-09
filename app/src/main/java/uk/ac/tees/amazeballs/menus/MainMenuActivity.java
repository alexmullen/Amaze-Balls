package uk.ac.tees.amazeballs.menus;

import uk.ac.tees.amazeballs.EditorActivity;
import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.maze.MazeNew;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * The main menu activity that is displayed when the app starts up.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MainMenuActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		Button btnLevelSelect = (Button) findViewById(R.id.button_play_level);
		btnLevelSelect.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), LevelSelect.class);
                startActivity(nextScreen);
            }
        });
		
		Button btnHighscores = (Button) findViewById(R.id.button_custom_levels);
		btnHighscores.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), Highscores.class);
                startActivity(nextScreen);
            }
        });
		
		Button btnLevelEditor = (Button) findViewById(R.id.button_random_level);
		btnLevelEditor.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(nextScreen);
            }
        });
		
		Button btnSettings = (Button) findViewById(R.id.button4);
		btnSettings.setOnClickListener(new View.OnClickListener() { 
            public void onClick(View view) {
                Intent nextScreen = new Intent(getApplicationContext(), Settings.class);
                startActivity(nextScreen);
            }
        });
		
		
		loadTiles();
	}
	
	/**
	 * Loads and registers the tile images used.
	 */
	private void loadTiles() {
		TileImageFactory.registerImage(MazeNew.FLOOR_TILE, getResources().getDrawable(R.drawable.floor));
		TileImageFactory.registerImage(MazeNew.WALL_TILE, getResources().getDrawable(R.drawable.wall));
		TileImageFactory.registerImage(MazeNew.BALL_TILE, getResources().getDrawable(R.drawable.ball));
		
		TileImageFactory.registerImage(MazeNew.CHEST_TILE, getResources().getDrawable(R.drawable.chest));
		TileImageFactory.registerImage(MazeNew.DOOR_TILE, getResources().getDrawable(R.drawable.door));
		TileImageFactory.registerImage(MazeNew.GOAL_TILE, getResources().getDrawable(R.drawable.goal));
		TileImageFactory.registerImage(MazeNew.ICE_TILE, getResources().getDrawable(R.drawable.ice));
		TileImageFactory.registerImage(MazeNew.KEY_TILE, getResources().getDrawable(R.drawable.key));
		TileImageFactory.registerImage(MazeNew.PENALTY_TILE, getResources().getDrawable(R.drawable.penalty));
		TileImageFactory.registerImage(MazeNew.RAIN_TILE, getResources().getDrawable(R.drawable.rain));
		TileImageFactory.registerImage(MazeNew.START_TILE, getResources().getDrawable(R.drawable.start));
		TileImageFactory.registerImage(MazeNew.WEATHER_TILE, getResources().getDrawable(R.drawable.weathertile));
	}
	
}
