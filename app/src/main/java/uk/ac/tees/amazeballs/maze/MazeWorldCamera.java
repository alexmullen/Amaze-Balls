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
	public void moveLeft(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount > left) {
			amount = left;
		}
		left -= amount;
		right -= amount;
	}
	
	/**
	 * Move the camera up.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public void moveUp(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount > top) {
			amount = top;
		}
		top -= amount;
		bottom -= amount;
	}
	
	/**
	 * Move the camera right.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public void moveRight(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountToShift = Math.min(amount, (world.width - right));
		right += amountToShift;
		left += amountToShift;
	}
	
	/**
	 * Move the camera down.
	 * 
	 * @param amount the amount to move
	 * @return the amount moved
	 */
	public void moveDown(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountToShift = Math.min(amount, (world.height - bottom));
		bottom += amountToShift;
		top += amountToShift;
	}


	
	
	public void zoomIn(int amount) {	// !!!!!!
		left += Math.min(amount, (right - left) - 25);
		top += Math.min(amount, (bottom - top) - 25);
		right -= Math.min(amount, (right - left) - 25);
		bottom -= Math.min(amount, (bottom - top) - 25);
	}
	
	public void zoomOut(int amount) {
		left -= Math.min(amount, left);
		top -= Math.min(amount, top);
		right += Math.min(amount, (world.width - right));
		bottom += Math.min(amount, (world.height - bottom));
	}
	
	
	
	/**
	 * Gets the visible grid coordinate range that is within the borders of 
	 * the viewed section. For performance, no new object is created.
	 * 
	 * @param outRange an array passed in that will contain the values on method exit.
	 */
	public void getVisibleRange(int[] outRange) {
		final int tilesize = world.tilesize;
		outRange[0] = (left / tilesize);
		outRange[1] = (top / tilesize);
		outRange[2] = Math.min((world.maze.width - 1), (right / tilesize));
		outRange[3] = Math.min((world.maze.height - 1), (bottom / tilesize));
	}
	
}
