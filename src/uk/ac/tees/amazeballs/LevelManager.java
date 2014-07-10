package uk.ac.tees.amazeballs;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import uk.ac.tees.amazeballs.maze.MazeNew;
import android.content.Context;
import android.content.res.AssetManager;


/**
 * A static helper class for retrieving, loading and saving levels.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class LevelManager {

	/**
	 * Gets the list of levels names from the games assets (included with the game).
	 * 
	 * @param context the game application context
	 * @return the level names
	 */
	public static String[] getLevels(Context context) {
		try {
			return context.getAssets().list("levels");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads a level included with the game from the games assets (included with the game).
	 * 
	 * @param context the game application context
	 * @param levelname the name of the level to load
	 * @return the Maze instance of the level or null if it could not be loaded for some reason
	 */
	public static MazeNew loadLevel(Context context, String levelname) {
		InputStream assetInput = null;
		ObjectInputStream objInput = null;
		try {
			assetInput = context.getAssets().open("levels/" + levelname, AssetManager.ACCESS_STREAMING);
			objInput = new ObjectInputStream(assetInput);
			MazeNew level = (MazeNew) objInput.readObject();
			objInput.close();
			return level;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objInput != null) {
				closeQuietly(objInput);
			} else if (assetInput != null) {
				closeQuietly(assetInput);
			}
		}
		return null;
	}
	
	/**
	 * Gets the list of custom level names that the user saved.
	 * 
	 * @param context the game application context
	 * @return the level names
	 */
	public static String[] getCustomLevels(Context context) {
		return context.getFilesDir().list();
	}
	
	/**
	 * Loads a custom level (not included with the game).
	 * 
	 * @param context the game application context
	 * @param levelname the name of the level to load
	 * @return the Maze instance of the level or null if it could not be loaded for some reason
	 */
	public static MazeNew loadCustomLevel(Context context, String levelname) {
		FileInputStream fileInput = null;
		ObjectInputStream objInput = null;
		try {
			fileInput = context.openFileInput(levelname);
			objInput = new ObjectInputStream(fileInput);
			MazeNew level = (MazeNew) objInput.readObject();
			objInput.close();
			return level;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (objInput != null) {
				closeQuietly(objInput);
			} else if (fileInput != null) {
				closeQuietly(fileInput);
			}
		}
		return null;
	}
	
	
	
	/**
	 * Saves a custom level the user has created.
	 * 
	 * @param context the game application context
	 * @param levelname the name of the level
	 * @param level the level
	 */
	public static void saveCustomLevel(Context context, String levelname, MazeNew level) {
		FileOutputStream fileOutput = null;
		ObjectOutputStream objOutput = null;
		try {
			fileOutput = context.openFileOutput(levelname, Context.MODE_PRIVATE);
			objOutput = new ObjectOutputStream(fileOutput);
			objOutput.writeObject(level);
			objOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objOutput != null) {
				closeQuietly(objOutput);
			} else if (fileOutput != null) {
				closeQuietly(fileOutput);
			}
		}
	}
	
	/**
	 * Deletes a custom level.
	 * 
	 * @param context the game application context
	 * @param levelname the name of the level to delete
	 * @return true if the level was successfully deleted; else false
	 */
	public static boolean deleteCustomLevel(Context context, String levelname) {
		return context.deleteFile(levelname);
	}
	
	/**
	 * A convenience method for closing a Closeable resource and printing any
	 * Exception that may occur to System.err.
	 * 
	 * This is used to avoid the try-catch boiler plate code.
	 * 
	 * @param closeable the Closeable to close
	 */
	private static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
