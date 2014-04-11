package vn.techplus.youtube.fragment;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.R;
import vn.techplus.filmon.youtube.model.MovieParser;
import vn.techplus.global.Statics;
import vn.techplus.youtube.adapter.VideoGridAdapter;
import vn.techplus.youtube.model.YoutubeVideo;
import vn.techplus.youtube.player.OpenYouTubePlayerActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

public class ListVideosFragment extends Fragment {

	private ProgressBar videoProgress; // dat ten co nghia mot ti
	// private ProgressDialog pDialog;
	private String catId;
	private String MY_CAT_ID = "-1";
	private ArrayList<YoutubeVideo> arrVideos = new ArrayList<YoutubeVideo>();
	private VideoGridAdapter adapterVideo;
	private GridView gvVideos;
	private static String KEY = "CATID";
	// khai ta bien moi truong
	private Context mContext;

	public static ListVideosFragment create(String catId) {
		ListVideosFragment lvFrag = new ListVideosFragment();
		Bundle bundle = new Bundle();
		bundle.putString(KEY, catId);
		lvFrag.setArguments(bundle);
		return lvFrag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		catId = getArguments().getString(KEY);
		adapterVideo = new VideoGridAdapter(getActivity(),
				R.layout.channel_item, arrVideos);

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		new getVideos().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.video_grid, container, false);
		gvVideos = (GridView) rootView.findViewById(R.id.gridVideos);
		videoProgress = (ProgressBar) rootView
				.findViewById(R.id.progressBarVideo);
		gvVideos.setAdapter(adapterVideo);
		gvVideos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String videoId = arrVideos.get(arg2).getVideoId();

				if (videoId == null || videoId.trim().equals("")) {
					return;
				}
				Intent lVideoIntent = new Intent(null, Uri.parse("ytv://"
						+ videoId), getActivity(),
						OpenYouTubePlayerActivity.class);

				startActivity(lVideoIntent);

			}
		});
		return rootView;

	}

	public class getVideos extends
			AsyncTask<String, String, List<YoutubeVideo>> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			videoProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<YoutubeVideo> doInBackground(String... params) {
			List<YoutubeVideo> list = null;
			if (catId.equals(MY_CAT_ID)) {
				list = MovieParser.getMyVideos();
			} else {
				list = MovieParser.getVideosByCategory(catId);
			}
			if (list != null && list.size() > 0)
				return list;
			else
				return null;
		}

		@Override
		protected void onPostExecute(List<YoutubeVideo> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			videoProgress.setVisibility(View.GONE);// thu lai di
			if (result != null && result.size() > 0) {

				if (arrVideos != null)
					arrVideos.clear();

				arrVideos.addAll(result);
				adapterVideo.notifyDataSetChanged();
			}

		}

	}

}