package uk.ac.tees.amazeballs.menus;




public class Score {

	private int id;
	private String name;
	private int score;
	private String date;
	

	public Score(int id, String name, int score, String date) {
		this.id =id;
		this.name = name;
		this.score = score;
		this.date = date;
	}
	
	///// Getters /////
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}

	public String getDate() {
		return date;
	}

	///// Setters /////
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}


	public void setDate(String date) {
		this.date = date;
	}
	
}

