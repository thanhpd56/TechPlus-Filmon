package vn.techplus.filmon.youtube.entity;

import java.util.List;

public class MovieTypeWrapper {
	public List<MovieType> listMovieType;

	public MovieTypeWrapper(List<MovieType> listMovieType) {
		super();
		this.listMovieType = listMovieType;
	}

	public List<MovieType> getListMovieType() {
		return listMovieType;
	}

	public void setListMovieType(List<MovieType> listMovieType) {
		this.listMovieType = listMovieType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("List Movie Type \n");
		for (MovieType movieType : listMovieType) {
			sb.append(movieType.toString());
		}
		return String.format("%s", sb.toString());
	}

}
