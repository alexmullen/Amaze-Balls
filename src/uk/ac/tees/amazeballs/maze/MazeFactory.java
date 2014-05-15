package uk.ac.tees.amazeballs.maze;

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
	public static Maze createBorderedMaze(int width, int height) {
		Maze borderedMaze = new Maze(width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Check if the current position is at an edge
				if (borderedMaze.isTileAtAnEdge(x, y)) {
					// Position is at an edge so set it to be a wall tile
					borderedMaze.grid[x][y] = TileType.Wall;
				} else {
					// Position is not an edge so fill it with a floor tile
					borderedMaze.grid[x][y] = TileType.Floor;
				}
			}
		}
		return borderedMaze;
	}
	
}
