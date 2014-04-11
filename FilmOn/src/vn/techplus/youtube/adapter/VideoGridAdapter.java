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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VideoGridAdapter extends ArrayAdapter<YoutubeVideo> {

	Context context;
	int layoutResourceId;
	ArrayList<YoutubeVideo> arrItem = new ArrayList<YoutubeVideo>();

	public VideoGridAdapter(Context context, int resource,
			ArrayList<YoutubeVideo> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.layoutResourceId = resource;
		this.arrItem = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		RecordHolder holder = null;
		if (row == null) {
			holder = new RecordHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtChannel);
			holder.imageItem = (ImageView) row.findViewById(R.id.imgLogoChannel);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		YoutubeVideo item = arrItem.get(position);
		holder.txtTitle.setText(item.getTitle() + "");
		String image_url = item.getImageThumb();
		//Glide.load(image_url).centerCrop().into(holder.imageItem);
		// holder.imageItem.setImageBitmap(item.getImage());
		return row;
	}

	static class RecordHolder {
		TextView txtTitle;
		ImageView imageItem;
	}

}
