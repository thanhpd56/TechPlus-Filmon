package vn.techplus.filmon.customwidget;

import vn.techplus.filmon.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomeTextView, 0, 0);
		
		try {
			String nameTypeFace = typedArray.getString(R.styleable.CustomeTextView_customTypeFace);
			if (!isInEditMode() &&!TextUtils.isEmpty(nameTypeFace)) {
//				Log.w(getClass().getName(), nameTypeFace);
				Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + nameTypeFace);
				if (typeface != null) {
					setTypeface(typeface);
					setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			typedArray.recycle();
		}
	}

}
