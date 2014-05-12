package uk.ac.tees.amazeballs.maze;

import android.graphics.Point;


/**
 * A class that represents a Maze in 2D space.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeWorld {
	
	public static class Ball {
		public int position_x;
		public int position_y;
		public int size;
		public Ball(int position_x, int position_y, int size) {
			this.position_x = position_x;
			this.position_y = position_y;
			this.size = size;
		}
	}

	public final Maze maze;
	public final int tilesize;
	public final int width;
	public final int height;
	
	public Ball ball;
	
	public MazeWorld(Maze maze, int tilesize) {
		this.maze = maze;
		this.tilesize = tilesize;
		width = maze.width * tilesize;
		height = maze.height * tilesize;
	}

	/**
	 * Converts world coordinates into grid coordinates.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the grid coordinates
	 */
	public Point getGridCoords(int x, int y) {
		return new Point((int)(x / tilesize), (int)(y / tilesize));
	}
	
	/**
	 * Converts grid coordinates into world coordinates.
	 * 
	 * @param gridX the x grid coordinate
	 * @param gridY the y grid coordinate
	 * @return the world coordinates
	 */
	public Point getWorldCoords(int gridX, int gridY) {
		return new Point(gridX * tilesize, gridY * tilesize);
	}
	
	/**
	 * Converts grid coordinates into world coordinates but does not
	 * create a new Point object for reducing object creation in the
	 * performance critical game loop.
	 * 
	 * @param gridX the x grid coordinate
	 * @param gridY the y grid coordinate
	 * @param outPoint the Point instance passed in that will contain the world coordinates
	 * when the method has finished executing.
	 */
	public void getWorldCoords(int gridX, int gridY, Point outPoint) {
		outPoint.x = (gridX * tilesize);
		outPoint.y = (gridY * tilesize);
	}
}
