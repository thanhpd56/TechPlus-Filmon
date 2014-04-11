package vn.techplus.youtube.model;

import vn.techplus.global.Statics;
import android.content.Context;
import android.widget.ImageView;

public class CustomImageView extends ImageView{

	public CustomImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		
		// force to custom ratio
		int heigh = Math.round(width * Statics.IMAGE_RATIO);
		setMeasuredDimension(width, heigh);
	}

}
