package uk.ac.tees.amazeballs.views;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MazeSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	public MazeSurfaceView(Context context) {
		super(context);
		this.getHolder().addCallback(this);
	}
	
	@Override
	public void run() {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		
	}



}
