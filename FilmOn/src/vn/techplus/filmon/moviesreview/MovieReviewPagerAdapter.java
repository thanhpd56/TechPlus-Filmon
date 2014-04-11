package vn.techplus.filmon.moviesreview;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MovieReviewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mArrayFragments;
	private String[] mTitles;
	
	public MovieReviewPagerAdapter(FragmentManager fm, ArrayList<Fragment> arrayFragments) {
		this(fm, arrayFragments, null);
	}
	
	public MovieReviewPagerAdapter(FragmentManager fm, ArrayList<Fragment> arrayFragments, String[] titles) {
		super(fm);
		mArrayFragments = arrayFragments;
		mTitles = titles;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArrayFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		if (mTitles != null && mTitles.length > position)
			return mTitles[position];
		return super.getPageTitle(position);
	}

}
