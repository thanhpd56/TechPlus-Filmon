package vn.techplus.filmon.Adapter;

import java.util.List;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.R;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.model.ManualChannel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryAdapter extends ArrayAdapter<Category> {
	public List<Category> mCategories;
	private int mResource;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	
	public CategoryAdapter(Context context, int resource, List<Category> categories) {
		super(context, resource, categories);
		// TODO Auto-generated constructor stub
		mCategories = categories;
		mInflater = LayoutInflater.from(context);
		mResource = resource;
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = FilmOnApplication.getDisplayOption(0);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;		
		if (view == null) {
			view = mInflater.inflate(mResource, null);
			holder = new ViewHolder();
			holder.imgView = (ImageView) view.findViewById(R.id.imgCategory);
			holder.txtView = (TextView) view.findViewById(R.id.txtCategoryItem);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		Category category = mCategories.get(position);
		holder.txtView.setText(category.getName());
		if (category.getId().equals(ChannelManagement.MY_CATEGORY_ID))
			holder.imgView.setImageResource(R.drawable.my_channel);
		else if (category.getId().equals(ManualChannel.SPECIAL_CATEGORY_ID))
			holder.imgView.setImageResource(R.drawable.special_offer);
		else
			mImageLoader.displayImage(category.getLogo(), holder.imgView, mOptions);
		
		return view;
	}

	
	class ViewHolder {
		public ImageView imgView;
		public TextView txtView;
	}
}
