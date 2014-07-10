package uk.ac.tees.amazeballs;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import uk.ac.tees.amazeballs.dialogs.NewScoreDialogFragment;
import uk.ac.tees.amazeballs.dialogs.NewScoreDialogFragment.OnScoreSaveRequestListener;
import uk.ac.tees.amazeballs.maze.MazeNew;
import uk.ac.tees.amazeballs.maze.MazeScanner;
import uk.ac.tees.amazeballs.menus.Highscores;
import uk.ac.tees.amazeballs.menus.Score;
import uk.ac.tees.amazeballs.menus.ScoreTableHandler;
import uk.ac.tees.amazeballs.menus.Settings;
import uk.ac.tees.amazeballs.views.MazeViewport;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * The main activity for playing a level.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class GameActivity extends Activity implements SensorEventListener, OnScoreSaveRequestListener {
	
	private static final long GAME_TICK_INTERVAL = 16;
	
	private SensorManager sensorManager;
	
	private MazeNew loadedMaze;
	private MazeViewport gameView;
	private GameController gameController;
	
	private Thread gameThread;
	
	private long startTime;
	private long timeTaken;
	
	private boolean inTestMode;
	private volatile boolean gameLoopThreadIsRunning;
	
	private MediaPlayer mediaPlayer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.activity_main_game);
		
		// Load the maze to play
		loadedMaze = (MazeNew) getIntent().getExtras().getParcelable("maze");
		
		/*
		 * Determine whether the specified level is getting tested. This means we don't have to waste
		 * time recording the time or do any other things intended only for normal gameplay.
		 */
		inTestMode = getIntent().getExtras().getBoolean("test-mode", false);
		
		/*
		 * Scan the maze and collect all the statistics about it in one go. This is more efficient than
		 * re-scanning the whole thing each time we want to check something.
		 */
		MazeScanner.ScanData mazeScanData = MazeScanner.scan(loadedMaze);

		// Make sure there is somewhere to place the ball
		if (mazeScanData.tileposition_firststart == null && 
				mazeScanData.tileposition_firstfloor == null) {
			// No place to place the ball! Notify the user then close this activity.
			Toast.makeText(this, "There is no where to place the ball in this level!", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
// Record the time at which the game started playing (if we aren't in test mode)
if (!inTestMode) {
startTime = System.currentTimeMillis();		// !!! The game doesn't start playing at this point
}

		// Work out where the starting position of the ball will be.
		Point startPosition = (mazeScanData.tileposition_firststart != null ? 
				mazeScanData.tileposition_firststart : mazeScanData.tileposition_firstfloor);
		
		// Get a reference to the inflated MazeViewport 
		gameView = (MazeViewport) findViewById(R.id.main_game_view);

		/* 
		 * Create a GameController for handling the moving, collisions and physics 
		 * for the game.
		 */
		gameController = new GameController(loadedMaze, gameView, startPosition);

		// Load and prepare the music if enabled
		SharedPreferences sp = getSharedPreferences(Settings.SETTINGS_PREFS_NAME, Activity.MODE_PRIVATE);
		if (sp.getBoolean(Settings.SETTINGS_MUSIC, true) == true) {
			mediaPlayer = MediaPlayer.create(this, R.raw.maze);
			mediaPlayer.setLooping(true);
		}

// Needed as some previously built levels will contain weather tiles in them
loadedMaze.replaceAllAt(mazeScanData.tilepositions_weather, MazeNew.FLOOR_TILE);
		gameView.invalidate();
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		// Start playing the music if it was enabled
		if (mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (!initAccelerometer()) {
			// Failed to register with accelerometer sensor, device probably doesn't have one
			// TODO: Display an error dialog
			// Finish();
		}
		startGameLoopThread();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Notify the game loop thread to stop running
		gameLoopThreadIsRunning = false;
		sensorManager.unregisterListener(this);
		// Pause the music if it was enabled
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
		/*
		 *  To keep things in sync, wait for the game loop thread to finish if it was
		 *  started. (The user could switch activity whilst the weather is downloading
		 *  which would mean gameThread wasn't initialized yet)
		 */
		if (gameThread != null) {
			try {
				gameThread.join();
			} catch (InterruptedException e) {
	
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// It's important to release the resources used by the music if it was enabled
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
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

	private boolean initAccelerometer() {
		// Check the device has an accelerometer
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		if (sensorList.size() > 0) {
			// Register ourselves as a listener so that we can receive accelerometer updates
			sensorManager.registerListener(this, sensorList.get(0), SensorManager.SENSOR_DELAY_GAME);
			return true;
		} else {
			// Failure! No accelerometer
			Log.e(getClass().getName(), "no accelerometer on device, unable to play game");
			//finish(); // No accelerometer on the emulator
			return false;
		}
	}
	
	private void startGameLoopThread() {
		if (gameLoopThreadIsRunning) {
			// Don't start again if it's already running
			return;
		}
		gameLoopThreadIsRunning = true;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				// Give the game a slight delay before stuff happens
				SystemClock.sleep(1000);
				while (gameLoopThreadIsRunning) {
					if (gameController.isFinished()) {
						/*
						 *  The game has finished so if not in test mode, prompt the user on 
						 *  whether they want to save their time.
						 */
						if (!inTestMode) {
							timeTaken = ((System.currentTimeMillis() - startTime) / 1000);
							GameActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									new NewScoreDialogFragment().show(getFragmentManager(), "newscore_dialogfragment");
								}
							});
						} else {
							// We're in test mode so just finish now
							finish();
						}
						// Need to exit the game loop
						gameLoopThreadIsRunning = false;
						return;
					} else {
						// The game isn't finished so perform another update
						long startUpdateTime = System.currentTimeMillis();
						gameController.update();
						gameView.postInvalidate();
						long updateTimeTook = System.currentTimeMillis() - startUpdateTime;
						long sleepTime = (GAME_TICK_INTERVAL - updateTimeTook);
						if (sleepTime > 0) {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								gameLoopThreadIsRunning = false;
								Log.d("Game loop", "interupted");
								return;
							}							
						}
					}
				}
			}
		};
		gameThread = new Thread(r);
		gameThread.start();
	}
	
	/**
	 * Adds the input data and completion time to a score object then writes it to the database.
	 * 
	 * @param name the name the user wants to associate their score with
	 */
	@Override
	public void onScoreSaveRequested(String name) {
		String df = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(System.currentTimeMillis()));
		Highscores.scoreHandler = new ScoreTableHandler(this);
		Highscores.scoreHandler.addScore(new Score(Highscores.scoreHandler.newID(), name, (int)timeTaken, df));
		finish();
	}
	
	/**
	 * The user clicks cancel when they don't want to save their name and score.
	 */
	@Override
	public void onScoreSaveCancelled() {
		finish();
	}
	
}
