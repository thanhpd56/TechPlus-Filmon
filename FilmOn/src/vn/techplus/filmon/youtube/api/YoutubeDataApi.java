package vn.techplus.filmon.youtube.api;

public class YoutubeDataApi {
	private static YoutubeDataApi youtubeDataApi;
	private static final String TAG = "YouTubeApi";
	private static final String API_KEY = "";
	
	interface VideoCallback {
		public void onVideoCompleted();
		public void onVideoFailed();
	}
}
