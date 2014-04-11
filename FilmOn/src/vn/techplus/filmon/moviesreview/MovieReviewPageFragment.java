package vn.techplus.filmon.moviesreview;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.R;
import vn.techplus.filmon.utils.Log;
import vn.techplus.filmon.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MovieReviewPageFragment extends Fragment {
	private static final String EXTRA_PAGE_INDEX = "Page Index";
	// state
	private static final int STATE_LOADING = 1;
	private static final int STATE_LOAD_SUCCESS = 2;
	private static final int STATE_LOAD_FAILED = 3;

	private GridView mGridMovie;
	private TextView mTxtRetry;
	private ProgressBar mProgressBar;
	
	private int mNumberCol = 0;
	private int mPageIndex;
	private List<MovieInfo> mMovieInfos;
	private MovieReviewAdapter mReviewAdapter;
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (mMovieInfos == null || mMovieInfos.size() < arg2)
				return;
			
			if (arg2 == mMovieInfos.size()) {
				new AsyncLoadingMovie().execute();
			} 
			else {			
				
				MovieInfo movieInfo = mMovieInfos.get(arg2);
				Intent iReviewDetail = new Intent(getActivity().getApplicationContext(), MovieReviewDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable(MovieReviewDetailActivity.EXTRA_MOVIE, movieInfo);
				iReviewDetail.putExtras(bundle);
				startActivity(iReviewDetail);
			}
		}
		
	};
	
	private OnClickListener mRetryClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (Utils.isNetworkConnected(getActivity())) {
				new AsyncLoadingMovie().execute();
			}
			else 
				changeState(STATE_LOAD_FAILED);
		}
	};
	
	
	public static MovieReviewPageFragment newInstance(int pageIndex) {
		MovieReviewPageFragment frag = new MovieReviewPageFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(EXTRA_PAGE_INDEX, pageIndex);
		frag.setArguments(bundle);
		return frag;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);		
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Log.i("OnCreate " + getClass().getSimpleName() + ":" + mNumberCol);
		
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mPageIndex = bundle.getInt(EXTRA_PAGE_INDEX, 0);
		}
		Point windowSize = Utils.getSizeWindowDp(getActivity());
		float rowHeight = getResources().getDimension(R.dimen.height_row_grid);
		float colWidth = rowHeight * 2 / 3;
		mNumberCol = (int)(windowSize.x / colWidth);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Log.i("OnCreateView " + getClass().getSimpleName() + ":" + mNumberCol);
		
		View view = inflater.inflate(R.layout.movie_review_page, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mTxtRetry = (TextView) view.findViewById(R.id.txtRetry);
		mTxtRetry.setOnClickListener(mRetryClickListener);
		mGridMovie = (GridView) view.findViewById(R.id.gridMovieReview);
		if (mNumberCol > 0)
			mGridMovie.setNumColumns(mNumberCol);
		mGridMovie.setOnItemClickListener(mItemClickListener);
		if (mReviewAdapter == null) {
			
			if (mMovieInfos == null) {
				if (Utils.isNetworkConnected(getActivity()))
					new AsyncLoadingMovie().execute();
				else
					changeState(STATE_LOAD_FAILED);
			}
			else {
				mReviewAdapter = new MovieReviewAdapter(getActivity(), mMovieInfos);
				mGridMovie.setAdapter(mReviewAdapter);
			}
		}
		else {
			mGridMovie.setAdapter(mReviewAdapter);
		}
		return view;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Log.i("OnDestroy " + getClass().getSimpleName() + ":" + mNumberCol);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		//Log.i("OnDestroyView " + getClass().getSimpleName() + ":" + mNumberCol);
	}

	private void changeState(int loadingState) {		
		switch (loadingState) {
		case STATE_LOADING :
			mTxtRetry.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
			break;
			
		case STATE_LOAD_SUCCESS :
			mTxtRetry.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.GONE);
			break;
			
		case STATE_LOAD_FAILED :
			mTxtRetry.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			break;
			
			default :
				mTxtRetry.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
				break;				
		}
	}

	
	class AsyncLoadingMovie extends AsyncTask<Void, Void, List<MovieInfo>> {
		
		@Override
		protected List<MovieInfo> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MovieManager manager = MovieManager.getInstance();
			switch (mPageIndex) {
			case 0 :
				return manager.getMoreTopRated();
			case 1 :
				return manager.getMorePopular();
			case 2 :
				return manager.getMoreNowPlaying();
			case 3 :
				return manager.getMoreUpComing();
				
				default: 
					break;
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(List<MovieInfo> result) {
			if (result == null || result.size() == 0) {
				changeState(STATE_LOAD_FAILED);
				return;
			}
			
			changeState(STATE_LOAD_SUCCESS);
			if (mMovieInfos == null)
				mMovieInfos = new ArrayList<MovieInfo>(result);
			else {
				mMovieInfos.clear();
				mMovieInfos.addAll(result);
			}
			
			if (mReviewAdapter == null) {
				mReviewAdapter = new MovieReviewAdapter(getActivity(), mMovieInfos);
				if (mGridMovie != null)
					mGridMovie.setAdapter(mReviewAdapter);
			} else {
				mReviewAdapter.notifyDataSetChanged();
			}
			
		}

		@Override
		protected void onPreExecute() {
			//super.onPreExecute();
			changeState(STATE_LOADING);
		}
		
	}
}
