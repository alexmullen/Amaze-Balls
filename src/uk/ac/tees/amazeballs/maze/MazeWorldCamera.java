package uk.ac.tees.amazeballs.maze;


public class MazeWorldCamera {
	
//	public class VisibleTile {
//		public final int grid_x;
//		public final int grid_y;
//		public final int world_x;
//		public final int world_y;
//		public VisibleTile(int grid_x, int grid_y, int world_x, int world_y) {
//			this.grid_x = grid_x;
//			this.grid_y = grid_y;
//			this.world_x = world_x;
//			this.world_y = world_y;
//		}
//	}

	public final MazeWorld world;
	
	private int left;
	private int top;
	private int right;
	private int bottom;
	
	//private final VisibleTile[] cachedVisibleTiles;
	
	public MazeWorldCamera(MazeWorld world, int left, int top, int right, int bottom) {
		this.world = world;
		this.left = Math.max(0, left);
		this.top = Math.max(0, top);
		this.right = Math.min(world.width, right);
		this.bottom = Math.min(world.height, bottom);
//		
//		Maze maze = world.maze;
//		cachedVisibleTiles = new VisibleTile[maze.width * maze.height];
//		int index = 0;
//		Point p = new Point();
//		for (int x = 0; x < maze.width; x++) {
//			for (int y = 0; y < maze.height; y++) {
//				world.getWorldCoords(x, y, p);
//				cachedVisibleTiles[index++] = new VisibleTile(x, y, p.x, p.y);
//			}
//		}
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getRight() {
		return right;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public int getWidth() {
		return (right - left);
	}
	
	public int getHeight() {
		return (bottom - top);
	}
	
	public int moveLeft(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= left) {
			amount = left;
			left = 0;
			right -= amount;
		} else {
			left -= amount;
			right -= amount;
		}
		return amount;
	}
	
	public int moveUp(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		if (amount >= top) {
			amount = top;
			top = 0;
			bottom -= amount;
		} else {
			top -= amount;
			bottom -= amount;
		}
		return amount;
	}
	
	public int moveRight(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (world.width - right);
		int amountToShift = Math.min(amount, amountRemaining);
		right += amountToShift;
		left += amountToShift;
		return amountToShift;
	}
	
	public int moveDown(int amount) {
		// Move by the amount or the remaining amount if the amount is more than the remaining space
		int amountRemaining = (world.height - bottom);
		int amountToShift = Math.min(amount, amountRemaining);
		bottom += amountToShift;
		top += amountToShift;
		return amountToShift;
	}
	
//	public VisibleTile[] getVisibleTiles() {
//		ArrayList<Point> visTileCoords = new ArrayList<Point>();
//		for (int x = left; x <= getWidth(); x += world.getTilesize()) {
//			for (int y = top; y <= getHeight(); y += world.getTilesize()) {
//				Point gridCoords = world.getGridCoords(x, y);
//				if (world.getMaze().isTileAt(gridCoords.x, gridCoords.y)) {
//					visTileCoords.add(gridCoords);
//				}
//			}
//		}
//		for (int x = 0; x < world.getMaze().getWidth(); x++) {
//			for (int y = 0; y < world.getMaze().getHeight(); y++) {
//				visTileCoords.add(new Point(x, y));
//			}
//		}
//		
//
//		
//		
//		return cachedVisibleTiles;
//	}
	
	public void getVisibleRange(int[] outRange) {
		outRange[0] = Math.max(0, (left / world.tilesize) - 1);
		outRange[1] = Math.max(0, (top / world.tilesize) - 1);
		outRange[2] = Math.min((world.maze.width - 1), (right / world.tilesize) + 1);
		outRange[3] = Math.min((world.maze.height - 1), (bottom / world.tilesize) + 1);
	}

}
