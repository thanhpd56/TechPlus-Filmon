package vn.techplus.youtube.adapter;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.R;
import vn.techplus.filmon.model.Channel;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.youtube.model.YoutubeVideo;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideoGridAdapter extends ArrayAdapter<YoutubeVideo> {

	Context context;
	int layoutResourceId;
	ArrayList<YoutubeVideo> arrVideos = new ArrayList<YoutubeVideo>();
	//private ImageLoader mImageLoader;
	//private DisplayImageOptions mOptions;

	public VideoGridAdapter(Context context, int resource,
			ArrayList<YoutubeVideo> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layoutResourceId = resource;
		this.arrVideos = objects;
		//mImageLoader = ImageLoader.getInstance();
		//mOptions = FilmOnApplication.getDisplayOption(R.drawable.ic_channel_default);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		RecordHolder holder = null;
		if (row == null) {
			holder = new RecordHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(R.layout.video_item, null);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtVideo);
			holder.imageItem = (ImageView) row.findViewById(R.id.imgLogoVideo);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		YoutubeVideo item = arrVideos.get(position);
		holder.txtTitle.setText(item.getTitle());
		//mImageLoader.displayImage(item.getImageThumb(), holder.imageItem, mOptions);
		Glide.load(item.getImageThumb()).into(holder.imageItem);
		return row; 
	}

	static class RecordHolder {
		TextView txtTitle;
		ImageView imageItem;
	}

}
