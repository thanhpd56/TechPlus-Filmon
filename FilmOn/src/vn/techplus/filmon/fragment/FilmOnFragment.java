package vn.techplus.filmon.fragment;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.ListChannelsFragment;
import vn.techplus.filmon.R;
import vn.techplus.filmon.Adapter.CategoryAdapter;
import vn.techplus.filmon.Adapter.CustomPagerAdapter;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.model.ChannelManagement;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FilmOnFragment extends Fragment implements OnClickListener,
		OnPageChangeListener, OnItemClickListener {
	private Context mContext;
	private ViewPager mViewPager;
	private CustomPagerAdapter mPagerAdapter;
	private ListView mListCategories;
	private CategoryAdapter mCategoriesAdapter;
	private List<Category> mCategories = new ArrayList<Category>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity().getApplicationContext();
		mCategories = ChannelManagement.getInstance().getCategories();
		if (mCategories == null)
			mCategories = new ArrayList<Category>();
		mPagerAdapter = new CustomPagerAdapter(getActivity()
				.getSupportFragmentManager(), generateFragments());
		mCategoriesAdapter = new CategoryAdapter(getActivity(),
				R.layout.category_item, mCategories);
	}

	private ArrayList<Fragment> generateFragments() {
		ArrayList<Fragment> array = new ArrayList<Fragment>();
		for (int i = 0; i < mCategories.size(); i++) {
			array.add(ListChannelsFragment.newInstance(i));
		}
		return array;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_filmon, null);
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		mListCategories = (ListView) view.findViewById(R.id.listCategories);
		mListCategories.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListCategories.setOnItemClickListener(this);
		mListCategories.setAdapter(mCategoriesAdapter);
		if (mCategories.size() > 0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				
				mListCategories.setActivated(true);
			mListCategories.setSelected(true);
			mListCategories.setSelection(0);
			mListCategories.setItemChecked(0, true);
		}
		return view;
	}

	public void setAdapter(CustomPagerAdapter customPagerAdapter,
			CategoryAdapter mCategoriesAdapter) {
		mViewPager.setAdapter(customPagerAdapter);
		mListCategories.setAdapter(mCategoriesAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Category category = mCategoriesAdapter.getItem(arg2);
		if (category != null) {
			mViewPager.setCurrentItem(arg2);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mListCategories.setSelection(arg0);
		mListCategories.setItemChecked(arg0, true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
