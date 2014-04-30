package uk.ac.tees.amazeballs.maze;

/**
 * A class for representing a selection for a subsection of a Maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeSelection extends Maze {

	private int offset_x;
	private int offset_y;
	private int selectionWidth;
	private int selectionHeight;
	private Maze baseMaze;
	
	public MazeSelection(Maze baseMaze, int offset_x, int offset_y, int selectionWidth, int selectionHeight) {
		super(baseMaze.getWidth(), baseMaze.getHeight());
		
		this.baseMaze = baseMaze;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		
		// Set the selection size, clamping the size to the maximum size of the maze
		this.selectionWidth = Math.min(selectionWidth, baseMaze.getWidth());
		this.selectionHeight = Math.min(selectionHeight, baseMaze.getHeight());
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
	public Tile getTileAt(int x, int y) {
		return baseMaze.getTileAt((x + offset_x), (y + offset_y));
	}

	@Override
	public void setTileAt(int x, int y, Tile tile) {
		baseMaze.setTileAt((x + offset_x), (y + offset_y), tile);
	}

	@Override
	public boolean isTileAt(int x, int y) {
		return baseMaze.isTileAt((x + offset_x), (y + offset_y));
	}

	@Override
	public boolean isTileAtAnEdge(int x, int y) {
		return baseMaze.isTileAtAnEdge((x + offset_x), (y + offset_y));
	}

	/**
	 * Shifts the selection up if possible.
	 * 
	 * @param amount the number of spaces to shift up
	 */
	public void shiftUp(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= offset_y) {
			offset_y = 0;
		} else {
			offset_y -= amount;
		}
	}
	
	/**
	 * Shifts the selection down if possible.
	 * 
	 * @param amount the number of spaces to shift down
	 */
	public void shiftDown(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (super.getHeight() - (offset_y + selectionHeight));
		offset_y += Math.min(amount, amountRemaining);
	}
	
	/**
	 * Shifts the selection left if possible.
	 * 
	 * @param amount the number of spaces to shift left
	 */
	public void shiftLeft(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= offset_x) {
			offset_x = 0;
		} else {
			offset_x -= amount;
		}
	}
	
	/**
	 * Shifts the selection right if possible.
	 * 
	 * @param amount the number of spaces to shift right
	 */
	public void shiftRight(int amount) {
		// Shift by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (super.getWidth() - (offset_x + selectionWidth));
		offset_x += Math.min(amount, amountRemaining);
	}

}
