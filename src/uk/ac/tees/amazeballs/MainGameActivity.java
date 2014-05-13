package uk.ac.tees.amazeballs;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;

import net.aksingh.java.api.owm.CurrentWeatherData;
import uk.ac.tees.amazeballs.dialogs.NewScoreDialogFragment;
import uk.ac.tees.amazeballs.dialogs.NewScoreDialogFragment.OnScoreSaveRequestListener;
import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.menus.Highscores;
import uk.ac.tees.amazeballs.menus.Score;
import uk.ac.tees.amazeballs.menus.ScoreTableHandler;
import uk.ac.tees.amazeballs.menus.Settings;
import uk.ac.tees.amazeballs.views.MazeViewport;
import uk.ac.tees.amazeballs.weather.Weather;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
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
public class MainGameActivity extends Activity implements SensorEventListener, OnScoreSaveRequestListener {
	
	private static final long GAME_TICK_INTERVAL = 16;
	
	private Sensor accelerometerSensor;
	private SensorManager sensorManager;

	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private ProgressDialog weatherProgressDialog;
	private RetrieveWeatherDataTask receiveWeatherDataTask;
	
	private Maze loadedMaze;
	private MazeViewport gameView;
	private GameController gameController;
	private GameTickHandler tickHandler;
	
	private long startTime;
	private long runningTime;
	private long lastUpdateTime;
	
	private boolean gameHasStarted;
	private boolean running;
	
	private MediaPlayer mediaPlayer;
	
	
	/**
	 * A task for downloading weather data asynchronously so as not to block
	 * the UI thread.
	 * 
	 * @author Alex Mullen (J9858839)
	 *
	 */
	private class RetrieveWeatherDataTask extends AsyncTask<Location, Void, CurrentWeatherData> {
		@Override
		protected CurrentWeatherData doInBackground(Location... args) {
			return Weather.getWeatherData(
					(float)args[0].getLatitude(), 
					(float)args[0].getLongitude());
		}
		@Override
		protected void onPostExecute(CurrentWeatherData result) {
			// Only apply if we weren't cancelled.
			if (!this.isCancelled()) {
				loadedMaze.replaceAll(TileType.Weather, determineWhetherTiles(result));
				gameView.invalidate();
				weatherProgressDialog.dismiss();
				startGame();
			}
		}
	}

	/**
	 * A handler to use for running the game loop.
	 * 
	 * @author Alex Mullen (J9858839)
	 *
	 */
	private static class GameTickHandler extends Handler {
        private final WeakReference<MainGameActivity> gameActivityReference;
		GameTickHandler(MainGameActivity gameActivity) {
			gameActivityReference = new WeakReference<MainGameActivity>(gameActivity);
		}
		@Override
        public void handleMessage(Message msg) {
			MainGameActivity gameActivity = gameActivityReference.get();
			if (gameActivity != null) {
				gameActivity.update();
			}
        }
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendEmptyMessageDelayed(0, delayMillis);
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.activity_main_game);

		// Load the maze to play
		loadedMaze = (Maze) getIntent().getExtras().getSerializable("maze");
		

		startTime = System.currentTimeMillis();


		// Get a reference to the inflated MazeViewport 
		gameView = (MazeViewport) findViewById(R.id.main_game_view);

		/* 
		 * Create a GameController for handling the moving, collisions and physics 
		 * for the game.
		 */
		gameController = new GameController(loadedMaze, gameView);
		
		
		tickHandler = new GameTickHandler(this);
		
		
		// Start playing music if enabled
		SharedPreferences sp = 
				getSharedPreferences(Settings.SETTINGS_PREFS_NAME, Activity.MODE_PRIVATE);
		
		if (sp.getBoolean(Settings.SETTINGS_MUSIC, true) == true) {
			mediaPlayer = new MediaPlayer();
			mediaPlayer = MediaPlayer.create(this, R.raw.maze);
			mediaPlayer.setLooping(true);
		}
		

		if (sp.getBoolean(Settings.SETTINGS_WEATHER, true) == true) {
			/* 
			 * Show an indeterminate progress dialog to signal to the user that we are
			 * doing something.
			 */
			weatherProgressDialog = ProgressDialog.show(
					this, 
					"Retrieving location...", 
					"Touch anywhere to cancel", 
					true, 
					true);
			
			/* 
			 * Handle the user cancelling the retrieval of location and weather if they
			 * wish. (It could possibly be taking too long)
			 */
			weatherProgressDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					handleCancelLocationAndWeatherRetrieval();
				}
			});
	
			// Acquire a reference to the system Location Manager
			locationManager = 
					(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			
			// Using the last known location should be sufficient for our purposes if available
			final Location lastKnownLocation = 
					locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			// Check if there was a cached last known location available
			if (lastKnownLocation != null) {
				// Use last known location
		    	handleLocationRetrieved(lastKnownLocation);
			} else {
				// Define a listener that responds to location updates
				locationListener = new LocationListener() {
				    public void onLocationChanged(final Location location) {
				    	/*
				    	 *  The first location update will be good enough for us so
				    	 *  we won't ask for anymore.
				    	 */
				    	locationManager.removeUpdates(this);
				    	handleLocationRetrieved(location);
				    }
				    public void onStatusChanged(String provider, int status, Bundle extras) {}
				    public void onProviderEnabled(String provider) {}
				    public void onProviderDisabled(String provider) {}
				};
				
				/*
				 *  Criteria for a location provider. For this we don't need super accuracy
				 *  or altitude.
				 */
				Criteria criteria = new Criteria();
				criteria.setAltitudeRequired(false);
				criteria.setCostAllowed(false);
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);
				
				// Retrieve the best provider if any
				String bestProvider = locationManager.getBestProvider(criteria, true);
				if (bestProvider != null) {
					// Register the listener with the Location Manager to receive location updates
					locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);
				} else {
					// No location providers so just start game without local weather tiles
					loadedMaze.replaceAll(TileType.Weather, TileType.Floor);
					gameView.invalidate();
					startGame();
				}
			}
		} else {
			// Weather is disabled so just start the game
			loadedMaze.replaceAll(TileType.Weather, TileType.Floor);
			gameView.invalidate();
			startGame();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();		
		// Start playing the music if it was enabled
		if (mediaPlayer != null) {
			mediaPlayer.start();
		}
		initAccelerometer();
		running = true;
		// Only start the game when told (might have to wait for weather data)
		if (gameHasStarted) {
			tickHandler.sendEmptyMessageDelayed(0, 1000);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		running = false;
		sensorManager.unregisterListener(this);
		// Pause the music if it was enabled
		if (mediaPlayer != null) {
			mediaPlayer.pause();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		running = false;
		sensorManager.unregisterListener(this);
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
	
	private void handleCancelLocationAndWeatherRetrieval() {
		// Cancel the weather retrieval task if it was started
		if (receiveWeatherDataTask != null) {
			receiveWeatherDataTask.cancel(true);
		}
		// Prevent anymore location updates being received if we were receiving them.
		if (locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
		// Just start the game without localised weather tiles
		loadedMaze.replaceAll(TileType.Weather, TileType.Floor);
		gameView.invalidate();
		startGame();
	}
	
	private void handleLocationRetrieved(Location location) {
    	weatherProgressDialog.setTitle("Retrieving weather data...");
    	receiveWeatherDataTask = new RetrieveWeatherDataTask();
    	receiveWeatherDataTask.execute(location);
	}

	private boolean initAccelerometer() {
		// Check the device has an accelerometer
		sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0) {
			// Register ourselves as a listener so that we can receive accelerometer updates
			accelerometerSensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
			return true;
		} else {
			// Failure! No accelerometer
			Log.e(this.getClass().getName(), "no accelerometer on device, unable to play game");
			//finish(); // No accelerometer on the emulator
			return false;
		}
	}
	
	private void startGame() {
		gameHasStarted = true;
		tickHandler.sendEmptyMessageDelayed(0, 1000);
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
			if ((now - lastUpdateTime) >= GAME_TICK_INTERVAL) {
				if (!gameController.isFinished()) {
					gameController.update();
					lastUpdateTime = System.currentTimeMillis();
					long updateTimeTook = lastUpdateTime - now;
					tickHandler.sleep(GAME_TICK_INTERVAL - updateTimeTook);
				} else {
					runningTime = ((System.currentTimeMillis() - startTime) / 1000);
					new NewScoreDialogFragment().show(getFragmentManager(), "newscore_dialogfragment");
				}
			} else {
				// Wait the remaining time
				tickHandler.sleep(GAME_TICK_INTERVAL - (now - lastUpdateTime));
			}
		}
	}
	
	/**
	 * Determine what the weather tiles should be based on weather data.
	 * Rain has priority over ice so if its both raining and cold, the
	 * weather tiles should become rain tiles.
	 * 
	 * @param cwd
	 * @return
	 */
	private TileType determineWhetherTiles(CurrentWeatherData cwd) {
		if (cwd == null) {
			return TileType.Floor;
		} else if (cwd.getRain_Object().hasRain3Hours()) {
			return TileType.Rain;
		} else if (cwd.getMainData_Object().hasTemperature()) {
			if (cwd.getMainData_Object().getMinTemperature() <= 32) {
				return TileType.Ice;
			}
		}
		return TileType.Floor;
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
		Highscores.scoreHandler.addScore(new Score(Highscores.scoreHandler.newID(), name, (int)runningTime, df));
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
