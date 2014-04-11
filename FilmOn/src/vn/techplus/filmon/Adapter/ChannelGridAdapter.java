package vn.techplus.filmon.Adapter;

import java.util.List;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.R;
import vn.techplus.filmon.model.Channel;
import vn.techplus.filmon.model.ChannelManagement;
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

public class ChannelGridAdapter extends ArrayAdapter<Channel> {
	
	private List<Channel> mArray;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
		
	public ChannelGridAdapter(Context context, List<Channel> array) {
		super(context, R.layout.channel_item, array);
		mInflater = LayoutInflater.from(context);
		mArray = array;
		mImageLoader = ImageLoader.getInstance();
		mOptions = FilmOnApplication.getDisplayOption(R.drawable.ic_channel_default);
	}

	@Override
	public Channel getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArray.get(arg0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.channel_item, null);
			holder = new ViewHolder();
			holder.imgLogo = (ImageView) view.findViewById(R.id.imgLogoChannel);
			holder.txtName = (TextView) view.findViewById(R.id.txtChannel);
			holder.toggleMyChannel = (ToggleButton) view.findViewById(R.id.toggleMyChannel);
			holder.logoHD = view.findViewById(R.id.imgHD);
			
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}
		
		// set Content to view
		Channel channel = mArray.get(position);
		holder.txtName.setText(channel.getName());
		mImageLoader.displayImage(channel.getLogo(), holder.imgLogo, mOptions);
		holder.toggleMyChannel.setChecked(channel.isMyChannel());		
		holder.toggleMyChannel.setTag(channel.getId());
		holder.toggleMyChannel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				String channelId = (String) buttonView.getTag();
				for (Channel channel: mArray) {
					if (channel.getId().equals(channelId)) {
						channel.setMyChannel(isChecked);
						if (isChecked)
							ChannelManagement.getInstance().addMyChannels(getContext(), channel);
						else
							ChannelManagement.getInstance().removeMyChannels(getContext(), channelId);
						break;
					}
				}
			}
		});
		if (channel.isHD()) {
			holder.logoHD.setVisibility(View.VISIBLE);
		}
		else {
			holder.logoHD.setVisibility(View.GONE);
		}
		
		return view;
	}

	class ViewHolder {
		public ImageView imgLogo;
		public TextView txtName;
		public ToggleButton toggleMyChannel;
		public View logoHD;
	}
}
