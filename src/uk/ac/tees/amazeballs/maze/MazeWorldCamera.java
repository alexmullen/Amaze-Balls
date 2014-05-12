package uk.ac.tees.amazeballs.maze;


/**
 * Represents a camera looking at a part of a MazeWorld.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeWorldCamera {

	public final MazeWorld world;
	
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	public MazeWorldCamera(MazeWorld world, int left, int top, int right, int bottom) {
		this.world = world;
		this.left = Math.max(0, left);
		this.top = Math.max(0, top);
		this.right = Math.min(world.width, right);
		this.bottom = Math.min(world.height, bottom);
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getRight() {
		return right;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public int getWidth() {
		return (right - left);
	}
	
	public int getHeight() {
		return (bottom - top);
	}
	
	/**
	 * Move the camera left.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public int moveLeft(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= left) {
			amount = left;
			left = 0;
			right -= amount;
		} else {
			left -= amount;
			right -= amount;
		}
		return amount;
	}
	
	/**
	 * Move the camera up.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public int moveUp(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= top) {
			amount = top;
			top = 0;
			bottom -= amount;
		} else {
			top -= amount;
			bottom -= amount;
		}
		return amount;
	}
	
	/**
	 * Move the camera right.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public int moveRight(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountToShift = Math.min(amount, (world.width - right));
		right += amountToShift;
		left += amountToShift;
		return amountToShift;
	}
	
	/**
	 * Move the camera down.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public int moveDown(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountToShift = Math.min(amount, (world.height - bottom));
		bottom += amountToShift;
		top += amountToShift;
		return amountToShift;
	}

	/**
	 * Gets the visible grid coordinate range that is within the borders of 
	 * the viewed section. For performance, no new object is created.
	 * 
	 * @param outRange an array passed in that will contain the values on method exit.
	 */
	public void getVisibleRange(int[] outRange) {
		outRange[0] = (left / world.tilesize);
		outRange[1] = (top / world.tilesize);
		outRange[2] = Math.min((world.maze.width - 1), (right / world.tilesize));
		outRange[3] = Math.min((world.maze.height - 1), (bottom / world.tilesize));
	}

}
