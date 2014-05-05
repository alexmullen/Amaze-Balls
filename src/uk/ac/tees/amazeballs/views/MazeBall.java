package uk.ac.tees.amazeballs.views;

import android.graphics.drawable.Drawable;

public class MazeBall {
	
	public int position_x;
	public int position_y;
	public Drawable image;
	public float imageRelativeSize;
	
	public MazeBall(int x, int y, Drawable image, float imageRelativeSize) {
		this.position_x = x;
		this.position_y = y;
		this.image = image;
		this.imageRelativeSize = imageRelativeSize;
	}
}
