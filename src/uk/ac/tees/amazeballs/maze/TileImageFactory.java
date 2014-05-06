package uk.ac.tees.amazeballs.maze;

import java.util.EnumMap;

import android.graphics.drawable.Drawable;

/**
 * A factory class for holding tiles images.
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
	 * Creates and returns a new Drawable instance of the requested type of tile.
	 * 
	 * @param  the type the type of Tile to create
	 * @return the image or null if the requested tile type does not
	 * exist or the tile type was not previously registered with a call first
	 * to registerImage.
	 * 
	 * @see registerImage
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
