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
	private final static int ICE_BALL_SPEED = 10;
	private final static int RAIN_BALL_SPEED = 1;
	private final static int GRID_SCROLLING_AMOUNT = 1;
	private final static double TILT_SENSITIVITY = 0.75;
	
	public volatile float lastAccelerometerReading_x;
	public volatile float lastAccelerometerReading_y;
	
	private final Ball ball;
	private final MazeBallView view;
	private final MazeSelection mazeSelection;
	
	private int ballSpeed = NORMAL_BALL_SPEED;
	private boolean finished;
	
	private int keys;
	
	public GameController(MazeSelection maze, MazeBallView mazeView) {
		this.mazeSelection = maze;
		this.view = mazeView;
		this.ball = new MazeBallView.Ball();
		
		ball.image = TileImageFactory.getImage(TileType.Ball);
		ball.imageRelativeSize = 0.8f;
		
		int ballSize = (int)(view.getTilesize() * ball.imageRelativeSize);
		int ballStartOffset = (view.getTilesize() - ballSize) / 2;
		
		Point startPosition = findStartPosition();
		ball.position_x = startPosition.x * view.getTilesize() + ballStartOffset;
		ball.position_y = startPosition.y * view.getTilesize() + ballStartOffset;
		
		view.setBall(ball);
	}
	
	/**
	 * Simulates one tick in the ball simulation where gravity and collisions are
	 * applied.
	 */
	public void update() {

		moveBall();

		scrollScreen();
		
		handleTouchedTiles();

		// Render the view
		view.invalidate();
	}
	
	/**
	 * Move the ball depending on the tilt of the device.
	 */
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
	
	/**
	 * Gets whether the ball has collided with a wall or door.
	 * 
	 * @return true if the ball intersects a wall or door tile by 1 pixel
	 * or more.
	 */
	private boolean ballHasCollided() {
		int gridPosTouchedX;
		int gridPosTouchedY;
		int ballSize = (int)(view.getTilesize() * ball.imageRelativeSize);
		int tileSize = view.getTilesize();
		TileType tileTouched;
		
		// Check top left corner of ball
		gridPosTouchedX = (int)(ball.position_x) / tileSize;
		gridPosTouchedY = (int)(ball.position_y) / tileSize;
		tileTouched = mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// Check top right corner of ball
		gridPosTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(ball.position_y) / tileSize;
		tileTouched = mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// Check bottom right corner of ball
		gridPosTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		tileTouched = mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// Check bottom left corner of ball
		gridPosTouchedX = (int)(ball.position_x) / tileSize;
		gridPosTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		tileTouched = mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// No collisions
		return false;
	}
	
	/**
	 * Scroll to another part of the maze if the ball is within a certain distance
	 * from the view edge.
	 */
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
	
	/**
	 * Handles what happens when certain tiles are touched.
	 */
	private void handleTouchedTiles() {
		boolean touchingRain = false;
		boolean touchingIce = false;
		
		// Get a the tiles the ball is currently touching
		List<TouchedTile> touchingTiles = getTouchingTiles();
		
		// Handle touching any special tiles we care about
		for (TouchedTile touchedTile : touchingTiles) {
			switch (touchedTile.type) {
				case Floor:
					// "Use" any picked up keys to unlock any doors near
					if ((keys > 0) && (isNextToADoor(touchedTile.x, touchedTile.y))) {
						List<Point> doors = getDoorsAround(touchedTile.x, touchedTile.y);
						for (Point d : doors) {
							if (keys > 0) {
								keys--;;
								mazeSelection.setTileAt(d.x, d.y, TileType.Floor);	
							} else {
								break;
							}
						}
					}
				case Chest:
					mazeSelection.setTileAt(touchedTile.x, touchedTile.y, TileType.Floor);
					break;				
				case Key:
					// Increment the number of keys we have then replace the key tile with a floor tile
					keys++;
					mazeSelection.setTileAt(touchedTile.x, touchedTile.y, TileType.Floor);
					break;
				case Goal:
					// End the game
					finished = true;
					return;
				case Ice:
					touchingIce = true;
					break;
				case Penalty:
					// Apply the penalty
					break;
				case Rain:
					touchingRain = true;
					break;
				default:
					// Ignore other tiles
					break;
			}
		}
		
		/*
		 * Change the ball speed if it is touching rain or ice. If it is touching both,
		 * we will let rain behaviour have the priority.
		 */
		if (touchingRain) {
			ballSpeed = RAIN_BALL_SPEED;
		} else if (touchingIce) {
			ballSpeed = ICE_BALL_SPEED;
		} else {
			ballSpeed = NORMAL_BALL_SPEED;
		}
	}
	
	private List<TouchedTile> getTouchingTiles() {
		TouchedTile tile;
		ArrayList<TouchedTile> touchedTiles = new ArrayList<TouchedTile>(4);
		int gridPosTouchedX;
		int gridPosTouchedY;
		int ballSize = (int)(view.getTilesize() * ball.imageRelativeSize);
		int tileSize = view.getTilesize();
		
		// Check top left corner of ball
		gridPosTouchedX = (int)(ball.position_x) / tileSize;
		gridPosTouchedY = (int)(ball.position_y) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY));
		touchedTiles.add(tile);
		
		// Check top right corner of ball
		gridPosTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(ball.position_y) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		// Check bottom right corner of ball
		gridPosTouchedX = (int)(ball.position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		// Check bottom left corner of ball
		gridPosTouchedX = (int)(ball.position_x) / tileSize;
		gridPosTouchedY = (int)(ball.position_y + ballSize) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeSelection.getTileAt(gridPosTouchedX, gridPosTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		return touchedTiles;
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
	
	public boolean isNextToADoor(int x, int y) {
		// Above
		if (y - 1 >= 0) {
			if (mazeSelection.getTileAt(x, (y - 1)) == TileType.Door) {
				return true;
			}
		}
		// Below
		if (y + 1 < mazeSelection.getHeight()) {
			if (mazeSelection.getTileAt(x, (y + 1)) == TileType.Door) {
				return true;
			}
		}
		// Left
		if (x - 1 >= 0) {
			if (mazeSelection.getTileAt((x - 1), y) == TileType.Door) {
				return true;
			}
		}
		// Right
		if (x + 1 < mazeSelection.getWidth()) {
			if (mazeSelection.getTileAt((x + 1), y) == TileType.Door) {
				return true;
			}
		}
		
		return false;
	}
	
	public List<Point> getDoorsAround(int x, int y) {
		ArrayList<Point> doors = new ArrayList<Point>(4);
		// Above
		if (y - 1 >= 0) {
			if (mazeSelection.getTileAt(x, (y - 1)) == TileType.Door) {
				doors.add(new Point(x, (y - 1)));
			}
		}
		// Below
		if (y + 1 < mazeSelection.getHeight()) {
			if (mazeSelection.getTileAt(x, (y + 1)) == TileType.Door) {
				doors.add(new Point(x, (y + 1)));
			}
		}
		// Left
		if (x - 1 >= 0) {
			if (mazeSelection.getTileAt((x - 1), y) == TileType.Door) {
				doors.add(new Point((x - 1), y));
			}
		}
		// Right
		if (x + 1 < mazeSelection.getWidth()) {
			if (mazeSelection.getTileAt((x + 1), y) == TileType.Door) {
				doors.add(new Point((x + 1), y));
			}
		}
		
		return doors;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
