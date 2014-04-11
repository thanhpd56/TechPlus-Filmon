package vn.techplus.filmon;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import vn.techplus.filmon.Adapter.ChannelGridAdapter;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.model.Channel;
import vn.techplus.filmon.model.ChannelManagement;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

public class ListChannelsFragment extends Fragment implements Observer {
	private static final String EXTRA_INDEX_CATEGORY = "indexCategory";
	
	private GridView mGridChannels;
	private ChannelGridAdapter mGridAdapter;
	private ProgressBar mProgressBar;
	
	private Context mContext;
	private Category mCategory;	
	private ChannelManagement mManager;
	private AsyncLoadListChannels mLoadChannelsThread;

	public static ListChannelsFragment newInstance(int indexCategory) {
		ListChannelsFragment frag = new ListChannelsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(EXTRA_INDEX_CATEGORY, indexCategory);
		frag.setArguments(bundle);
		return frag;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int index = getArguments().getInt(EXTRA_INDEX_CATEGORY);		
		mManager = ChannelManagement.getInstance();
		mCategory = mManager.getCategories().get(index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view = inflater.inflate(R.layout.channel_grid, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mGridChannels = (GridView) view.findViewById(R.id.gridChannels);		
		mGridChannels.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Channel channel = (Channel) mGridAdapter.getItem(arg2);
				if (channel != null) {
					Intent iPlayer = new Intent(mContext, TVPlayer.class);
					Bundle bundle = new Bundle();
					bundle.putParcelable(TVPlayer.EXTRA_CHANNEL, channel);
					iPlayer.putExtras(bundle);
					mContext.startActivity(iPlayer);
				}
			}
			
		});
		ChannelManagement.getInstance().addObserver(this);
		mLoadChannelsThread = new AsyncLoadListChannels();
		mLoadChannelsThread.execute();	
		
		if (BuildConfig.DEBUG)
			Log.i(getClass().getSimpleName(), "OnCreateView: " + mCategory.toString());
		
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (!getActivity().getResources().getString(R.string.model).equalsIgnoreCase("1"))
			return;
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (true) {
				mGridChannels.setNumColumns(3);
			}
		}
		else {
			if (true) {
				mGridChannels.setNumColumns(2);
			}
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		
		ChannelManagement.getInstance().deleteObserver(this);
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	class AsyncLoadListChannels extends AsyncTask<Void, Void, List<Channel>> {

		@Override
		protected List<Channel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return ChannelManagement.getInstance().getChannelsByCategory(mContext, mCategory);
		}

		@Override
		protected void onPostExecute(List<Channel> result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.GONE);
			
			if (result != null) {
				mGridAdapter = null;
				mGridAdapter = new ChannelGridAdapter(mContext, result);
				mGridChannels.setAdapter(mGridAdapter);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.VISIBLE);
		}
				
	}


	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		if (BuildConfig.DEBUG)
			Log.i(getClass().getSimpleName(), mCategory.toString());
		
		if (Integer.parseInt(mCategory.getId()) >= 0 && mGridAdapter != null) {
			mGridAdapter.notifyDataSetChanged();
		}
		else {	
			if (mLoadChannelsThread != null)
				mLoadChannelsThread.cancel(true);
			mLoadChannelsThread = new AsyncLoadListChannels();
			mLoadChannelsThread.execute();
		}
	}


}
