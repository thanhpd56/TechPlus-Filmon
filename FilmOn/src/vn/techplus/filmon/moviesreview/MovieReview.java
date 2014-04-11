package vn.techplus.filmon.moviesreview;

import java.util.List;

import org.json.JSONObject;

import vn.techplus.filmon.utils.JSONParser;

import com.fasterxml.jackson.core.JsonParser;

public abstract class MovieReview {

	public abstract List<MovieInfo> getTopRated();
	public abstract List<MovieInfo> getTopRated(int page);

	public abstract List<MovieInfo> getUpcoming();
	public abstract List<MovieInfo> getUpcoming(int page);

	public abstract List<MovieInfo> getPopular();
	public abstract List<MovieInfo> getPopular(int page);

	public abstract List<MovieInfo> getNowPlaying();
	public abstract List<MovieInfo> getNowPlaying(int page);

	public abstract MovieInfo getMoviesInfo(int movieId);

	public abstract List<MovieInfo> searchMovies(String key);
	public abstract List<MovieInfo> searchMovies(String key, int page);

	public abstract Object getTrailers(int movieId);
	
	public String getImdbRating(String imdbId) {
		String rating = "???";
		JSONParser jParser = new JSONParser();
		JSONObject jObj = jParser.getJSONFromUrl("http://www.omdbapi.com/?i=" + imdbId);
		if (jObj != null) {
			rating = jObj.optString("imdbRating", "???");
		}
			
		return rating;
	}

}
