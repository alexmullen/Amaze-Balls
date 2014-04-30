package uk.ac.tees.amazeballs;

import uk.ac.tees.amazeballs.maze.Maze;
import uk.ac.tees.amazeballs.maze.MazeFactory;
import uk.ac.tees.amazeballs.maze.MazeSelection;
import uk.ac.tees.amazeballs.maze.TileFactory;
import uk.ac.tees.amazeballs.maze.TileType;
import uk.ac.tees.amazeballs.views.MazeEditorView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MazeEditorActivity extends Activity {

	private Maze currentMaze;
	private MazeSelection currentMazeSelection;
	private MazeEditorView mazeEditorView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maze_editor);
		
		loadTiles();
		
		mazeEditorView = (MazeEditorView) findViewById(R.id.maze_grid_view);
		
		currentMaze = MazeFactory.createBorderedMaze(25, 30);
		currentMazeSelection = new MazeSelection(currentMaze, 0, 0, 10, 15);
		mazeEditorView.setMaze(currentMazeSelection);
	}
	
	private void loadTiles() {
		TileFactory.registerTile(TileType.Floor, this.getResources().getDrawable(R.drawable.floor));
		TileFactory.registerTile(TileType.Wall, this.getResources().getDrawable(R.drawable.wall));
		TileFactory.registerTile(TileType.Ball, this.getResources().getDrawable(R.drawable.ball));
	}
	
	public void OnUpButtonClicked(View v) {
		currentMazeSelection.shiftUp(2);
		mazeEditorView.invalidate();
	}
	
	public void OnRightButtonClicked(View v) {
		currentMazeSelection.shiftRight(2);
		mazeEditorView.invalidate();
	}
	
	public void OnDownButtonClicked(View v) {
		currentMazeSelection.shiftDown(2);
		mazeEditorView.invalidate();
	}
	
	public void OnLeftButtonClicked(View v) {
		currentMazeSelection.shiftLeft(2);
		mazeEditorView.invalidate();
	}

}
