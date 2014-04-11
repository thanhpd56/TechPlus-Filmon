package vn.techplus.filmon.customwidget;

import vn.techplus.filmon.R;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class PopupView implements OnDismissListener{
	public interface OnPopupDismissListener {
		public void onDismiss();
	}
	
	private PopupWindow mPopupWindow;
//	private Context mContext;
	private View mSubView;
	private OnPopupDismissListener mDismissListener;
	
	public PopupView(Context context) {
//		mContext = context;
		mPopupWindow = new PopupWindow(context);
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		mPopupWindow.setWidth((int)context.getResources().getDimension(R.dimen.popup_list_width));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00FFFFFF));
		mPopupWindow.setOnDismissListener(this);
	}
	
	public void setContentView(View view) {
		mSubView = view;
		mPopupWindow.setContentView(view);
	}
	
	public void showAtLocation(Point point) {
		if ((mPopupWindow != null) && (mSubView != null))
			mPopupWindow.showAtLocation(mSubView, Gravity.NO_GRAVITY, point.x - mPopupWindow.getWidth() / 2, point.y);
	}
	
	public void showWithLeftBottomLocation(Point point) {		
		if ((mPopupWindow != null) && (mSubView != null)) { 			
			mPopupWindow.showAtLocation(mSubView, Gravity.NO_GRAVITY, point.x, point.y);
		}
	}
	
	public void dismiss() {
		if ((mPopupWindow != null) && mPopupWindow.isShowing())
			mPopupWindow.dismiss();
	}
	
	public void setOnDismissListener(OnPopupDismissListener popupDismissListener) {
		mDismissListener = popupDismissListener;
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		if (mDismissListener != null)
			mDismissListener.onDismiss();
	}
}
