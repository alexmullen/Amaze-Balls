package uk.ac.tees.amazeballs.menus;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import uk.ac.tees.amazeballs.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Highscores extends Activity {

	private List<Score> scores;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscores);
		
		// In order to add highscores, you must instantiate ScoreTableHandler then call the addScore method.
		
		ScoreTableHandler sth = new ScoreTableHandler(this);
		
		String df = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(System.currentTimeMillis()));
		
		sth.addScore(new Score(1, "Steve", 50, df));
		sth.addScore(new Score(2, "Bob", 60, df));
		sth.addScore(new Score(3, "Dave", 70, df));
		sth.addScore(new Score(4, "Terry", 80, df));
		sth.addScore(new Score(5, "Lewis", 10, df));
		
		scores = sth.getTopFive();

		//////////////////////////////////////////////////////
		
		TextView tv1 = (TextView) findViewById(R.id.Name1);
		tv1.setText(scores.get(0).getName() + "");
		
		TextView tv2 = (TextView) findViewById(R.id.Score1);
		tv2.setText(scores.get(0).getScore() + "");
		
		TextView tv3 = (TextView) findViewById(R.id.Date1);
		tv3.setText(scores.get(0).getDate() + "");
		
		//////////////////////////////////////////////////////
		
		TextView tv4 = (TextView) findViewById(R.id.Name2);
		tv4.setText(scores.get(1).getName() + "");
		
		TextView tv5 = (TextView) findViewById(R.id.Score2);
		tv5.setText(scores.get(1).getScore() + "");
		
		TextView tv6 = (TextView) findViewById(R.id.Date2);
		tv6.setText(scores.get(1).getDate() + "");
		
		//////////////////////////////////////////////////////
		
		TextView tv7 = (TextView) findViewById(R.id.Name3);
		tv7.setText(scores.get(2).getName() + "");
		
		TextView tv8 = (TextView) findViewById(R.id.Score3);
		tv8.setText(scores.get(2).getScore() + "");
		
		TextView tv9 = (TextView) findViewById(R.id.Date3);
		tv9.setText(scores.get(2).getDate() + "");
		
		//////////////////////////////////////////////////////
		
		TextView tv10 = (TextView) findViewById(R.id.Name4);
		tv10.setText(scores.get(3).getName() + "");
		
		TextView tv11 = (TextView) findViewById(R.id.Score4);
		tv11.setText(scores.get(3).getScore() + "");
		
		TextView tv12 = (TextView) findViewById(R.id.Date4);
		tv12.setText(scores.get(3).getDate() + "");
		
		//////////////////////////////////////////////////////
		
		TextView tv13 = (TextView) findViewById(R.id.Name5);
		tv13.setText(scores.get(4).getName() + "");
		
		TextView tv14 = (TextView) findViewById(R.id.Score5);
		tv14.setText(scores.get(4).getScore() + "");
		
		TextView tv15 = (TextView) findViewById(R.id.Date5);
		tv15.setText(scores.get(4).getDate() + "");
		
	}
	
}
