package uk.ac.tees.amazeballs;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileImageFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.views.MazeBallView;
import uk.ac.tees.amazeballs.views.MazeBallView.Ball;


/**
 * A controller class for managing a ball in a maze.
 * 
 * @author Alex Mullen (J9858839)
 *
 */
public class GameController {
	
	private class TouchedTile {
		public int x;
		public int y;
		public TileType type;
		public TouchedTile(int x, int y, TileType type) {
			this.x = x;
			this.y = y;
			this.type = type;
		}
		@Override
		public boolean equals(Object o) {
			TouchedTile tt = (TouchedTile) o;
			return this.x == tt.x &&
				   this.y == tt.y;
		}
		
	}
	
	private final static int NORMAL_BALL_SPEED = 5;
	private final static int GRID_SCROLLING_AMOUNT = 1;
	private final static double TILT_SENSITIVITY = 0.75;
	
	public volatile float lastAccelerometerReading_x;
	public volatile float lastAccelerometerReading_y;
	
	private final Ball ball;
	private final MazeBallView view;
	private final MazeSelection mazeSelection;
	
	private int ballSpeed = NORMAL_BALL_SPEED;
	private boolean finished;
	
	public GameController(MazeSelection maze, MazeBallView mazeView) {
		this.mazeSelection = maze;
		this.view = mazeView;
		this.ball = new MazeBallView.Ball();
	}
	
	/**
	 * Simulates one tick in the ball simulation where gravity and collisions are
	 * applied.
	 */
	public void update() {
		
		if (view.getBall() == null) {
			ball.image = TileImageFactory.getImage(TileType.Ball);
			ball.imageRelativeSize = 0.8f;
			
			Point startPosition = findStartPosition();
			int ballSize = (int)(view.getTilesize() * ball.imageRelativeSize);
			int ballStartOffset = (view.getTilesize() - ballSize) / 2;
			
			ball.position_x = startPosition.x * view.getTilesize() + ballStartOffset;
			ball.position_y = startPosition.y * view.getTilesize() + ballStartOffset;
			view.setBall(ball);
		}
		

		moveBall();

		scrollScreen();
		
		handleTouchedTiles();

		// Update the view
		view.invalidate();
	}
	
	private void moveBall() {
		// Get the accelerometer reading
		if (lastAccelerometerReading_y > TILT_SENSITIVITY) {
			ball.position_y += ballSpeed;		
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_y--;
			}
		} 
		if (lastAccelerometerReading_y < -TILT_SENSITIVITY) {
			ball.position_y -= ballSpeed;		
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_y++;
			}
		} 
		if (lastAccelerometerReading_x > TILT_SENSITIVITY) {
			ball.position_x -= ballSpeed;
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_x++;
			}
		}
		if (lastAccelerometerReading_x < -TILT_SENSITIVITY) {
			ball.position_x += ballSpeed;
			// Resolve collisions
			while (ballHasCollided()) {
				ball.position_x--;
			}
		}
	}
	
	private void scrollScreen() {
		// Potentially scroll the screen down
		int mazeAreaHeight = view.getTilesize() * mazeSelection.getHeight();
		if ((double)ball.position_y / mazeAreaHeight >= 0.75) {
			int amountShiftedDown = mazeSelection.shiftDown(GRID_SCROLLING_AMOUNT);
			ball.position_y -= (amountShiftedDown * view.getTilesize());
		}
		
		// Potentially scroll the screen up
		if ((double)ball.position_y / mazeAreaHeight <= 0.25) {
			int amountShiftedUp = mazeSelection.shiftUp(GRID_SCROLLING_AMOUNT);
			ball.position_y += (amountShiftedUp * view.getTilesize());
		}
		
		// Potentially scroll the screen right
		int mazeAreaWidth = view.getTilesize() * mazeSelection.getWidth();
		if ((double)ball.position_x / mazeAreaWidth >= 0.75) {
			int amountShiftedRight = mazeSelection.shiftRight(GRID_SCROLLING_AMOUNT);
			ball.position_x -= (amountShiftedRight * view.getTilesize());
		}
		
		// Potentially scroll the screen left
		if ((double)ball.position_x / mazeAreaWidth <= 0.25) {
			int amountShiftedLeft = mazeSelection.shiftLeft(GRID_SCROLLING_AMOUNT);
			ball.position_x += (amountShiftedLeft * view.getTilesize());
		}
	}
	
	private void handleTouchedTiles() {
		// Check for the ball touching any special blocks that we need to handle
		List<TouchedTile> touchingTiles = getTouchingTiles();
		//Log.d(getClass().getName(), String.valueOf(touchingTiles.size()));
		boolean touchingRain = false;
		boolean touchingIce = false;
		for (TouchedTile currentTouchedTile : touchingTiles) {
			if (currentTouchedTile.type == TileType.Chest) {
				mazeSelection.setTileAt(currentTouchedTile.x, currentTouchedTile.y, TileType.Floor);
			}
			if (currentTouchedTile.type == TileType.Goal) {
				// End the game
				finished = true;
			}
			if (currentTouchedTile.type == TileType.Ice) {
				touchingIce = true;
			}
			if (currentTouchedTile.type == TileType.Key) {
				mazeSelection.setTileAt(currentTouchedTile.x, currentTouchedTile.y, TileType.Floor);
			}
			if (currentTouchedTile.type == TileType.Penalty) {
				// Do the penalty
			}
			if (currentTouchedTile.type == TileType.Rain) {
				touchingRain = true;
			}
		}
		ballSpeed = (touchingRain ? 1 : NORMAL_BALL_SPEED);
		ballSpeed = (touchingIce ? 15 : NORMAL_BALL_SPEED);		
	}
	
	private List<TouchedTile> getTouchingTiles() {
		TouchedTile tile;
		ArrayList<TouchedTile> touchedTiles = new ArrayList<TouchedTile>(4);
		int gridPositionTouchedX;
		int gridPositionTouchedY;
		int ballSize = (int)(view.getTilesize() * ball.imageRelativeSize);
		int tileSize = view.getTilesize();
		
		// Check top left corner of ball
		gridPositionTouchedX = (int)(ball.position_x) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y) / tileSize;
		tile = new TouchedTile(gridPositionTouchedX, gridPositionTouchedY, 
				mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY));
		touchedTiles.add(tile);
		
		// Check top right corner of ball
		gridPositionTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y) / tileSize;
		tile = new TouchedTile(gridPositionTouchedX, gridPositionTouchedY, 
				mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		// Check bottom right corner of ball
		gridPositionTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		tile = new TouchedTile(gridPositionTouchedX, gridPositionTouchedY, 
				mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		// Check bottom left corner of ball
		gridPositionTouchedX = (int)(ball.position_x) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		tile = new TouchedTile(gridPositionTouchedX, gridPositionTouchedY, 
				mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		return touchedTiles;
	}
	
	private boolean ballHasCollided() {
		int gridPositionTouchedX;
		int gridPositionTouchedY;
		int ballSize = (int)(view.getTilesize() * ball.imageRelativeSize);
		int tileSize = view.getTilesize();
		
		// Check top left corner of ball
		gridPositionTouchedX = (int)(ball.position_x) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y) / tileSize;
		if (mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// Check top right corner of ball
		gridPositionTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y) / tileSize;
		if (mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// Check bottom right corner of ball
		gridPositionTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		if (mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// Check bottom left corner of ball
		gridPositionTouchedX = (int)(ball.position_x) / tileSize;
		gridPositionTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		if (mazeSelection.getTileAt(gridPositionTouchedX, gridPositionTouchedY) == TileType.Wall) {
			return true;
		}
		
		// No collisions
		return false;
	}
	
	private Point findStartPosition() {
		Point firstEmptySpace = null;
		for (int y = 0; y < mazeSelection.getHeight(); y++) {
			for (int x = 0; x < mazeSelection.getWidth(); x++) {
				if (mazeSelection.getTileAt(x, y) == TileType.Start) {
					return new Point(x, y);
				} else if (firstEmptySpace == null && mazeSelection.getTileAt(x, y) == TileType.Floor) {
					firstEmptySpace = new Point(x, y);
				}
			}
		}
		return firstEmptySpace;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
