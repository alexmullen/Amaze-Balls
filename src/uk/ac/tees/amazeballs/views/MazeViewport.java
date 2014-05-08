package uk.ac.tees.amazeballs.views;

import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class MazeViewport extends View {

	private MazeWorldCamera camera;
	
	public MazeViewport(Context context) {
		super(context);
	}

	public MazeViewport(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MazeViewport(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public MazeWorldCamera getCamera() {
		return camera;
	}
	
	public void setCamera(MazeWorldCamera camera) {
		this.camera = camera;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
		
		
	}
	
}

/*

MazeLevel()
	getTileAt()

MazeWorld(MazeLevel, 10)
	getGridCoords()
	getWorldCoords()

MazeWorldCamera(MazeWorld, 100, 150)
	getVisibleTiles()

MazeViewport(MazeWorldCamera)




*/