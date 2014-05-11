package uk.ac.tees.amazeballs.menus;

import uk.ac.tees.amazeballs.MazeEditorActivity;
import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
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
                Intent nextScreen = new Intent(getApplicationContext(), MazeEditorActivity.class);
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
		TileImageFactory.registerImage(TileType.Floor, this.getResources().getDrawable(R.drawable.floor));
		TileImageFactory.registerImage(TileType.Wall, this.getResources().getDrawable(R.drawable.wall));
		TileImageFactory.registerImage(TileType.Ball, this.getResources().getDrawable(R.drawable.ball));
		
		TileImageFactory.registerImage(TileType.Chest, this.getResources().getDrawable(R.drawable.chest));
		TileImageFactory.registerImage(TileType.Door, this.getResources().getDrawable(R.drawable.door));
		TileImageFactory.registerImage(TileType.Goal, this.getResources().getDrawable(R.drawable.goal));
		TileImageFactory.registerImage(TileType.Ice, this.getResources().getDrawable(R.drawable.ice));
		TileImageFactory.registerImage(TileType.Key, this.getResources().getDrawable(R.drawable.key));
		TileImageFactory.registerImage(TileType.Penalty, this.getResources().getDrawable(R.drawable.penalty));
		TileImageFactory.registerImage(TileType.Rain, this.getResources().getDrawable(R.drawable.rain));
		TileImageFactory.registerImage(TileType.Start, this.getResources().getDrawable(R.drawable.start));
		TileImageFactory.registerImage(TileType.Weather, this.getResources().getDrawable(R.drawable.weathertile));
	}
	
}
