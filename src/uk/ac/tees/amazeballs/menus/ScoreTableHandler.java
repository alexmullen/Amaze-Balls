package uk.ac.tees.amazeballs.menus;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Handles DB creation and CRUD operations.
 * 
 * @author m2088258
 * 
 */
public class ScoreTableHandler extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Highscores.db";

	private static final String TABLE_NAME = "scores";

	private static final String COL_ID = "_id";
	private static final String COL_NAME = "name";
	private static final String COL_SCORE = "score";
	private static final String COL_DATE = "date";

	//
	// CURRENT DB ERROR EITHER HERE ^ OR IN onCreate METHOD.
	//

	// context is a reference to the activity using the db.
	public ScoreTableHandler(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	// When the ScoreTableHandler object is created this method is run and it
	// creates the local db.
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ COL_ID + " INTEGER PRIMARY KEY," + COL_NAME + " TEXT,"
				+ COL_SCORE + " INT," + COL_DATE + " TEXT)";
		db.execSQL(CREATE_SCORES_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if exists and create fresh
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

	}

	// Adds a score object to the database.
	public void addScore(Score score) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COL_ID, score.getId());
		values.put(COL_NAME, score.getName());
		values.put(COL_SCORE, score.getScore());
		values.put(COL_DATE, score.getDate());
		db.insert(TABLE_NAME, null, values);
		db.close(); // Closing database connection
	}

	// Returns all items from the database.
	public List<Score> getAll() {

		List<Score> list = new ArrayList<Score>();
		String selectQuery = "SELECT * FROM " + TABLE_NAME; // * means all.
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) { // checks for null cursor object
			do {
				Score score = new Score(cursor.getInt(0), cursor.getString(1),
						cursor.getInt(2), cursor.getString(3));
				list.add(score);
			} while (cursor.moveToNext()); // returns true while there are
											// results
		}
		cursor.close();
		return list;
	}

	// Returns the top X results ordered by score.
	public List<Score> getTopX(int values) {

		List<Score> list = new ArrayList<Score>();
		String selectTopQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY "
				+ COL_SCORE + " LIMIT " + values;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectTopQuery, null);
		if (cursor.moveToFirst()) { // checks for null cursor object
			do {
				Score score = new Score(cursor.getInt(0), cursor.getString(1),
						cursor.getInt(2), cursor.getString(3));
				list.add(score);
			} while (cursor.moveToNext()); // returns true while there are results/data within the cursor object
		}
		return list;
	}

	// Retrieves the last written field in order to increment the primary key value
	public int newID() {
		List<Score> list = new ArrayList<Score>();
		String selectTopQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC " + " LIMIT  1";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectTopQuery, null);
		if (cursor.moveToFirst()) { // checks for null cursor object
			do {
				Score score = new Score(cursor.getInt(0), cursor.getString(1),
						cursor.getInt(2), cursor.getString(3));
				list.add(score);
			} while (cursor.moveToNext()); // returns true while there are
											// results
		}
		
		if (list.isEmpty()) {
			return 1;
		} else {
			Score lastInput = list.get(0);
			Log.v("Previous id", lastInput.getId() + "");

			return lastInput.getId() + 1;
		}
	}

}
