package uk.ac.tees.amazeballs;

import android.graphics.Point;
import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeWorld;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.maze.MazeWorld.Ball;
import uk.ac.tees.amazeballs.maze.MazeWorldCamera;
import uk.ac.tees.amazeballs.views.MazeViewport;


public class GameController {

	private final int NORMAL_BALL_SPEED = 3;
	private final int ICE_BALL_SPEED = 7;
	private final int RAIN_BALL_SPEED = 1;

	private final static double TILT_SENSITIVITY = 0.50;

	public volatile float lastAccelerometerReading_x;
	public volatile float lastAccelerometerReading_y;

	private final MazeWorld mazeWorld;
	private final MazeWorldCamera mazeWorldCamera;
	private final MazeViewport mazeViewport;

	private int ballSpeed = NORMAL_BALL_SPEED;
	private int keysCarrying;
	boolean touchingRain;
	boolean touchingIce;
	
	private boolean finished;

	public GameController(Maze maze, MazeViewport mazeVewport) {
		this.mazeViewport = mazeVewport;
		mazeWorld = new MazeWorld(maze, 20);
		mazeWorldCamera = new MazeWorldCamera(mazeWorld, 0, 0,
				10 * mazeWorld.getTilesize(), 
				15 * mazeWorld.getTilesize());
		mazeViewport.setCamera(mazeWorldCamera);

		// Search for the (or a) start position to place the ball
		Point startPosition = findStartPosition();
		if (startPosition == null) {
			// No start position available!
			finished = true;
			return;
		}

		// Create and initialize the ball
		int ballSize = (int) (mazeWorld.getTilesize() * 0.8);
		int ballStartOffset = (mazeWorld.getTilesize() - ballSize) / 2;
		int ballStart_x = startPosition.x * mazeWorld.getTilesize() + ballStartOffset;
		int ballStart_y = startPosition.y * mazeWorld.getTilesize() + ballStartOffset;
		Ball ball = new Ball(ballStart_x, ballStart_y, ballSize);
		mazeWorld.setBall(ball);
	}

	public void update() {
		moveBall();

		scrollScreen();

		processTouchingTiles();

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
	 * @return true if the ball intersects a wall or door tile by 1 pixel or
	 *         more.
	 */
	private boolean ballHasCollided() {
		int gridPosTouchedX;
		int gridPosTouchedY;
		int ballSize = mazeWorld.getBall().size;
		int tileSize = mazeWorld.getTilesize();
		TileType tileTouched;

		// Check top left corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}

		// Check top right corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x + (ballSize - 1)) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}

		// Check bottom right corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x + (ballSize - 1)) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y + (ballSize - 1)) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}

		// Check bottom left corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y + (ballSize - 1)) / tileSize;
		tileTouched = mazeWorld.getMaze().getTileAt(gridPosTouchedX, gridPosTouchedY);
		if (tileTouched == TileType.Wall || tileTouched == TileType.Door) {
			return true;
		}

		// No collisions
		return false;
	}

	/**
	 * Scroll to another part of the maze if the ball is within a certain
	 * distance from the view edge.
	 */
	private void scrollScreen() {
		int mazeAreaHeight = mazeWorldCamera.getHeight();
		int mazeAreaWidth = mazeWorldCamera.getWidth();

		// Convert the balls world coordinates into view/camera coordinates
		int viewBallPositionX = (mazeWorldCamera.getWorld().getBall().position_x - mazeWorldCamera.getLeft());
		int viewBallPositionY = (mazeWorldCamera.getWorld().getBall().position_y - mazeWorldCamera.getTop());

		// Potentially scroll the screen down
		if ((float) viewBallPositionY / mazeAreaHeight >= 0.75) {
			mazeWorldCamera.moveDown(ballSpeed);
		}

		// Potentially scroll the screen up
		if ((float) viewBallPositionY / mazeAreaHeight <= 0.25) {
			mazeWorldCamera.moveUp(ballSpeed);
		}

		// Potentially scroll the screen right
		if ((float) viewBallPositionX / mazeAreaWidth >= 0.75) {
			mazeWorldCamera.moveRight(ballSpeed);
		}

		// Potentially scroll the screen left
		if ((float) viewBallPositionX / mazeAreaWidth <= 0.25) {
			mazeWorldCamera.moveLeft(ballSpeed);
		}
	}

	/**
	 * Handles what happens when certain tiles are touched.
	 */
	private void handleTouchedTile(int touchedGrid_x, int touchedGrid_y, TileType type) {
		// Reset these
		touchingRain = false;
		touchingIce = false;
		ballSpeed = NORMAL_BALL_SPEED;
		
		processDoorsNearby(touchedGrid_x, touchedGrid_y);

		switch (type) {
			case Chest:
				mazeWorld.getMaze().setTileAt(touchedGrid_x, touchedGrid_y, TileType.Floor);
				break;
			case Key:
				// Increment the number of keys we have then replace the key tile
				// with a floor tile
				keysCarrying++;
				mazeWorld.getMaze().setTileAt(touchedGrid_x, touchedGrid_y, TileType.Floor);
				break;
			case Goal:
				// End the game
				finished = true;
				return;
			case Ice:
				if (!touchingRain) {
					touchingIce = true;
					ballSpeed = ICE_BALL_SPEED;
				}
				break;
			case Penalty:
				// Apply the penalty
	
				mazeWorld.getMaze().setTileAt(touchedGrid_x, touchedGrid_y, TileType.Floor);
				break;
			case Rain:
				touchingIce = false;
				touchingRain = true;
				ballSpeed = RAIN_BALL_SPEED;
				break;
			default:
				// Ignore other tiles
				break;
		}
	}

	private void processTouchingTiles() {
		int gridPosTouchedX;
		int gridPosTouchedY;
		int ballSize = mazeWorld.getBall().size;
		int tileSize = mazeWorld.getTilesize();

		// Check top left corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y) / tileSize;
		handleTouchedTile(gridPosTouchedX, gridPosTouchedY, mazeWorld.getMaze()
				.getTileAt(gridPosTouchedX, gridPosTouchedY));

		// Check top right corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x + (ballSize - 1)) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y) / tileSize;
		handleTouchedTile(gridPosTouchedX, gridPosTouchedY, mazeWorld.getMaze()
				.getTileAt(gridPosTouchedX, gridPosTouchedY));

		// Check bottom right corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x + (ballSize - 1)) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y + (ballSize - 1)) / tileSize;
		handleTouchedTile(gridPosTouchedX, gridPosTouchedY, mazeWorld.getMaze()
				.getTileAt(gridPosTouchedX, gridPosTouchedY));

		// Check bottom left corner of ball
		gridPosTouchedX = (int) (mazeWorld.getBall().position_x) / tileSize;
		gridPosTouchedY = (int) (mazeWorld.getBall().position_y + (ballSize - 1)) / tileSize;
		handleTouchedTile(gridPosTouchedX, gridPosTouchedY, mazeWorld.getMaze()
				.getTileAt(gridPosTouchedX, gridPosTouchedY));
	}

	private void processDoorsNearby(int x, int y) {
		// Above
		if (y - 1 >= 0) {
			if (mazeWorld.getMaze().getTileAt(x, (y - 1)) == TileType.Door) {
				handleDoorNearby(x, (y - 1));
			}
		}
		// Below
		if (y + 1 < mazeWorld.getMaze().getHeight()) {
			if (mazeWorld.getMaze().getTileAt(x, (y + 1)) == TileType.Door) {
				handleDoorNearby(x, (y + 1));
			}
		}
		// Left
		if (x - 1 >= 0) {
			if (mazeWorld.getMaze().getTileAt((x - 1), y) == TileType.Door) {
				handleDoorNearby((x - 1), y);
			}
		}
		// Right
		if (x + 1 < mazeWorld.getMaze().getWidth()) {
			if (mazeWorld.getMaze().getTileAt((x + 1), y) == TileType.Door) {
				handleDoorNearby((x + 1), y);
			}
		}
	}

	private void handleDoorNearby(int x, int y) {
		// "Use" any picked up keys to unlock any doors near
		if (keysCarrying > 0) {
			keysCarrying--;
			mazeWorld.getMaze().setTileAt(x, y, TileType.Floor);
		}
	}

	private Point findStartPosition() {
		Point firstEmptySpace = null;
		for (int y = 0; y < mazeWorld.getMaze().getHeight(); y++) {
			for (int x = 0; x < mazeWorld.getMaze().getWidth(); x++) {
				if (mazeWorld.getMaze().getTileAt(x, y) == TileType.Start) {
					return new Point(x, y);
				} else if (firstEmptySpace == null && mazeWorld.getMaze().getTileAt(x, y) == TileType.Floor) {
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
