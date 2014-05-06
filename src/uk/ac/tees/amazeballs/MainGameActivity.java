package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.views.MazeBallView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * The main activity for displaying a game.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MainGameActivity extends Activity implements SensorEventListener {
	
	private static final long GAME_TICK_INTERVAL = 5;
	
	private Sensor accelerometerSensor;
	private SensorManager sensorManager;
	
	private MazeSelection mazeSelection;
	private MazeBallView gameView;
	private GameController gameController;
	private GameTickHandler tickHandler;
	
	private boolean running = true;
	private long lastMoveTime;
	
	
	private class GameTickHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	update();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
		
		// Check the device has an accelerometer
		sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0) {
			// Register ourselves as a listener so that we can receive accelerometer updates
			accelerometerSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
		} else {
			// Failure! No accelerometer
			Log.e(this.getClass().getName(), "no accelerometer on device, unable to play game");
			//finish(); // No accelerometer on the emulator
		}

		// Get a reference to the inflated MazeBallView 
		gameView = (MazeBallView) findViewById(R.id.main_game_view);

		// Load the maze to play
		Maze loadedMaze = (Maze) getIntent().getExtras().getSerializable("maze");
		
		/*
		 * Create a maze selection to view only a small portion of the maze so
		 * that we can have mazes that are much larger than most devices'
		 * displays. The size specified here represents the grid size displayed
		 * in the MazeEditorView.
		 */
		mazeSelection = new MazeSelection(loadedMaze, 0, 0, 10, 15);
		
		// Set the maze for the MazeEditorView to display
		gameView.setMaze(mazeSelection);
		
		// Initialize the ball position, image and size
		gameView.getBall().position_x = 70;
		gameView.getBall().position_y = 70;
		gameView.getBall().image = TileImageFactory.getImage(TileType.Ball);
		gameView.getBall().imageRelativeSize = 0.8f;
				
		// Create a GameController for handling the moving, collisions and physics for the game
		gameController = new GameController(mazeSelection, gameView);
		
		// Start the game loop		
		tickHandler = new GameTickHandler();
		tickHandler.sendMessageDelayed(tickHandler.obtainMessage(0), 1000);		// HACKISH!
	}

	@Override
	protected void onPause() {
		super.onPause();
		running = false;
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
		running = true;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Give the GameController the latest accelerometer readings
		gameController.lastAccelerometerReading_x = event.values[0];
		gameController.lastAccelerometerReading_y = event.values[1];
	}
	
	/**
	 * Performs an update in the game simulation.
	 */
	private void update() {
		// Only perform updates if the game is running
		if (running) {
			long now = System.currentTimeMillis();
			/*
			 * Only perform an update if the specified delay time has elapsed
			 * since the last update.
			 */
			if ((now - lastMoveTime) >= GAME_TICK_INTERVAL) {
				//Log.d(this.getClass().getName(), "tick");
				gameController.update();
				lastMoveTime = System.currentTimeMillis();
				tickHandler.sleep(GAME_TICK_INTERVAL);
			} else {
				// Wait the remaining time
				tickHandler.sleep(GAME_TICK_INTERVAL - (now - lastMoveTime));
			}
		}
	}

}
