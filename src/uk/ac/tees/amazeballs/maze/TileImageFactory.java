package uk.ac.tees.amazeballs.maze;

import android.graphics.drawable.Drawable;


/**
 * A factory class for holding tiles images.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class TileImageFactory {
	
	private static final Drawable[] registeredTileDrawables;
	
	static {
		registeredTileDrawables = new Drawable[16];
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
	public static Drawable getDrawable(int type) {
		return registeredTileDrawables[type];
	}

	/**
	 * Associates the specified Drawable with the specified TileType.
	 * 
	 * @param type  the type of tile
	 * @param image the image to associate with the specified tile type
	 */
	public static void registerImage(int type, Drawable image) {
		registeredTileDrawables[type] = image;
	}
}
