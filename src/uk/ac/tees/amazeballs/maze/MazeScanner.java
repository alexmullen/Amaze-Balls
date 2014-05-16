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
	
	public static ScanData scan(Maze maze) {
		ScanData data = new ScanData();
		for (int y = 0; y < maze.height; y++) {
			for (int x = 0; x < maze.width; x++) {
				switch (maze.grid[x][y]) {
					case Floor:
						if (data.tileposition_firstfloor == null) {
							data.tileposition_firstfloor = new Point(x, y);
						}
						break;
					case Weather:
						data.tilepositions_weather.add(new Point(x, y));
						break;
					case Start:
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
