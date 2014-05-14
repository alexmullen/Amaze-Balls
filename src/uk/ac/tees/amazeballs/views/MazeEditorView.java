package uk.ac.tees.amazeballs.views;

import java.util.ArrayList;

import uk.ac.tees.amazeballs.R;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A custom implementation of a View for displaying a maze with grid lines and
 * the ability for users to interact with to design a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class MazeEditorView extends MazeView implements OnGestureListener, OnScaleGestureListener {

	private static final Paint LINE_PAINT;
	private static final ArrayList<SpecialTileChoice> SPECIAL_TILES;
	
	static {
		LINE_PAINT = new Paint();
		LINE_PAINT.setStyle(Style.STROKE);
		
		SPECIAL_TILES = new ArrayList<SpecialTileChoice>();
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Start, TileImageFactory.getImage(TileType.Start)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Goal, TileImageFactory.getImage(TileType.Goal)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Key, TileImageFactory.getImage(TileType.Key)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Door, TileImageFactory.getImage(TileType.Door)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Penalty, TileImageFactory.getImage(TileType.Penalty)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Chest, TileImageFactory.getImage(TileType.Chest)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Weather, TileImageFactory.getImage(TileType.Weather)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Ice, TileImageFactory.getImage(TileType.Ice)));
		SPECIAL_TILES.add(new SpecialTileChoice(TileType.Rain, TileImageFactory.getImage(TileType.Rain)));
	}
	
	private static class SpecialTileChoice {
		public TileType type;
		public Drawable image;
		public SpecialTileChoice(TileType type, Drawable image) {
			this.type = type;
			this.image = image;
		}
	}
	
	
	
	private final GestureDetector gestureDetector;
	private final ScaleGestureDetector scaleGestureDetector;
	
	
	public MazeEditorView(Context context, AttributeSet attrs) {
		// Constructor used when this view is inflated from XML.
		super(context, attrs);
		gestureDetector = new GestureDetector(context, this);
		scaleGestureDetector = new ScaleGestureDetector(context, this);
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// This event needs to be handled by the GestureDetector.
		gestureDetector.onTouchEvent(event);
		scaleGestureDetector.onTouchEvent(event);
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent event) {
		// This needs to return true for the GestureDetector to detect gestures.
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent event) {
		if (currentMaze == null) {
			return;
		}

		// Normalize the coordinates touched into grid coordinates.
		final int gridPositionTouchedX = (int) Math.floor(((event.getX() - gridOffset_x) / getTilesize()));
		final int gridPositionTouchedY = (int) Math.floor(((event.getY() - gridOffset_y) / getTilesize()));
		
		// Ignore any touches that are within our view area but outside the displayed grid.
		if (gridPositionTouchedX < 0 || gridPositionTouchedY < 0 || 
			gridPositionTouchedX >= currentMaze.getWidth() || gridPositionTouchedY >= currentMaze.getHeight()) {
			return;
		}
		
		// Prevent the edges of the maze being modified
		if (currentMaze.isTileAtAnEdge(gridPositionTouchedX, gridPositionTouchedY)) {
			return;
		}

		// Display a dialog of special blocks to choose from
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Choose a block");
        builder.setAdapter(new ArrayAdapter<SpecialTileChoice>(this.getContext(), R.layout.dialog_specialtile_row, SPECIAL_TILES) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
 
                View view = (convertView != null ? convertView :
                	LayoutInflater.from(getContext()).inflate(R.layout.dialog_specialtile_row, parent, false));
                
                ImageView imageView = (ImageView) view.findViewById(R.id.special_tile_choice_image);
                TextView textView = (TextView) view.findViewById(R.id.special_tile_choice_title);
                SpecialTileChoice specialTileChoice = getItem(position);
                imageView.setImageDrawable(specialTileChoice.image);
                textView.setText(specialTileChoice.type.name());
                return view;
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	currentMaze.setTileAt(gridPositionTouchedX, gridPositionTouchedY, SPECIAL_TILES.get(which).type);
        		invalidate();
            }
        });
        builder.create().show();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		if (currentMaze == null) {
			return true;
		}
		
		boolean invalidateNeeded = false;
		MazeSelection mazeSelection = (MazeSelection) currentMaze;

		if (distanceX < -5) {
			mazeSelection.shiftLeft(1);
			invalidateNeeded = true;
		}
		if (distanceX > 5) {
			mazeSelection.shiftRight(1);
			invalidateNeeded = true;
		}
		if (distanceY < -5) {
			mazeSelection.shiftUp(1);
			invalidateNeeded = true;
		}
		if (distanceY > 5) {
			mazeSelection.shiftDown(1);
			invalidateNeeded = true;
		}
		
		
		if (invalidateNeeded) {
			invalidate();
		}
		
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		if (currentMaze == null) {
			return true;
		}
		
		// Normalize the coordinates touched into grid coordinates.
		int gridPositionTouchedX = (int) Math.floor(((event.getX() - gridOffset_x) / getTilesize()));
		int gridPositionTouchedY = (int) Math.floor(((event.getY() - gridOffset_y) / getTilesize()));
		
		// Ignore any touches that are within our view area but outside the displayed grid.
		if (gridPositionTouchedX < 0 || gridPositionTouchedY < 0 || 
			gridPositionTouchedX >= currentMaze.getWidth() || gridPositionTouchedY >= currentMaze.getHeight()) {
			return true;
		}
		
		handleTileTouched(gridPositionTouchedX, gridPositionTouchedY);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		// For providing some form of feedback to the user.
	}
	
	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		if (currentMaze == null) {
			return true;
		}
		
		boolean invalidateNeeded = false;
		MazeSelection mazeSelection = (MazeSelection) currentMaze;
		
		if (detector.getScaleFactor() <= 1.0f) {
			mazeSelection.expandUp(1);
			mazeSelection.expandDown(1);
			mazeSelection.expandRight(1);
			mazeSelection.expandLeft(1);
			invalidateNeeded = true;
		} 
		if (detector.getScaleFactor() >= 1.0f) {
			mazeSelection.contractUp(1);
			mazeSelection.contractDown(1);
			mazeSelection.contractRight(1);
			mazeSelection.contractLeft(1);
			invalidateNeeded = true;
		}
		
		if (invalidateNeeded) {
			invalidate();
		}
		
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {

	}
	

	private void handleTileTouched(int x, int y) {
		// Prevent the edges of the maze being modified
		if (currentMaze.isTileAtAnEdge(x, y)) {
			return;
		}
		
		// Toggle the tile.
		if (currentMaze.getTileAt(x, y) == TileType.Floor) {
			currentMaze.setTileAt(x, y, TileType.Wall);
		} else {
			currentMaze.setTileAt(x, y, TileType.Floor);
		}
		
		// Repaint the view
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Don't draw a maze if there is no maze
		if (currentMaze == null) {
			return;
		}
		
		// Draw vertical grid lines
		for (int i = 0; i <= currentMaze.getWidth(); i++) {
			int current_x = (gridOffset_x + (tileSize * i));
			canvas.drawLine(
					current_x, // start x
					gridOffset_y, // start y
					current_x, // stop x
					(gridOffset_y + (currentMaze.getHeight() * tileSize)), // stop y
					LINE_PAINT); // paint
		}
		
		// Draw horizontal grid lines
		for (int i = 0; i <= currentMaze.getHeight(); i++) {
			int current_y = (gridOffset_y + (tileSize * i));
			canvas.drawLine(
					gridOffset_x, // start x
					current_y, // start y
					(gridOffset_x + (currentMaze.getWidth() * tileSize)), // stop x
					current_y, // stop y
					LINE_PAINT); // paint
		}
	}

}
