package uk.ac.tees.amazeballs.maze;

/**
 * A class for representing a selection for a subsection of a Maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeSelection extends Maze {

	private static final long serialVersionUID = 1L;
	
	private int offset_x;
	private int offset_y;
	private int selectionWidth;
	private int selectionHeight;
	private Maze underlyingMaze;
	
	public MazeSelection(Maze maze, int offset_x, int offset_y, int selectionWidth, int selectionHeight) {
		super(maze.width, maze.height);
		
		this.underlyingMaze = maze;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		
		// Set the selection size, clamping the size to the maximum size of the maze
		this.selectionWidth = Math.min(selectionWidth, maze.width);
		this.selectionHeight = Math.min(selectionHeight, maze.height);
	}
	
	/**
	 * Returns the underlying maze this MazeSelection is using.
	 * 
	 * @return the full maze
	 */
	public Maze getUnderlyingMaze() {
		return underlyingMaze;
	}
	
	@Override
	public int getWidth() {
		// We only return the selection width.
		return selectionWidth;
	}

	@Override
	public int getHeight() {
		// We only return the selection height.
		return selectionHeight;
	}

	@Override
	public TileType getTileAt(int x, int y) {
		return underlyingMaze.getTileAt((x + offset_x), (y + offset_y));
	}

	@Override
	public void setTileAt(int x, int y, TileType tile) {
		underlyingMaze.setTileAt((x + offset_x), (y + offset_y), tile);
	}

//	@Override
//	public boolean isTileAt(int x, int y) {
//		return baseMaze.isTileAt((x + offset_x), (y + offset_y));
//	}

	@Override
	public boolean isTileAtAnEdge(int x, int y) {
		return underlyingMaze.isTileAtAnEdge((x + offset_x), (y + offset_y));
	}

	/**
	 * Shifts the selection up if possible.
	 * 
	 * @param amount the number of spaces to shift up
	 * @return the number of spaces actually shifted
	 */
	public int shiftUp(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= offset_y) {
			amount = offset_y;
			offset_y = 0;
		} else {
			offset_y -= amount;
		}
		return amount;
	}
	
	/**
	 * Shifts the selection down if possible.
	 * 
	 * @param amount the number of spaces to shift down
	 * @return the number of spaces actually shifted
	 */
	public int shiftDown(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (super.height - (offset_y + selectionHeight));
		int amountToShift = Math.min(amount, amountRemaining);
		offset_y += amountToShift;
		return amountToShift;
	}
	
	/**
	 * Shifts the selection left if possible.
	 * 
	 * @param amount the number of spaces to shift left
	 * @return the number of spaces actually shifted
	 */
	public int shiftLeft(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= offset_x) {
			amount = offset_x;
			offset_x = 0;
		} else {
			offset_x -= amount;
		}
		return amount;
	}
	
	/**
	 * Shifts the selection right if possible.
	 * 
	 * @param amount the number of spaces to shift right
	 * @return the number of spaces actually shifted
	 */
	public int shiftRight(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (super.width - (offset_x + selectionWidth));
		int amountToShift = Math.min(amount, amountRemaining);
		offset_x += amountToShift;
		return amountToShift;
	}
	
	
	public int expandUp(int amount) {
		int amountToExpand = Math.min(amount, offset_y);
		offset_y -= amountToExpand;
		selectionHeight += amountToExpand;
		return amountToExpand;
	}
	
	public int expandDown(int amount) {
		int amountRemaining = (super.height - (offset_y + selectionHeight));
		int amountToExpand = Math.min(amount, amountRemaining);
		selectionHeight += amountToExpand;
		return amountToExpand;
	}
	
	public int expandRight(int amount) {
		int amountRemaining = (super.width - (offset_x + selectionWidth));
		int amountToExpand = Math.min(amount, amountRemaining);
		selectionWidth += amountToExpand;
		return amountToExpand;
	}
	
	public int expandLeft(int amount) {
		int amountToExpand = Math.min(amount, offset_x);
		offset_x -= amountToExpand;
		selectionWidth += amountToExpand;
		return amountToExpand;
	}
	
	
	public int contractUp(int amount) {
		int amountToContract = Math.min(amount, (selectionHeight - 1));
		selectionHeight -= amountToContract;
		return amountToContract;
	}
	
	public int contractDown(int amount) {
		int amountToContract = Math.min(amount, (selectionHeight - 1));
		offset_y += amountToContract;
		selectionHeight -= amountToContract;
		return amountToContract;
	}
	
	public int contractLeft(int amount) {
		int amountToContract = Math.min(amount, (selectionWidth - 1));
		selectionWidth -= amountToContract;
		return amountToContract;
	}
	
	public int contractRight(int amount) {
		int amountToContract = Math.min(amount, (selectionWidth - 1));
		offset_x += amountToContract;
		selectionWidth -= amountToContract;
		return amountToContract;
	}
	
}
