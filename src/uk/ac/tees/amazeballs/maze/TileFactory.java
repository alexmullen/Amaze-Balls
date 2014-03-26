package uk.ac.tees.amazeballs.maze;

import java.util.EnumMap;

import android.graphics.drawable.Drawable;

/**
 * A factory class for creating tiles for a maze.
 * 
 * Alex Mullen (J9858839)
 *
 */
public class TileFactory {
	
	private static final EnumMap<TileType, Drawable> registeredTiles;
	
	static {
		registeredTiles = new EnumMap<TileType, Drawable>(TileType.class);
	}
	
	public static Tile createTile(TileType type) {
		Drawable tileImage = registeredTiles.get(type);
		if (tileImage == null) {
			return null;
		}
		
		switch (type) {
			case Floor:
				return new FloorTile(tileImage);
			default:
				return null;
		}
	}
	
	public static void registerTile(TileType type, Drawable image) {
		registeredTiles.put(type, image);
	}
}
