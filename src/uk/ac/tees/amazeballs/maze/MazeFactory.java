package uk.ac.tees.amazeballs.maze;

/**
 * A factory class for creating pre-generated maze templates.
 * 
 * @author Alex Mullen (j9858839)
 *
 */
public class MazeFactory {

	/**
	 * Creates a maze of the specified width and height were each tile is a
	 * floor tile.
	 * 
	 * @param width	 the width of the maze to generate
	 * @param height the height of the maze to generate
	 * @return the generated maze
	 */
	public static Maze createBlankMaze(int width, int height) {
		Maze blankMaze = new Maze(width, height);
		for (int x = 0; x < blankMaze.getWidth(); x++) {
			for (int y = 0; y < blankMaze.getHeight(); y++) {
				blankMaze.setTileAt(x, y, TileFactory.createTile(TileType.Floor));
			}
		}
		return blankMaze;
	}
	
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
		for (int x = 0; x < borderedMaze.getWidth(); x++) {
			for (int y = 0; y < borderedMaze.getHeight(); y++) {
				// Check if the current position is at an edge
				if ((x == 0 || x == (width - 1)) || (y == 0 || y == (height - 1))) {
					// Position is at an edge so set it to be a wall tile
					borderedMaze.setTileAt(x, y, TileFactory.createTile(TileType.Wall));
				} else {
					// Position is not an edge so fill it with a floor tile
					borderedMaze.setTileAt(x, y, TileFactory.createTile(TileType.Floor));
				}
			}
		}
		return borderedMaze;
	}
	
}
