package vn.techplus.filmon.youtube.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.techplus.filmon.utils.JSONParser;
import vn.techplus.filmon.youtube.entity.Movie;
import vn.techplus.filmon.youtube.entity.MovieType;
import vn.techplus.filmon.youtube.entity.MovieTypeWrapper;
import vn.techplus.filmon.youtube.entity.Video;
import vn.techplus.filmon.youtube.entity.DataProvider;
import vn.techplus.global.Statics;
import vn.techplus.youtube.model.YoutubeCategory;
import vn.techplus.youtube.model.YoutubeManagement;
import vn.techplus.youtube.model.YoutubeVideo;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

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
	// tag youtube movie
	private static String TAG_YVIDEO = "links";
	private static String TAG_YVIDEO_ID = "link_id";
	private static String TAG_YVIDEO_TITLE = "link_title";
	private static String TAG_YVIDEO_URL = "link_video";
	private static String TAG_YVIDEO_THUMB = "link_thumb";
	// tag youtube category 
		private static final String TAG_CAT = "categories";
		private static final String TAG_CAT_ID = "cat_id";
		private static final String TAG_CAT_NAME = "cat_name";
		private static final String TAG_CAT_PARENT = "cat_parent";

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
	
	public static SparseArray<Object> getVideos(Context context){
		String url = Statics.URL_GET_ALL_VIDEOS;
		JSONParser parser = new JSONParser();
		JSONArray jArray = parser.getJSONArrayFromUrl(url);
		if(jArray == null)
			return null;
		
		int total = jArray.length();
		YoutubeManagement youtubeManagement = YoutubeManagement.getInstance();
		HashMap<String, List<YoutubeVideo>> dictVideos = new HashMap<String, List<YoutubeVideo>>();
		List<YoutubeCategory> categories = new ArrayList<YoutubeCategory>();
		List<YoutubeVideo> videos = null;
		for(int i=0; i<total; i++){
			JSONObject jVideo = jArray.optJSONObject(i);
			if(jVideo == null)
				continue;
			YoutubeVideo yVideo = new YoutubeVideo();
			yVideo.setId(jVideo.optInt(TAG_YVIDEO_ID));
			yVideo.setTitle(jVideo.optString(TAG_YVIDEO_TITLE));
			yVideo.setVideoId(jVideo.optString(TAG_YVIDEO_URL));
			yVideo.setImageThumb(jVideo.optString(TAG_YVIDEO_THUMB));YoutubeVideo video = new YoutubeVideo();
			
		}
		return null;
	}
	public static List<YoutubeVideo> getVideosByCategory(String catId){
		List<YoutubeVideo> mVideos = new ArrayList<YoutubeVideo>();
		
		JSONParser jsonParser = new JSONParser();
		String url = Statics.URL_GET_VIDEOS_BY_ID + catId;
		JSONObject json = jsonParser.getJSONFromUrl(url);
		if(json == null)
			return null;
		Log.i(TAG, json.toString());
		try {
			JSONArray videos = json.getJSONArray(TAG_YVIDEO);
			for (int i = 0; i < videos.length(); i++) {
				JSONObject video = videos.getJSONObject(i);
				YoutubeVideo yVideo = new YoutubeVideo();
				yVideo.setId(video.getInt(TAG_YVIDEO_ID));
				yVideo.setTitle(video.getString(TAG_YVIDEO_TITLE));
				yVideo.setVideoId(video.getString(TAG_YVIDEO_URL));
				yVideo.setImageThumb(video.getString(TAG_YVIDEO_THUMB));
				mVideos.add(yVideo);
			}
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
		}
		return mVideos;
	}	

	public static List<YoutubeCategory> getYoutubeCagegories(){
		List<YoutubeCategory> mCats = new ArrayList<YoutubeCategory>();
		
		JSONParser jsonParser = new JSONParser();
		String url = Statics.URL_GET_CATS;
		JSONObject json = jsonParser.getJSONFromUrl(url);
		if(json == null)
			return null;
		Log.i(TAG, json.toString());
		try{
			JSONArray arrCatsJson = json.getJSONArray(TAG_CAT);
			for(int i=0; i<arrCatsJson.length(); i++){
				JSONObject catJson = arrCatsJson.getJSONObject(i);
				YoutubeCategory yCat = new YoutubeCategory();
				yCat.setId(catJson.getInt(TAG_CAT_ID));
				yCat.setName(catJson.getString(TAG_CAT_NAME));
				yCat.setParentId(catJson.getInt(TAG_CAT_PARENT));
				mCats.add(yCat);
			}
		}catch(JSONException e){
			Log.i(TAG, e.getMessage());
		}
		return mCats;
		
	}
	public static List<YoutubeVideo> getMyVideos(){
		List<YoutubeVideo> arrVideos = new ArrayList<YoutubeVideo>();
		
		// Demo hardcode data
		YoutubeVideo video1 = new YoutubeVideo();
		video1.setId(0);
		video1.setImageThumb("https://i1.ytimg.com/vi/-Ib86ZmUBOY/hqdefault.jpg");
		video1.setTitle("My favourite");
		video1.setVideoId("HQLHygQwV7Q");
		
		YoutubeVideo video2 = new YoutubeVideo();
		video2.setId(0);
		video2.setImageThumb("https://i1.ytimg.com/vi/-Ib86ZmUBOY/hqdefault.jpg");
		video2.setTitle("My favourite2");
		video2.setVideoId("HQLHygQwV7Q");
		arrVideos.add(video1);
		arrVideos.add(video2);
		return arrVideos;
		
	}
}