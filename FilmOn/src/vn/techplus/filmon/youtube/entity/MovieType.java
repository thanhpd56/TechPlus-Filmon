package vn.techplus.filmon.youtube.entity;

import java.util.List;

public class MovieType {

	public int movie_type_id;
	public String movie_type_name;
	public List<Movie> listMovie;

	public MovieType(int movie_type_id, String movie_type_name,
			List<Movie> listMovie) {
		super();
		this.movie_type_id = movie_type_id;
		this.movie_type_name = movie_type_name;
		this.listMovie = listMovie;
	}

	public List<Movie> getListMovie() {
		return listMovie;
	}

	public void setListMovie(List<Movie> listMovie) {
		this.listMovie = listMovie;
	}

	public int getMovie_type_id() {
		return movie_type_id;
	}

	public void setMovie_type_id(int movie_type_id) {
		this.movie_type_id = movie_type_id;
	}

	public String getMovie_type_name() {
		return movie_type_name;
	}

	public void setMovie_type_name(String movie_type_name) {
		this.movie_type_name = movie_type_name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Movie movie : listMovie) {
			sb.append(movie.toString());
		}
		return String.format("\n\t- %d %s %s", movie_type_id, movie_type_name,
				sb.toString());
	}

}
