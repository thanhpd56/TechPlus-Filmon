package vn.techplus.filmon.Adapter;

import java.util.List;

import vn.techplus.filmon.model.Channel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListChannelAdapter extends ArrayAdapter<Channel> {
	private LayoutInflater mInflater;
	private List<Channel> mChannels;
	private int mLayoutId;
	
	
	public ListChannelAdapter(Context context, int resource, List<Channel> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		mLayoutId = resource;
		mChannels = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView txtView = (TextView) convertView;
		if (txtView == null) {
			txtView = (TextView) mInflater.inflate(mLayoutId, null);
		}
		
		Channel channel = mChannels.get(position);
		txtView.setText(channel.getName());
		return txtView;
	}
	
}
