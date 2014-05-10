package uk.ac.tees.amazeballs.maze;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

public class MazeWorldCamera {

	private final MazeWorld world;
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	public MazeWorldCamera(MazeWorld world, int left, int top, int right, int bottom) {
		this.world = world;
		this.left = Math.max(0, left);
		this.top = Math.max(0, top);
		this.right = Math.min(world.getWidth(), right);
		this.bottom = Math.min(world.getHeight(), bottom);
	}
	
	public MazeWorld getWorld() {
		return world;
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
	
	public int moveRight(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (world.getWidth() - right);
		int amountToShift = Math.min(amount, amountRemaining);
		right += amountToShift;
		left += amountToShift;
		return amountToShift;
	}
	
	public int moveDown(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (world.getHeight() - bottom);
		int amountToShift = Math.min(amount, amountRemaining);
		bottom += amountToShift;
		top += amountToShift;
		return amountToShift;
	}
	
	public List<Point> getVisibleTiles() {
		ArrayList<Point> visTileCoords = new ArrayList<Point>();
//		for (int x = left; x <= getWidth(); x += world.getTilesize()) {
//			for (int y = top; y <= getHeight(); y += world.getTilesize()) {
//				Point gridCoords = world.getGridCoords(x, y);
//				if (world.getMaze().isTileAt(gridCoords.x, gridCoords.y)) {
//					visTileCoords.add(gridCoords);
//				}
//			}
//		}
		for (int x = 0; x < world.getMaze().getWidth(); x++) {
			for (int y = 0; y < world.getMaze().getHeight(); y++) {
				visTileCoords.add(new Point(x, y));
			}
		}
		return visTileCoords;
	}

}
