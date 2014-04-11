package vn.techplus.filmon.fragment;


import java.util.ArrayList;

import vn.techplus.filmon.R;
import vn.techplus.filmon.Adapter.CustomPagerAdapter;
import vn.techplus.youtube.fragment.ListVideosFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class YoutubeMovieFragment extends Fragment{
	
	// FOR CATEGORIES
	String[] arrCats = {"Phan", "Thanh", "Tuong"};
	private ListView lvCats;
	private ArrayAdapter<String> adapterCats;
	
	// FOR VIDEOS
	private ViewPager vpVideos;
	private CustomPagerAdapter adapterPager;
	//private ArrayList<Fragment> arrFragments =  new ArrayList<Fragment>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//adapterPager = new CustomPagerAdapter(getActivity().getSupportFragmentManager(), arrFragments);
		//arrFragments.add(new ListVideosFragment());
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_youtube, container, false);
		lvCats = (ListView) rootView.findViewById(R.id.lvCats);
		adapterCats = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrCats);
		lvCats.setAdapter(adapterCats);
		
		//vpVideos = (ViewPager) rootView.findViewById(R.id.vpVideos);
		//vpVideos.setAdapter(adapterPager);
		return rootView;
	}
}