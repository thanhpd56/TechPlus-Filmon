package vn.techplus.filmon.youtube.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.techplus.filmon.utils.JSONParser;
import vn.techplus.filmon.youtube.entity.Movie;
import vn.techplus.filmon.youtube.entity.MovieType;
import vn.techplus.filmon.youtube.entity.MovieTypeWrapper;
import vn.techplus.filmon.youtube.entity.Video;
import vn.techplus.filmon.youtube.entity.DataProvider;
import android.content.Context;
import android.util.Log;

public class MovieParser {

	public static String TAG = "MOVIE_PARSER";

	// tag special videos
	public static String TAG_SPECIAL_VIDEOS = "special_videos";
	public static String TAG_VIDEOS = "videos";
	public static String TAG_VIDEO_ID = "video_id";
	public static String TAG_VIDEO_NAME = "video_name";
	public static String TAG_VIDEO_THUMB = "video_thumbnail";
	// tag movies type
	public static String TAG_MOVIES_TYPE = "movies_type";
	public static String TAG_MOVIE_TYPE_ID = "movie_type_id";
	public static String TAG_MOVIE_TYPE_NAME = "movie_type_name";
	public static String TAG_MOVIES = "movies";
	public static String TAG_MOVIE_ID = "movie_id";
	public static String TAG_MOVIE_NAME = "movie_name";

	public static DataProvider getDataProvider(Context context, String path) {
		DataProvider dataProvider = null;
		
		List<Video> _listVideo = new ArrayList<Video>();
		List<MovieType> _listMovieTypes = new ArrayList<MovieType>();
		MovieTypeWrapper movieTypeWrapper = null;
		JSONParser jsonParser = new JSONParser();
		JSONObject json = jsonParser.getJSONFromAsset(context, path);
		Log.i(TAG, json.toString());
		JSONArray videoArray = null;
		JSONArray movieTypeArray = null;
		JSONArray movieArray = null;

		try {
			JSONObject special_videos = json.getJSONObject(TAG_SPECIAL_VIDEOS);
			JSONArray videos = special_videos.getJSONArray(TAG_VIDEOS);
			for (int i = 0; i < videos.length(); i++) {
				JSONObject video = videos.getJSONObject(i);
				String video_id = video.optString(TAG_VIDEO_ID);
				String video_name = video.optString(TAG_VIDEO_NAME);
				String video_thumbnail = video.optString(TAG_VIDEO_THUMB);
				_listVideo
						.add(new Video(video_id, video_name, video_thumbnail));
			}

			JSONArray movies_type_arr = json.getJSONArray(TAG_MOVIES_TYPE);
			for (int j = 0; j < movies_type_arr.length(); j++) {
				JSONObject movie_type = movies_type_arr.getJSONObject(j);
				int movie_type_id = movie_type.optInt(TAG_MOVIE_TYPE_ID);
				String movie_type_name = movie_type
						.optString(TAG_MOVIE_TYPE_NAME);
				JSONArray movies = movie_type.getJSONArray(TAG_MOVIES);
				List<Movie> _listMovie = new ArrayList<Movie>();
				for (int k = 0; k < movies.length(); k++) {
					JSONObject movie = movies.getJSONObject(k);
					String movie_id = movie.optString(TAG_MOVIE_ID);
					String movie_name = movie.optString(TAG_MOVIE_NAME);

					_listMovie.add(new Movie(movie_id, movie_name));
				}
				_listMovieTypes.add(new MovieType(movie_type_id,
						movie_type_name, _listMovie));
			}
			movieTypeWrapper = new MovieTypeWrapper(_listMovieTypes);
			dataProvider = new DataProvider(_listVideo, movieTypeWrapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataProvider;
	}
}