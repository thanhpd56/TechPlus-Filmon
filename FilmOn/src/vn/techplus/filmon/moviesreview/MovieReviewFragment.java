package vn.techplus.filmon.moviesreview;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

public class MovieReviewFragment extends Fragment {
	private static final String[] PAGE_TITLES = {"TOP RATED", "POPULAR", "NOW PLAYING", "UP COMING"};
	private ViewPager mViewPager;
	private TabPageIndicator mPageIndicator;
	
	private List<Fragment> mPageFragments;
	private MovieReviewPagerAdapter mPagerAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MovieManager.release();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.movie_review_main, null);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		if (mPageFragments == null)
			mPageFragments = createPageFragments();
		if (mPagerAdapter == null) {
			mPagerAdapter = new MovieReviewPagerAdapter(getChildFragmentManager(), 
					new ArrayList<Fragment>(mPageFragments), PAGE_TITLES);
		}
		mViewPager.setAdapter(mPagerAdapter);
		mPageIndicator = (TabPageIndicator) view.findViewById(R.id.viewIndicator);
		mPageIndicator.setViewPager(mViewPager);		
		return view;
	}

	private List<Fragment> createPageFragments() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(MovieReviewPageFragment.newInstance(0));
		fragments.add(MovieReviewPageFragment.newInstance(1));
		fragments.add(MovieReviewPageFragment.newInstance(2));
		fragments.add(MovieReviewPageFragment.newInstance(3));
		return fragments;
	}
}
