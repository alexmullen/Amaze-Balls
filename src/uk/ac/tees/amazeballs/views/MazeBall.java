package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.BallTile;

public class MazeBall {
	
	public int position_x;
	public int position_y;
	public BallTile image;
	public float imageRelativeSize;
	
	public MazeBall(int x, int y, BallTile image, float imageRelativeSize) {
		this.position_x = x;
		this.position_y = y;
		this.image = image;
		this.imageRelativeSize = imageRelativeSize;
	}
}
