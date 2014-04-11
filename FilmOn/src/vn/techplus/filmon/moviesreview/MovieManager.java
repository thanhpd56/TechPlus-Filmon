package vn.techplus.filmon.moviesreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vn.techplus.filmon.utils.Log;

import com.omertron.themoviedbapi.MovieDbException;

public class MovieManager {
	private static MovieManager mManager;
	
	private List<MovieInfo> mTopRated;
	private List<MovieInfo> mPopular;
	private List<MovieInfo> mNowPlaying;
	private List<MovieInfo> mUpComing;
	
	private HashMap<String, MovieInfo> mDictFullDetails;
	
	
	public static MovieManager getInstance(){
		if (mManager == null) {
			synchronized (MovieManager.class) {
				if (mManager == null) {
					mManager = new MovieManager();
				}
			}
		}
		return mManager;
	}
	
	public static void release() {
		mManager = null;
	}
	
	private MovieManager() {
		mTopRated = new ArrayList<MovieInfo>();
		mPopular = new ArrayList<MovieInfo>();
		mNowPlaying = new ArrayList<MovieInfo>();
		mUpComing = new ArrayList<MovieInfo>();
		mDictFullDetails = new HashMap<String, MovieInfo>();
	}
	
	public List<MovieInfo> getTopRated() {
		if (mTopRated != null && mTopRated.size() > 0) {
			return mTopRated;
		}
		
		try {
			MovieReview review = new TheMoviesDB();
			mTopRated.clear();
			mTopRated.addAll(review.getTopRated());
		} catch (MovieDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mTopRated;
	}
	
	public List<MovieInfo> getMoreTopRated() {
		if (mTopRated == null || mTopRated.size() <= 0) {
			return getTopRated();
		}
		else if (mTopRated.size() % 20 == 0){
			
			int nextPage = (mTopRated.size() / 20) + 1;
			Log.i("next page: " + nextPage);
			try {
				MovieReview review = new TheMoviesDB();
				mTopRated.addAll(review.getTopRated(nextPage));
			} catch (MovieDbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return mTopRated;
	}
	
	public List<MovieInfo> getPopular() {
		if (mPopular != null && mPopular.size() > 0) {
			return mPopular;
		}
		
		try {
			MovieReview review = new TheMoviesDB();
			mPopular.clear();
			mPopular.addAll(review.getPopular());
		} catch (MovieDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mPopular;
	}
	
	public List<MovieInfo> getMorePopular() {
		if (mPopular == null || mPopular.size() <= 0) {
			return getPopular();
		}
		else if (mPopular.size() % 20 == 0){
			
			int nextPage = (mPopular.size() / 20) + 1;
			Log.i("next page: " + nextPage);
			try {
				MovieReview review = new TheMoviesDB();
				mPopular.addAll(review.getPopular(nextPage));
			} catch (MovieDbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return mPopular;
	}
	
	public List<MovieInfo> getNowPlaying() {
		if (mNowPlaying != null && mNowPlaying.size() > 0) {
			return mNowPlaying;
		}
		
		try {
			MovieReview review = new TheMoviesDB();
			mNowPlaying.clear();
			mNowPlaying.addAll(review.getNowPlaying());
		} catch (MovieDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mNowPlaying;
	}
	
	public List<MovieInfo> getMoreNowPlaying() {
		if (mNowPlaying == null || mNowPlaying.size() <= 0) {
			return getNowPlaying();
		}
		else if (mNowPlaying.size() % 20 == 0){
			
			int nextPage = (mNowPlaying.size() / 20) + 1;
			Log.i("next page: " + nextPage);
			try {
				MovieReview review = new TheMoviesDB();
				mNowPlaying.addAll(review.getNowPlaying(nextPage));
			} catch (MovieDbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return mNowPlaying;
	}
	
	public List<MovieInfo> getUpComing() {
		if (mUpComing != null && mUpComing.size() > 0) {
			return mUpComing;
		}
		
		try {
			MovieReview review = new TheMoviesDB();
			mUpComing.clear();
			mUpComing.addAll(review.getUpcoming());
		} catch (MovieDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mUpComing;
	}
	
	public List<MovieInfo> getMoreUpComing() {
		if (mUpComing == null || mUpComing.size() <= 0) {
			return getUpComing();
		}
		else if (mUpComing.size() % 20 == 0){
			
			int nextPage = (mUpComing.size() / 20) + 1;
			Log.i("next page: " + nextPage);
			try {
				MovieReview review = new TheMoviesDB();
				mUpComing.addAll(review.getUpcoming(nextPage));
			} catch (MovieDbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return mUpComing;
	}
	
	public MovieInfo getFullDetailMovie(int movieId) {
		MovieInfo info = mDictFullDetails.get("" + movieId);
		if (info == null) {
			
			MovieReview review;
			try {
				review = new TheMoviesDB();
				info = review.getMoviesInfo(movieId);
				if (info != null) {
					info.setImdbVote(review.getImdbRating(info.getImdbId()));
					mDictFullDetails.put("" + movieId, info);
				}
			} catch (MovieDbException e) {
				e.printStackTrace();
			}
		}
		return info;
	}
}
