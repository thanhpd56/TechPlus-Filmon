package vn.techplus.filmon.youtube.entity;

public class Movie {

	public String movie_id;
	public String movie_name;

	public Movie(String movie_id, String movie_name) {
		super();
		this.movie_id = movie_id;
		this.movie_name = movie_name;
	}

	public String getMovie_id() {
		return movie_id;
	}

	public void setMovie_id(String movie_id) {
		this.movie_id = movie_id;
	}

	public String getMovie_name() {
		return movie_name;
	}

	public void setMovie_name(String movie_name) {
		this.movie_name = movie_name;
	}

	@Override
	public String toString() {
		return String.format("- %s %s\n", movie_id, movie_name);
	}

}
