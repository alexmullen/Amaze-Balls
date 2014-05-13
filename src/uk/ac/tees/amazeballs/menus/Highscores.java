package uk.ac.tees.amazeballs.menus;

import java.util.List;

import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Highscores extends Activity {

	private List<Score> scores;
	public static ScoreTableHandler scoreHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscores);

		// In order to add highscores, you must make a call to the addScore method of a ScoreTableHandler
		scoreHandler = new ScoreTableHandler(this);
		scores = scoreHandler.getAll();
		
		// Returns the number of values in the database
		int numOfVals = scores.size();
		
		// Analyses database content then displays appropriate number of results
		switch (numOfVals) {
		
		case 1:
			scores = scoreHandler.getTopX(numOfVals);
			writeToDb(1);
			break;
		case 2:
			scores = scoreHandler.getTopX(numOfVals);
			writeToDb(1);
			writeToDb(2);
			break;
		case 3:
			scores = scoreHandler.getTopX(numOfVals);
			writeToDb(1);
			writeToDb(2);
			writeToDb(3);
			break;
		case 4:
			scores = scoreHandler.getTopX(numOfVals);
			writeToDb(1);
			writeToDb(2);
			writeToDb(3);
			writeToDb(4);
			break;
		case 5:
			scores = scoreHandler.getTopX(numOfVals);
			writeToDb(1);
			writeToDb(2);
			writeToDb(3);
			writeToDb(4);
			writeToDb(5);
			break;
			
		default:
			// The default number of data items to display is five as the Highscore form can only display five scores
//			scores = scoreHandler.getTopX(numOfVals);
//			writeToDb(1);
//			writeToDb(2);
//			writeToDb(3);
//			writeToDb(4);
//			writeToDb(5);
			break;
		}
	}
	
	// Closes the database connection twhen the activity is destroyed.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(scoreHandler != null) {
			scoreHandler.close();
		}
	}

	// Writes each individual row of data to the database
	public void writeToDb(int row) {

		switch (row) {

		case 1:
			TextView tv1 = (TextView) findViewById(R.id.Name1);
			tv1.setText(scores.get(0).getName() + "");

			TextView tv2 = (TextView) findViewById(R.id.Score1);
			tv2.setText(scores.get(0).getScore() + "");

			TextView tv3 = (TextView) findViewById(R.id.Date1);
			tv3.setText(scores.get(0).getDate() + "");
			break;

		case 2:
			TextView tv4 = (TextView) findViewById(R.id.Name2);
			tv4.setText(scores.get(1).getName() + "");

			TextView tv5 = (TextView) findViewById(R.id.Score2);
			tv5.setText(scores.get(1).getScore() + "");

			TextView tv6 = (TextView) findViewById(R.id.Date2);
			tv6.setText(scores.get(1).getDate() + "");
			break;

		case 3:
			TextView tv7 = (TextView) findViewById(R.id.Name3);
			tv7.setText(scores.get(2).getName() + "");

			TextView tv8 = (TextView) findViewById(R.id.Score3);
			tv8.setText(scores.get(2).getScore() + "");

			TextView tv9 = (TextView) findViewById(R.id.Date3);
			tv9.setText(scores.get(2).getDate() + "");
			break;
		case 4:
			TextView tv10 = (TextView) findViewById(R.id.Name4);
			tv10.setText(scores.get(3).getName() + "");

			TextView tv11 = (TextView) findViewById(R.id.Score4);
			tv11.setText(scores.get(3).getScore() + "");

			TextView tv12 = (TextView) findViewById(R.id.Date4);
			tv12.setText(scores.get(3).getDate() + "");
			break;

		case 5:
			TextView tv13 = (TextView) findViewById(R.id.Name5);
			tv13.setText(scores.get(4).getName() + "");

			TextView tv14 = (TextView) findViewById(R.id.Score5);
			tv14.setText(scores.get(4).getScore() + "");

			TextView tv15 = (TextView) findViewById(R.id.Date5);
			tv15.setText(scores.get(4).getDate() + "");
			break;
		}

	}

}
