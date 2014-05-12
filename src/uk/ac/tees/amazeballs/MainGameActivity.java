package uk.ac.tees.amazeballs;

import net.aksingh.java.api.owm.CurrentWeatherData;
import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.views.MazeViewport;
import uk.ac.tees.amazeballs.weather.Weather;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
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
	
	private static final long GAME_TICK_INTERVAL = 16;
	
	private Sensor accelerometerSensor;
	private SensorManager sensorManager;
	
	private MazeViewport gameView;
	private GameController gameController;
	private GameTickHandler tickHandler;
	
	private boolean started;
	private boolean running;
	private long lastUpdateTime;
	
	private MediaPlayer mp = new MediaPlayer();
	
	
	private class GameTickHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
        	update();
        }
        
        public void sleep(long delayMillis) {
            //this.removeMessages(0);
            sendEmptyMessageDelayed(0, delayMillis);
            //sendMessageDelayed(obtainMessage(0), delayMillis);
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.activity_main_game);

		// Load the maze to play
		final Maze loadedMaze = (Maze) getIntent().getExtras().getSerializable("maze");
		

		// Get a reference to the inflated MazeViewport 
		gameView = (MazeViewport) findViewById(R.id.main_game_view);

		/* 
		 * Create a GameController for handling the moving, collisions and physics 
		 * for the game.
		 */
		gameController = new GameController(loadedMaze, gameView);
		
		
		tickHandler = new GameTickHandler();
		
		mp = MediaPlayer.create(this, R.raw.maze);
		mp.setLooping(true);
		

		
		final ProgressDialog pd = ProgressDialog.show(
				this, 
				null, 
				"Retrieving location...", 
				true, 
				true);
		
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		final Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			pd.setMessage("Retrieving weather data...");
			Runnable r = new Runnable() {
				@Override
				public void run() {
					final CurrentWeatherData cwd = Weather.getWeatherData(
							(float)lastKnownLocation.getLatitude(), 
							(float)lastKnownLocation.getLongitude());
					gameView.post( new Runnable() {
						@Override
						public void run() {
							applyWeatherToMaze(loadedMaze, cwd);
							started = true;
							tickHandler.sendMessageDelayed(tickHandler.obtainMessage(0), 1000);
						}
					});
				}
			};
			new Thread(r).start();
		} else {
			// Define a listener that responds to location updates
			final LocationListener locationListener = new LocationListener() {
			    public void onLocationChanged(final Location location) {
			    	// Called when a new location is found by the network location provider.
			    	locationManager.removeUpdates(this);
			    	
			    	Log.d(getClass().getName(), location.toString());
	
			    	pd.setMessage("Retrieving weather data...");
			    	
					Runnable r = new Runnable() {
						@Override
						public void run() {
							final CurrentWeatherData cwd = Weather.getWeatherData(
									(float)location.getLatitude(), 
									(float)location.getLongitude());
							gameView.post( new Runnable() {
								@Override
								public void run() {
									applyWeatherToMaze(loadedMaze, cwd);
									started = true;
									tickHandler.sendMessageDelayed(tickHandler.obtainMessage(0), 1000);
								}
							});
						}
					};
					new Thread(r).start();
			    }
			    public void onStatusChanged(String provider, int status, Bundle extras) {}
			    public void onProviderEnabled(String provider) {}
			    public void onProviderDisabled(String provider) {}
			};

			pd.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					locationManager.removeUpdates(locationListener);
					started = true;
					tickHandler.sendMessageDelayed(tickHandler.obtainMessage(0), 1000);
				}
			});
			pd.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					locationManager.removeUpdates(locationListener);
					started = true;
					tickHandler.sendMessageDelayed(tickHandler.obtainMessage(0), 1000);
				}
			});
			
			Criteria criteria = new Criteria();
			criteria.setAltitudeRequired(false);
			criteria.setCostAllowed(false);
			
			// Register the listener with the Location Manager to receive location updates
			locationManager.requestLocationUpdates(
					locationManager.getBestProvider(criteria, true), 0, 0, locationListener);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initAccelerometer();
		running = true;
		if (started) {
			//tickHandler.sendMessageDelayed(tickHandler.obtainMessage(0), 1000);
			tickHandler.sendEmptyMessageDelayed(0, 1000);
		}
		mp.start();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		running = false;
		sensorManager.unregisterListener(this);
		mp.pause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		running = false;
		sensorManager.unregisterListener(this);
		mp.release();
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
				//Log.d(this.getClass().getName(), "tick");
				if (gameController.isFinished()) {
					Toast.makeText(this, "Level completed", Toast.LENGTH_LONG).show();
					finish();
					return;
				}
				gameController.update();
				lastUpdateTime = System.currentTimeMillis();
				long updateTime = lastUpdateTime - now;
				tickHandler.sleep(GAME_TICK_INTERVAL - updateTime);
			} else {
				// Wait the remaining time
				tickHandler.sleep(GAME_TICK_INTERVAL - (now - lastUpdateTime));
			}
		}
	}
	
	private void applyWeatherToMaze(Maze maze, CurrentWeatherData cwd) {
		TileType weatherTileToUse = TileType.Floor;
		if (cwd.getRain_Object().hasRain3Hours()) {
			weatherTileToUse = TileType.Rain;
		} else if (cwd.getMainData_Object().hasTemperature()) {
			if (cwd.getMainData_Object().getMinTemperature() <= 32) {
				weatherTileToUse = TileType.Ice;
			}
		}
		for (int x = 0; x < maze.width; x++) {
			for (int y = 0; y < maze.height; y++) {
				maze.setTileAt(x, y, weatherTileToUse);
			}
		}
	}
}
