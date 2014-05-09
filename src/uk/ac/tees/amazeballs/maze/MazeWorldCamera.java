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
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
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
	
	public int moveLeft(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= left) {
			amount = left;
			left = 0;
			right -= left;
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
			bottom -= top;
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
		for (int x = left; x <= (right - left); x += world.getTilesize()) {
			for (int y = top; y <= (bottom - top); y += world.getTilesize()) {
				Point gridCoords = world.getGridCoords(x, y);
				if (world.getMaze().isTileAt(gridCoords.x, gridCoords.y)) {
					visTileCoords.add(gridCoords);
				}
			}
		}
		return visTileCoords;
	}

}
