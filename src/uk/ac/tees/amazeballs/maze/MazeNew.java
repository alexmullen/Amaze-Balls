package uk.ac.tees.amazeballs.maze;

import java.io.Serializable;
import java.util.List;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class for representing a maze.
 * 
 * The grid coordinates for a maze start at (0,0) in the top left corner
 * with the X axis going across and the Y axis down like this.
 * 
 *       0  1  2  3  4 -------> X 	(width)
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
 * (height)
 *    
 * The above example represent a maze of width 5 and height 5 (5x5)
 * with each axis beginning at 0.
 * 
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeNew implements Parcelable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final int CHEST_TILE = 0;
	public static final int DOOR_TILE = 1;
	public static final int FLOOR_TILE = 2;
	public static final int GOAL_TILE = 3;
	public static final int ICE_TILE = 4;
	public static final int KEY_TILE = 5;
	public static final int PENALTY_TILE = 6;
	public static final int RAIN_TILE = 7;
	public static final int START_TILE = 8;
	public static final int WALL_TILE = 9;
	public static final int WEATHER_TILE = 10;
	public static final int BALL_TILE = 11;
	
	
	public final int width;
	public final int height;
	protected final int[] grid;

	
	/**
	 * Instantiates a new empty maze of the specified dimensions.
	 * 
	 * @param width  the width of the m15aze
	 * @param height the height of the maze
	 */
	public MazeNew(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new int[width * height];
	}
	
	private MazeNew(int width, int height, int[] grid) {
		this.width = width;
		this.height = height;
		this.grid = grid;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(width);
		out.writeInt(height);
		out.writeIntArray(grid);
	}
	
	public static final Parcelable.Creator<MazeNew> CREATOR = new Parcelable.Creator<MazeNew>() {
		@Override
		public MazeNew createFromParcel(Parcel source) {
			int width = source.readInt();
			int height = source.readInt();
			int grid[] = new int[width * height];
			source.readIntArray(grid);
			return new MazeNew(width, height, grid);
		}

		@Override
		public MazeNew[] newArray(int size) {
			return new MazeNew[size];
		}
	};
	
	/**
	 * Returns the tile instance at the specified coordinate in this
	 * maze.
	 * 
	 * @param x the x position (across)
	 * @param y the y position (down)
	 * @return the tile at that location or null if there is no tile
	 */
	public int getTileAt(int x, int y) {
		return grid[y * width + x];
	}
	
	/**
	 * Sets the tile instance for the specified coordinates in this
	 * maze.
	 * 
	 * @param x the x position (across)
	 * @param y the y position (down)
	 * @param tile the tile to place at the specified coordinates
	 */
	public void setTileAt(int x, int y, int tile) {
		grid[y * width + x] = tile;
	}
	
	/**
	 * Checks if the specified tile position is located at the border of 
	 * this maze.
	 * 
	 * @param x the x position (across)
	 * @param y the y position (down)
	 * @return true if the specified position is located at the border of
	 * this maze, otherwise false is returned
	 */
	public boolean isTileAtAnEdge(int x, int y) {
		return (x == 0 || x == (width - 1) || y == 0 || y == (height - 1));
	}

	/**
	 * Replaces each tile from a list of positions with the specified tile.
	 * 
	 * @param positions the list of positions to replace
	 * @param newTile the tile type to replace each position with
	 */
	public void replaceAllAt(List<Point> positions, int newTile) {
		for (Point p : positions) {
			grid[p.y * width + p.x] = newTile;
		}
	}
	
}