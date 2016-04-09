package uk.ac.tees.amazeballs.maze;

import java.util.Random;


/**
 * A factory class for creating pre-generated maze templates.
 * 
 * @author Alex Mullen (j9858839)
 *
 */
public class MazeFactory {

	/**
	 * Creates a maze of the specified width and height were each tile at the
	 * border is a wall tile and every other is a floor tile.
	 * 
	 * @param width  the width of the maze to generate
	 * @param height the height of the maze to generate
	 * @return the generated maze
	 */
	public static MazeNew createBorderedMaze(int width, int height) {
		MazeNew borderedMaze = new MazeNew(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Check if the current position is at an edge
				if (borderedMaze.isTileAtAnEdge(x, y)) {
					// Position is at an edge so set it to be a wall tile
					borderedMaze.grid[y * width + x] = MazeNew.WALL_TILE;
				} else {
					// Position is not an edge so fill it with a floor tile
					borderedMaze.grid[y * width + x] = MazeNew.FLOOR_TILE;
				}
			}
		}
		return borderedMaze;
	}
	
	public static MazeNew createGeneratedMaze(int width, int height) {
		MazeNew randomBorderedMaze = createBorderedMaze(width, height);
		
		Random rng = new Random();
		int startX = rng.nextInt((int)(width * 0.25));
		int startY = rng.nextInt((int)(height * 0.25));
		int endX = rng.nextInt(width);
		int endY = rng.nextInt(height);
		
		randomBorderedMaze.grid[startY * width + startX] = MazeNew.START_TILE;
		randomBorderedMaze.grid[endY * width + endX] = MazeNew.GOAL_TILE;
		
		return randomBorderedMaze;
	}
	
	private static int distanceBetween(int x1, int y1, int x2, int y2) {
		int xd = x2 - x1;
		int yd = y2 - y1;
		return (int) Math.sqrt((xd * xd) + (yd * yd));
	}
	
}
