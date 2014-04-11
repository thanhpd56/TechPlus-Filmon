package vn.techplus.youtube.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.R;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.model.ManualChannel;
import vn.techplus.youtube.model.YoutubeCategory;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class YoutubeCategoryAdapter extends ArrayAdapter<YoutubeCategory> {
	public ArrayList<YoutubeCategory> arrCats;
	private int mResource;
	private int resourceId;
	private Context context;

	public YoutubeCategoryAdapter(Context context, int resource,
			ArrayList<YoutubeCategory> categories) {
		super(context, resource, categories);
		// TODO Auto-generated constructor stub
		arrCats = categories;
		resourceId = resource;
		this.context = context;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;
		// init view
		if(view == null){
			holder = new ViewHolder();
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(resourceId, parent, false);
			holder.txtView = (TextView) view.findViewById(R.id.txtCategoryItem);
			view.setTag(holder);
		}else{
			holder = (ViewHolder) view.getTag();
			
		}
		YoutubeCategory yCat = arrCats.get(position);
		holder.txtView.setText(yCat.getName());
		return view;
	}
	class ViewHolder {
		public TextView txtView;
	}
}
