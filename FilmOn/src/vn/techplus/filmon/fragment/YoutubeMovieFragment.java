package vn.techplus.filmon.fragment;

import io.vov.vitamio.utils.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import vn.techplus.filmon.R;
import vn.techplus.filmon.Adapter.CustomPagerAdapter;
import vn.techplus.filmon.youtube.model.MovieParser;
import vn.techplus.global.Statics;
import vn.techplus.youtube.adapter.YoutubeCategoryAdapter;
import vn.techplus.youtube.fragment.ListVideosFragment;
import vn.techplus.youtube.model.YoutubeCategory;
import vn.techplus.youtube.model.YoutubeManagement;
import vn.techplus.youtube.model.YoutubeManagement.YoutubeCateCallback;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class YoutubeMovieFragment extends Fragment implements
		OnPageChangeListener, YoutubeCateCallback {

	// FOR CATEGORIES
	private static final String TAG_CAT = "categories";
	private static final String TAG_CAT_ID = "cat_id";
	private static final String TAG_CAT_NAME = "cat_name";
	private static final String TAG_CAT_PARENT = "cat_parent";
	ArrayList<YoutubeCategory> arrCategories = new ArrayList<YoutubeCategory>();
	YoutubeCategoryAdapter adapterCategory;
	private ListView lvCats;
	private ArrayAdapter<String> adapterCats;
	private YoutubeManagement youtubeManagement;

	// FOR VIDEOS
	private ViewPager vpVideos;
	private CustomPagerAdapter adapterPager;
	private ArrayList<Fragment> arrFragments = new ArrayList<Fragment>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		adapterPager = new CustomPagerAdapter(getActivity()
				.getSupportFragmentManager(), arrFragments);

		youtubeManagement = YoutubeManagement.getInstance();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_youtube, container,
				false);
		lvCats = (ListView) rootView.findViewById(R.id.lvCats);
		lvCats.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		adapterCategory = new YoutubeCategoryAdapter(getActivity(),
				R.layout.category_item, arrCategories);
		lvCats.setAdapter(adapterCategory);

		lvCats.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				vpVideos.setCurrentItem(arg2);

			}
		});
		vpVideos = (ViewPager) rootView.findViewById(R.id.vpVideos);
		vpVideos.setAdapter(adapterPager);
		// em quen chua set listener cho no roi go di may anh ko go dc
		vpVideos.setOnPageChangeListener(this);
		// roi thu di

		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)

				lvCats.setActivated(true);
			lvCats.setSelection(0);
			lvCats.setItemChecked(0, true);
		}
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		youtubeManagement.loadYoutubeCate(this);
	}

	// public class getData extends AsyncTask<String, String,
	// List<YoutubeCategory>> {
	//
	// @Override
	// protected List<YoutubeCategory> doInBackground(String... params) {
	// // TODO Auto-generated method stub
	// List<YoutubeCategory> list = new ArrayList<YoutubeCategory>();
	// list = MovieParser.getYoutubeCagegories();
	// if(list != null && list.size()>0)
	// return list;
	// return null;
	//
	// }
	//
	// @Override
	// protected void onPostExecute(List<YoutubeCategory> result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// if(result!= null && result.size()>0){
	// if(arrCategories == null)
	// arrCategories.clear();
	// arrCategories.addAll(result);
	//
	// }
	// for (int i = 0; i < arrCategories.size(); i++) {
	// arrFragments.add(ListVideosFragment.create(String
	// .valueOf(arrCategories.get(i).getId())));
	// }
	// adapterPager.notifyDataSetChanged();
	//
	// adapterCategory.notifyDataSetChanged();
	//
	// }
	//
	// }

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
		lvCats.setSelection(arg0);
		lvCats.setItemChecked(arg0, true);
	}

	@Override
	public void onCompleted(boolean isCompleted) {
		// TODO Auto-generated method stub
		if (isCompleted) {
			List<YoutubeCategory> list = youtubeManagement
					.getmYoutubeCategories();
			if (list.size() > 0) {
				arrCategories.addAll(list);
				for (int i = 0; i < arrCategories.size(); i++) {
					arrFragments.add(ListVideosFragment.create(String
							.valueOf(arrCategories.get(i).getId())));
				}
				adapterPager.notifyDataSetChanged();
				adapterCategory.notifyDataSetChanged();
			}

		}

	}
}