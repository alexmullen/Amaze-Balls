package uk.ac.tees.amazeballs.maze;

import android.graphics.Point;


/**
 * A class that represents a Maze in 2D space.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeWorld {

	private final Maze maze;
	private final int tilesize;
	private final int width;
	private final int height;
	
	public MazeWorld(Maze maze, int tilesize) {
		this.maze = maze;
		this.tilesize = tilesize;
		width = maze.getWidth() * tilesize;
		height = maze.getHeight() * tilesize;
	}
	
	public Maze getMaze() {
		return maze;
	}
	
	public int getTilesize() {
		return tilesize;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Point getGridCoords(int x, int y) {
		return new Point((int)(x / tilesize), (int)(y / tilesize));
	}
	
	public Point getWorldCoords(int gridX, int gridY) {
		return new Point(gridX * tilesize, gridY * tilesize);
	}

}
