package vn.techplus.filmon.customwidget;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class CustomViewPager extends ViewPager {
	private boolean enabled;
	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.enabled = true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.enabled) {
            return super.onTouchEvent(event);
        }
  
        return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
	}
	public void setPagingEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		this.enabled = enabled;
	}
}
