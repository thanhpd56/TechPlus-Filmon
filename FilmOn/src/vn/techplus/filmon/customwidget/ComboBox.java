package vn.techplus.filmon.customwidget;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.customwidget.PopupView.OnPopupDismissListener;
import vn.techplus.filmon.R;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ComboBox implements OnItemClickListener, OnPopupDismissListener{
	private static final String TAG = "OptionTopBar";
	
	public interface OnItemOptionSelected {
		public void onSelect(int selectedPosition);
	}
	
	private View mRootView;
	private RelativeLayout mOptionView;
	private ListView mOptionList;
	private TextView mTxtTitle;
	private Context mContext;
	private LayoutInflater mInflater;
	private PopupView mPopupView;
	
	private ArrayList<String> mArrayTitle;
	private ArrayAdapter<String> mTitleAdapter;
	private OnItemOptionSelected mOnItemSelected;
	
	public ComboBox(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		mRootView = mInflater.inflate(R.layout.combobox, null);
		mOptionView = (RelativeLayout) mRootView.findViewById(R.id.comboBox);
		mOptionView.setSelected(false);
		mTxtTitle = (TextView) mRootView.findViewById(R.id.txtTitle);
		
		View view2 = mInflater.inflate(R.layout.combobox_list, null);
		mOptionList = (ListView) view2;
		mArrayTitle = new ArrayList<String>();
		mTitleAdapter = new ArrayAdapter<String>(mContext, R.layout.combobox_item, R.id.txt, mArrayTitle);
		mOptionList.setAdapter(mTitleAdapter);
		mOptionList.setOnItemClickListener(this);
		
		mPopupView = new PopupView(context);
		mPopupView.setContentView(view2);
		mPopupView.setOnDismissListener(this);
		
		mOptionView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mOptionView.setSelected(!mOptionView.isSelected());
				if (mOptionView.isSelected()) {
					Point point = new Point(); 
					int[] location = new int[2];
					v.getLocationOnScreen(location);
					point.x = location[0] + v.getWidth()/2;
					point.y = location[1] + v.getHeight();
					
					if (mPopupView != null) {
						mPopupView.showAtLocation(point);
					}					
				}
				else {
					if (mPopupView != null) {
						mPopupView.dismiss();;
					}
				}
			}
		});
	}
	
	/**
	 * 
	 * @param titles
	 * array title for list view showing
	 * 
	 * @param currentIndex
	 * current title is selected
	 */
	public void setArrayTitles(List<String> titles, int currentIndex) {
		mArrayTitle.clear();
		mArrayTitle.addAll(titles);
		mTitleAdapter.notifyDataSetChanged();
		if (currentIndex < titles.size() && currentIndex > -1) {
			mTxtTitle.setText(titles.get(currentIndex));
		} else {
			mTxtTitle.setText("");
		}
	}
	
	public void setTitle(String title) {
		mTxtTitle.setText(title);
	}
	
	public void setChooseIndex(int index) {
		if (index < mArrayTitle.size())
			mTxtTitle.setText(mArrayTitle.get(index));
	}
	
	public View getView() {
		return mRootView;
	}

	public void setOnItemSelected(OnItemOptionSelected onItemSelected) {
		mOnItemSelected = onItemSelected;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.w(TAG, "On Item Click");
		mTxtTitle.setText(mArrayTitle.get(arg2));
		if (mOnItemSelected != null) {
			mOnItemSelected.onSelect(arg2);
		}
		mPopupView.dismiss();		
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		mOptionView.setSelected(false);
	}
}
