package uk.ac.tees.amazeballs;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeWorld;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.maze.MazeWorld.Ball;
import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import uk.ac.tees.amazeballs.views.MazeViewport;


public class NewImprovedGameController {

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
	
	private final int NORMAL_BALL_SPEED = 5;
	private final int ICE_BALL_SPEED = 10;
	private final int RAIN_BALL_SPEED = 1;
	
	private final static int GRID_SCROLLING_AMOUNT = 1;
	private final static double TILT_SENSITIVITY = 0.75;
	
	public volatile float lastAccelerometerReading_x;
	public volatile float lastAccelerometerReading_y;
	
	private final MazeWorld mazeWorld;
	private final MazeWorldCamera mazeWorldCamera;
	private final MazeViewport mazeViewport;
	
	private int ballSpeed = NORMAL_BALL_SPEED;
	private int keys;
	private boolean finished;
	
	
	public NewImprovedGameController(Maze maze, MazeViewport mazeVewport) {
		this.mazeViewport = mazeVewport;
		mazeWorld = new MazeWorld(maze, 10);
		mazeWorldCamera = new MazeWorldCamera(mazeWorld, 0, 0, 100, 150);
		mazeViewport.setCamera(mazeWorldCamera);
		
		// Search for the (or a) start position to place the ball
		Point startPosition = findStartPosition();
		if (startPosition == null) {
			// No start position available!
			//finished = true;
			return;
		}
		
		// Create and initialize the ball
		int ballSize = (int) (mazeWorld.getTilesize() * 0.8);
		int ballStartOffset = (mazeWorld.getTilesize() - ballSize) / 2;		
		int ballStart_x = startPosition.x * mazeWorld.getTilesize() + ballStartOffset;
		int ballStart_y = startPosition.y * mazeWorld.getTilesize() + ballStartOffset;
		Ball ball = new Ball(ballStart_x, ballStart_y, (int) (mazeWorld.getTilesize() * 0.8));
		mazeWorld.setBall(ball);
	}
	
	
	public void update() {
		
		moveBall();

		scrollScreen();
		
		handleTouchedTiles();

		// Render the next frame
		mazeViewport.invalidate();
	}
	
	/**
	 * Move the ball depending on the tilt of the device.
	 */
	private void moveBall() {
		// Get the accelerometer reading
		if (lastAccelerometerReading_y > TILT_SENSITIVITY) {
			mazeWorld.getBall().position_y += ballSpeed;		
			// Resolve collisions
			while (ballHasCollided()) {
				mazeWorld.getBall().position_y--;
			}
		} 
		if (lastAccelerometerReading_y < -TILT_SENSITIVITY) {
			mazeWorld.getBall().position_y -= ballSpeed;		
			// Resolve collisions
			while (ballHasCollided()) {
				mazeWorld.getBall().position_y++;
			}
		} 
		if (lastAccelerometerReading_x > TILT_SENSITIVITY) {
			mazeWorld.getBall().position_x -= ballSpeed;
			// Resolve collisions
			while (ballHasCollided()) {
				mazeWorld.getBall().position_x++;
			}
		}
		if (lastAccelerometerReading_x < -TILT_SENSITIVITY) {
			mazeWorld.getBall().position_x += ballSpeed;
			// Resolve collisions
			while (ballHasCollided()) {
				mazeWorld.getBall().position_x--;
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
		int ballSize = mazeWorld.getBall().size;
		int tileSize = mazeWorld.getTilesize();
		TileType tileTouched;
		
		// Check top left corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// Check top right corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// Check bottom right corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y + ballSize) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}
		
		// Check bottom left corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y + ballSize) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
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
		int mazeAreaHeight = mazeWorldCamera.getHeight();
		if ((double)mazeWorld.getBall().position_y / mazeAreaHeight >= 0.75) {
			int amountShiftedDown = mazeWorldCamera.moveDown(GRID_SCROLLING_AMOUNT);
			mazeWorld.getBall().position_y -= amountShiftedDown;
		}
		
		// Potentially scroll the screen up
		if ((double)mazeWorld.getBall().position_y / mazeAreaHeight <= 0.25) {
			int amountShiftedUp = mazeWorldCamera.moveUp(GRID_SCROLLING_AMOUNT);
			mazeWorld.getBall().position_y += amountShiftedUp;
		}
		
		// Potentially scroll the screen right
		int mazeAreaWidth = mazeWorldCamera.getWidth();
		if ((double)mazeWorld.getBall().position_x / mazeAreaWidth >= 0.75) {
			int amountShiftedRight = mazeWorldCamera.moveRight(GRID_SCROLLING_AMOUNT);
			mazeWorld.getBall().position_x -= amountShiftedRight;
		}
		
		// Potentially scroll the screen left
		if ((double)mazeWorld.getBall().position_x / mazeAreaWidth <= 0.25) {
			int amountShiftedLeft = mazeWorldCamera.moveLeft(GRID_SCROLLING_AMOUNT);
			mazeWorld.getBall().position_x += amountShiftedLeft;
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
			
			// "Use" any picked up keys to unlock any doors near
			if ((keys > 0) && (isNextToADoor(touchedTile.x, touchedTile.y))) {
				List<Point> doors = getDoorsAround(touchedTile.x, touchedTile.y);
				for (Point d : doors) {
					if (keys > 0) {
						keys--;;
						mazeWorld.getMaze().setTileAt(d.x, d.y, TileType.Floor);	
					} else {
						break;
					}
				}
			}
			
			switch (touchedTile.type) {
				case Chest:
					mazeWorld.getMaze().setTileAt(touchedTile.x, touchedTile.y, TileType.Floor);
					break;				
				case Key:
					// Increment the number of keys we have then replace the key tile with a floor tile
					keys++;
					mazeWorld.getMaze().setTileAt(touchedTile.x, touchedTile.y, TileType.Floor);
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
					
					mazeWorld.getMaze().setTileAt(touchedTile.x, touchedTile.y, TileType.Floor);
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
		int ballSize = mazeWorld.getBall().size;
		int tileSize = mazeWorld.getTilesize();
		
		// Check top left corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY));
		touchedTiles.add(tile);
		
		// Check top right corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		// Check bottom right corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x + ballSize) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y + ballSize) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		// Check bottom left corner of ball
		gridPosTouchedX = (int)(mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int)(mazeWorld.getBall().position_y + ballSize) / tileSize;
		tile = new TouchedTile(gridPosTouchedX, gridPosTouchedY, 
				mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY));
		if (!touchedTiles.contains(tile)) {
			touchedTiles.add(tile);
		}
		
		return touchedTiles;
	}
	
	public boolean isNextToADoor(int x, int y) {
		// Below
		if (y + 1 < mazeWorld.getMaze().getHeight()) {
			if (mazeWorld.getMaze().getTileAt(x, (y + 1)) == TileType.Door) {
				return true;
			}
		}
		// Left
		if (x - 1 >= 0) {
			if (mazeWorld.getMaze().getTileAt((x - 1), y) == TileType.Door) {
				return true;
			}
		}
		// Right
		if (x + 1 < mazeWorld.getMaze().getWidth()) {
			if (mazeWorld.getMaze().getTileAt((x + 1), y) == TileType.Door) {
				return true;
			}
		}
		// Above
		if (y - 1 >= 0) {
			if (mazeWorld.getMaze().getTileAt(x, (y - 1)) == TileType.Door) {
				return true;
			}
		}
		
		return false;
	}
	
	public List<Point> getDoorsAround(int x, int y) {
		ArrayList<Point> doors = new ArrayList<Point>(4);
		// Above
		if (y - 1 >= 0) {
			if (mazeWorld.getMaze().getTileAt(x, (y - 1)) == TileType.Door) {
				doors.add(new Point(x, (y - 1)));
			}
		}
		// Below
		if (y + 1 < mazeWorld.getMaze().getHeight()) {
			if (mazeWorld.getMaze().getTileAt(x, (y + 1)) == TileType.Door) {
				doors.add(new Point(x, (y + 1)));
			}
		}
		// Left
		if (x - 1 >= 0) {
			if (mazeWorld.getMaze().getTileAt((x - 1), y) == TileType.Door) {
				doors.add(new Point((x - 1), y));
			}
		}
		// Right
		if (x + 1 < mazeWorld.getMaze().getWidth()) {
			if (mazeWorld.getMaze().getTileAt((x + 1), y) == TileType.Door) {
				doors.add(new Point((x + 1), y));
			}
		}
		
		return doors;
	}
	
	private Point findStartPosition() {
		Point firstEmptySpace = null;
		Maze underlyingMaze = mazeWorld.getMaze();
		for (int y = 0; y < underlyingMaze.getHeight(); y++) {
			for (int x = 0; x < underlyingMaze.getWidth(); x++) {
				if (underlyingMaze.getTileAt(x, y) == TileType.Start) {
					return new Point(x, y);
				} else if (firstEmptySpace == null && underlyingMaze.getTileAt(x, y) == TileType.Floor) {
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
