package uk.ac.tees.amazeballs.maze;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;


public class MazeScanner {

	public static class ScanData {
		public Point tileposition_firstfloor;
		public Point tileposition_firststart;
		public List<Point> tilepositions_weather;
	
		public ScanData() {
			tilepositions_weather = new ArrayList<Point>();
		}
	}
	
	public static ScanData scan(MazeNew maze) {
		ScanData data = new ScanData();
		for (int y = 0; y < maze.height; y++) {
			for (int x = 0; x < maze.width; x++) {
				switch (maze.grid[y * maze.width + x]) {
					case MazeNew.FLOOR_TILE:
						if (data.tileposition_firstfloor == null) {
							data.tileposition_firstfloor = new Point(x, y);
						}
						break;
					case MazeNew.WEATHER_TILE:
						data.tilepositions_weather.add(new Point(x, y));
						break;
					case MazeNew.START_TILE:
						if (data.tileposition_firststart == null) {
							data.tileposition_firststart = new Point(x, y);
						}
						break;
					default:
						break;
				}
			}
		}
		return data;
	}

}
