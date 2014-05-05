package uk.ac.tees.amazeballs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import uk.ac.tees.amazeballs.maze.Maze;
import android.content.Context;
import android.content.res.AssetManager;

public class LevelManager {

	public static String[] getLevels(Context context) {
		try {
			return context.getAssets().list("levels");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Maze getLevel(Context context, String levelname) {
		ObjectInputStream objInput = null;
		try {
			objInput = new ObjectInputStream(context.getAssets().open(levelname, AssetManager.ACCESS_STREAMING));
			Maze level = (Maze) objInput.readObject();
			objInput.close();
			return level;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String[] getCustomLevels(Context context) {
		return context.getFilesDir().list();
	}
	
	public static Maze getCustomLevel(Context context, String levelname) {
		ObjectInputStream objInput = null;
		try {
			objInput = new ObjectInputStream(context.openFileInput(levelname));
			Maze level = (Maze) objInput.readObject();
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
		}
		return null;
	}
	
	public static void saveCustomLevel(Context context, String levelname, Maze level) {
		ObjectOutputStream objOutput = null;
		try {
			objOutput = new ObjectOutputStream(context.openFileOutput(levelname, Context.MODE_PRIVATE));
			objOutput.writeObject(level);
			objOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean deleteCustomLevel(Context context, String levelname) {
		return context.deleteFile(levelname);
	}
	
}
