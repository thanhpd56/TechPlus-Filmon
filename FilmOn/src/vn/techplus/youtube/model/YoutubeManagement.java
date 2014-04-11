package vn.techplus.youtube.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.youtube.model.MovieParser;
import vn.techplus.youtube.fragment.ListVideosFragment;

public class YoutubeManagement {

	private static final String MY_CATEGORY_ID = "-1";

	private static YoutubeManagement mManager;

	private List<YoutubeCategory> mYoutubeCategories;
	private List<YoutubeVideo> mYoutubeVideos;
	private HashMap<String, List<YoutubeVideo>> mAllVideos;
	private List<YoutubeVideo> mMyVideos;

	private YoutubeCateCallback onYoutubeCateCallback;

	public interface YoutubeCateCallback {
		public void onCompleted(boolean isCompleted);
	}

	public static YoutubeManagement getInstance() {
		if (mManager == null) {
			synchronized (YoutubeManagement.class) {
				if (mManager == null)
					mManager = new YoutubeManagement();
			}
		}

		return mManager;
	}

	private YoutubeManagement() {
	};

	public void release() {
		clear();
		mManager = null;
	}

	/**
	 * Remove all data
	 */
	public void clear() {
		if (mYoutubeCategories != null) {
			mYoutubeCategories.clear();
		}
		if (mYoutubeVideos != null) {
			mYoutubeVideos.clear();
		}
	}

	/**
	 * Load new data to replace for old data, when finish loading data, callback
	 * 
	 * @return
	 */
	public void loadYoutubeCate(YoutubeCateCallback youtubeCateCallback) {
		onYoutubeCateCallback = youtubeCateCallback;
		new getYoutubeCate().execute();
	}

	public List<YoutubeVideo> getVideosByCategory(Context context,
			YoutubeCategory category) {
		List<YoutubeVideo> videos = null;
		if (String.valueOf(category.getId()).equals(MY_CATEGORY_ID)) {
			// retrieve list my videos
			videos = new ArrayList<YoutubeVideo>(mMyVideos);
		} else {
			// retrieve list videos in the same category
			if (mAllVideos != null) {
				videos = mAllVideos.get(category.getId());
			}
			if (videos == null && Looper.myLooper() != Looper.getMainLooper()) {
				videos = MovieParser.getVideosByCategory(String
						.valueOf(category.getId()));
				if (mAllVideos == null) {
					mAllVideos = new HashMap<String, List<YoutubeVideo>>();

				}
				mAllVideos.put(String.valueOf(category.getId()), videos);

			}
		}
		return videos;
	}

	/**
	 * Load list favourite videos from database to cache
	 * 
	 * @return list
	 */
	public List<YoutubeVideo> loadMyVideos(Context context) {
		return null;
	}

	/**
	 * Add a video to list favourite videos
	 * 
	 * @param context
	 */
	public void addMyVideos(Context context, YoutubeVideo video) {

	}

	public void updateCategoryYoutube(Context context) {
		List<YoutubeCategory> arrCats = MovieParser.getYoutubeCagegories();
		if (arrCats != null && arrCats.size() > 0) {
			if (mAllVideos == null) {
				mYoutubeCategories = new ArrayList<YoutubeCategory>();

			} else {
				mYoutubeCategories.clear();
				mYoutubeCategories.addAll(arrCats);
			}
			mYoutubeCategories.add(0,
					new YoutubeCategory(Integer.parseInt(MY_CATEGORY_ID),
							FilmOnApplication.myCategory, 0));
			if (mAllVideos != null) {
				mAllVideos.clear();
			}
		}
	}

	private void updateVideosYoutube(Context context) {
		// SparseArray<Object> arrary = MovieParser.get
	}

	public List<YoutubeCategory> getmYoutubeCategories() {
		return mYoutubeCategories;
	}

	public void setmYoutubeCategories(List<YoutubeCategory> mYoutubeCategories) {
		this.mYoutubeCategories = mYoutubeCategories;
	}

	public void updateYoutubeCat() {

	}

	private class getYoutubeCate extends
			AsyncTask<String, String, List<YoutubeCategory>> {

		@Override
		protected List<YoutubeCategory> doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<YoutubeCategory> list = new ArrayList<YoutubeCategory>();
			List<YoutubeCategory> normalList = new ArrayList<YoutubeCategory>();
			YoutubeCategory myMoviesCategory = new YoutubeCategory();
			myMoviesCategory.setId(Integer.parseInt(MY_CATEGORY_ID));
			myMoviesCategory.setName("MY MOVIES");
			list.add(myMoviesCategory);
			normalList = MovieParser.getYoutubeCagegories();
			list.addAll(normalList);
			if (list != null && list.size() > 0)
				return list;

			return null;

		}

		@Override
		protected void onPostExecute(List<YoutubeCategory> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null && result.size() > 0) {
				if (mYoutubeCategories == null)
					mYoutubeCategories = new ArrayList<YoutubeCategory>();
				if (mYoutubeCategories != null)
					mYoutubeCategories.clear();
				mYoutubeCategories.addAll(result);
				if (mYoutubeCategories != null && mYoutubeCategories.size() > 0)
					if (onYoutubeCateCallback != null)
						onYoutubeCateCallback.onCompleted(true);
					else
						onYoutubeCateCallback.onCompleted(false);
			}
		}

	}
}
