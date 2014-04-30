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
	
	public MazeSelection(Maze baseMaze, int offset_x, int offset_y,
			int selectionWidth, int selectionHeight) {
		
		super(baseMaze.getWidth(), baseMaze.getHeight());
		
		this.baseMaze = baseMaze;
		this.offset_x = offset_x;
		this.offset_y = offset_y;
		this.selectionWidth = selectionWidth;
		this.selectionHeight = selectionHeight;
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
		return true;
//		if ( 
//			(x + offset_x) >= selectionWidth || 
//			(y + offset_y) >= selectionHeight) {
//			return false;
//		} else {
//			return true;
//		}
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
		
		int distanceFromEdge = super.getHeight() - (offset_y + selectionHeight + amount);
		offset_y += Math.min(distanceFromEdge, amount);
		
//		if ((offset_y + selectionHeight + amount) <= super.getHeight()) {
//			offset_y += amount;
//		}
	}
	
	/**
	 * Shifts the selection left if possible.
	 * 
	 * @param amount the number of spaces to shift left
	 */
	public void shiftLeft(int amount) {
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
		if ((offset_x + selectionWidth + amount) <= super.getWidth()) {
			offset_x += amount;
		}
	}

}
