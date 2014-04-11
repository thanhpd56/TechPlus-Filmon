package vn.techplus.youtube.fragment;

import vn.techplus.filmon.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ListVideosFragment extends Fragment{
	
	public static ListVideosFragment create(){
		ListVideosFragment lvFrag = new ListVideosFragment();
		return lvFrag;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Create", Toast.LENGTH_SHORT).show();
		View rootView =  inflater.inflate(R.layout.demo, container, false);
		return rootView;
		
	}
	
	
}