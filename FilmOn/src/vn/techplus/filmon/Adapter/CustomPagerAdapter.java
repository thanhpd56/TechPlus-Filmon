package vn.techplus.filmon.Adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mArrayFragments;
	
	public CustomPagerAdapter(FragmentManager fm, ArrayList<Fragment> arrayFragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		mArrayFragments = arrayFragments;
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
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}
	
}
