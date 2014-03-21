package uk.ac.tees.amazeballs.maze;

import android.graphics.drawable.Drawable;

/**
 * A class for representing a maze tile.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class Tile {

	private final Drawable image;
	
	/**
	 * Instantiates a new Tile with the specified image to
	 * represent it on screen.
	 * 
	 * @param image the image of this tile
	 */
	public Tile(Drawable image) {
		this.image = image;
	}
	
	/**
	 * Returns the image representing this tile.
	 * 
	 * @return the image
	 */
	public Drawable getImage() {
		return image;
	}
}
