package uk.ac.tees.amazeballs.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class MazeBallView extends MazeView {
	
	public class Ball {
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
	
	private final Ball ball;
	
	public MazeBallView(Context context) {
		super(context);
		ball = new Ball();
	}
	
	public MazeBallView(Context context, AttributeSet attrs) {
		super(context, attrs);
		ball = new Ball();
	}

	public Ball getBall() {
		return ball;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Drawable ballImage = ball.image;
		
		int ballOffset_x = gridOffset_x + ball.position_x;
		int ballOffset_y = gridOffset_y + ball.position_y;
		int ballSize = (int)(tileSize * ball.imageRelativeSize);
		
		ballImage.setBounds(
				ballOffset_x, // left
				ballOffset_y, // top
				ballOffset_x + ballSize,  // right
				ballOffset_y + ballSize); // bottom

		ballImage.draw(canvas);
	}

}
