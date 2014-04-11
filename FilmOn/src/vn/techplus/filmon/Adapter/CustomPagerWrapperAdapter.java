package vn.techplus.filmon.Adapter;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.fragment.FilmOnFragment;
import vn.techplus.filmon.fragment.YoutubeMovieFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomPagerWrapperAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;

	private static final String[] CONTENT = new String[] { "FilmOn", "Movie"};

	public CustomPagerWrapperAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		fragments.add(new FilmOnFragment());
		fragments.add(new YoutubeMovieFragment());
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return fragments.get(position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return CONTENT[position % CONTENT.length].toUpperCase();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}
}
