package uk.ac.tees.amazeballs.maze;

import java.util.EnumMap;

import android.graphics.drawable.Drawable;

/**
 * A factory class for creating tiles for a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class TileImageFactory {
	
	private static final EnumMap<TileType, Drawable> registeredTiles;
	
	static {
		registeredTiles = new EnumMap<TileType, Drawable>(TileType.class);
	}
	
	/**
	 * Creates and returns a new Tile instance of the requested type.
	 * 
	 * @param  the type the type of Tile to create
	 * @return the created tile or null if the requested tile type does not
	 * exist or the tile type was not previously registered with a call first
	 * to registerTile.
	 * 
	 * @see registerTile
	 */
	public static Drawable getImage(TileType type) {
		return registeredTiles.get(type);
	}
	
	/**
	 * Associates the specified Drawable with the specified TileType.
	 * 
	 * @param type  the type of tile
	 * @param image the image to associate with the specified tile type
	 */
	public static void registerImage(TileType type, Drawable image) {
		registeredTiles.put(type, image);
	}
}
