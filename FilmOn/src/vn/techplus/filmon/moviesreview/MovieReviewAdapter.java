package vn.techplus.filmon.moviesreview;

import java.util.List;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MovieReviewAdapter extends ArrayAdapter<MovieInfo> {
	
	private List<MovieInfo> mMovieInfos;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
		
	public MovieReviewAdapter(Context context, List<MovieInfo> movieInfos) {
		super(context, R.layout.movie_review_item, movieInfos);
		mInflater = LayoutInflater.from(context);
		mMovieInfos = movieInfos;
		mImageLoader = ImageLoader.getInstance();
		mOptions = FilmOnApplication.getDisplayOption(R.drawable.ic_app);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount() > 0 ? (super.getCount() + 1) : super.getCount();
	}

	@Override
	public MovieInfo getItem(int arg0) {
		// TODO Auto-generated method stub
		return mMovieInfos.get(arg0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.i(position + " - " + mMovieInfos.size());
		View view = null;
		if (position == mMovieInfos.size()) {
			view = mInflater.inflate(R.layout.movie_review_last_item, null);
		}
		else {
			view = convertView;
			ViewHolder holder;
			if (view == null || view.findViewById(R.id.posterMovie) == null) {
				view = mInflater.inflate(R.layout.movie_review_item, null);
				holder = new ViewHolder();
				holder.imgPoster = (ImageView) view.findViewById(R.id.posterMovie);
				holder.txtTitle = (TextView) view.findViewById(R.id.titleMovie);
				
				view.setTag(holder);
			}
			else {
				holder = (ViewHolder) view.getTag();
			}
			
			// set Content to view
			MovieInfo movieInfo = mMovieInfos.get(position);
			holder.txtTitle.setText(movieInfo.getTitle());
			mImageLoader.displayImage(movieInfo.getPosterImage(), holder.imgPoster, mOptions);
			//Log.i(getClass().getSimpleName(), channel.toString() + holder.toggleMyChannel);
		}
		return view;
	}

	class ViewHolder {
		public ImageView imgPoster;
		public TextView txtTitle;
	}
}
