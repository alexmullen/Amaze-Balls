package uk.ac.tees.amazeballs.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


/**
 * A custom implementation of a View for displaying a maze and a ball.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeBallView extends MazeView {
	
	public static class Ball {
		public int position_x;
		public int position_y;
		public Drawable image;
		public float imageRelativeSize;
		
		public Ball() {
			
		}
		
		public Ball(int x, int y, Drawable image, float imageRelativeSize) {
			this.position_x = x;
			this.position_y = y;
			this.image = image;
			this.imageRelativeSize = imageRelativeSize;
		}
	}
	
	private Ball ball;
	

	public MazeBallView(Context context, AttributeSet attrs) {
		// Constructor used when this view is inflated from XML.
		super(context, attrs);
	}
	
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public Ball getBall() {
		return ball;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int ballOffset_x = gridOffset_x + ball.position_x;
		int ballOffset_y = gridOffset_y + ball.position_y;
		int ballSize = (int)(tileSize * ball.imageRelativeSize);
		
		ball.image.setBounds(
				ballOffset_x, // left
				ballOffset_y, // top
				ballOffset_x + ballSize,  // right
				ballOffset_y + ballSize); // bottom

		ball.image.draw(canvas);
	}

}
