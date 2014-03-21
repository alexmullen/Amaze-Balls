package uk.ac.tees.amazeballs.maze;

/**
 * A class for representing a maze.
 * 
 * The grid coordinates for a maze start at (0,0) in the top left corner
 * with the X axis going across and the Y axis down like this.
 * 
 *       0  1  2  3  4 -------> X
 * 		+--------------+
 * 	  0 |  |  |  |  |  |
 * 		+--+--+--+--+--+
 * 	  1 |  |  |  |  |  |
 * 		+--+--+--+--+--+
 * 	  2 |  |  |  |  |  |
 * 		+--+--+--+--+--+
 *    3 |  |  |  |  |  |
 * 		+--+--+--+--+--+
 *    4 |  |  |  |  |  |
 *      +--+--+--+--+--+
 *    |
 *    |
 *    |
 *    |
 *    Y
 *    
 * The above example represent a maze of width 5 and height 5 (5x5)
 * with each axis beginning at 0.
 * 
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class Maze {

	private final int width;
	private final int height;
	private final Tile[][] grid;

	/**
	 * Instantiates a new empty maze of the specified dimensions.
	 * 
	 * @param width  the width of the maze
	 * @param height the height of the maze
	 */
	public Maze(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new Tile[width][height];
	}
	
	/**
	 * Returns the width of this maze (The number of tiles across).
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of this maze (The number of tiles down).
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the tile instance at the specified coordinate in this
	 * maze.
	 * 
	 * @param x the x position (across)
	 * @param y the y position (down)
	 * @return the tile at that location or null if there is no tile
	 */
	public Tile getTileAt(int x, int y) {
		return grid[x][y];
	}
	
	/**
	 * Sets the tile instance for the specified coordinates in this
	 * maze.
	 * 
	 * @param x the x position (across)
	 * @param y the y position (down)
	 * @param tile the tile to place at the specified coordinates
	 */
	public void setTileAt(int x, int y, Tile tile) {
		grid[x][y] = tile;
	}
}
