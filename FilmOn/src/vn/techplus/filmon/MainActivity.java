package vn.techplus.filmon;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import vn.techplus.filmon.Adapter.CategoryAdapter;
import vn.techplus.filmon.Adapter.CustomPagerAdapter;
import vn.techplus.filmon.configuration.AppConfig;
import vn.techplus.filmon.customwidget.ComboBox;
import vn.techplus.filmon.customwidget.PopupView;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.model.FilmOnService;
import vn.techplus.filmon.utils.Utils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;

public class MainActivity extends FragmentActivity implements OnClickListener,
		Observer, OnCheckedChangeListener, OnPageChangeListener,
		OnItemClickListener {
	private ViewPager mViewPager;
	// private CirclePageIndicator mCircleIndicator;
	private CustomPagerAdapter mPagerAdapter;
	// private RelativeLayout mViewMiddleTopbar;
	private ComboBox mComboBoxView;
	private ToggleButton mBtnMenu;
	private ImageView mBtnSearch;
	private SlidingMenu mSlidingMenu;
	private ImageView mBtnAbout;

	private ListView mListCategories;
	private CategoryAdapter mCategoriesAdapter;
	private TextView mTxtCurrCategory;
	// private ImageView mBtnSetting;
	private PopupView mPopupSetting;

	private List<Category> mCategories;

	private boolean isMobile = true;
	private boolean isProgressing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getResources().getString(R.string.model).equalsIgnoreCase("1")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		setContentView(R.layout.main_activity);

		mCategories = ChannelManagement.getInstance().getCategories();
		if (mCategories == null)
			mCategories = new ArrayList<Category>();

		mBtnAbout = (ImageView) findViewById(R.id.btnAbout);
		mBtnAbout.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(),
				generateFragments());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		/*
		 * mCircleIndicator = (CirclePageIndicator)
		 * findViewById(R.id.viewIndicator);
		 * mCircleIndicator.setViewPager(mViewPager, 0);
		 * mCircleIndicator.setSnap(true);
		 * mCircleIndicator.setOnPageChangeListener(this);
		 */
		AdView adview = (AdView) findViewById(R.id.adView);
		if (AppConfig.isHiddenAds()) {
			adview.setVisibility(View.GONE);
		}

		View view = findViewById(R.id.left_menu);
		if (view == null) {
			// Mobile Version
			isMobile = true;
			loadMobileContentView();
		} else {
			// Tivi Version
			isMobile = false;
			loadTvContentView();
		}

		ChannelManagement.getInstance().addObserver(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// unbindDrawables(getWindow().getDecorView().findViewById(android.R.id.content));
		System.gc();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FilmOnService.getInstance().stopLoopKeepAlive(getApplicationContext());
		FilmOnService.release();
		ChannelManagement manager = ChannelManagement.getInstance();
		manager.deleteObserver(this);
		manager.release();
		super.onDestroy();
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		/*
		 * if (observable instanceof ChannelManagement) {
		 * //mCircleIndicator.setCurrentItem(0); ChannelManagement cManager =
		 * ChannelManagement.getInstance(); // change values of combobox
		 * mCategories.clear(); mCategories.addAll(cManager.getCategories());
		 * 
		 * // change pager adapter mPagerAdapter = new
		 * CustomPagerAdapter(getSupportFragmentManager(), generateFragments());
		 * mViewPager.setAdapter(mPagerAdapter); mViewPager.invalidate();
		 * //mCircleIndicator.notifyDataSetChanged();
		 * 
		 * if (isMobile) {
		 * 
		 * // if (mCategories.size() == 0) { //
		 * mComboBoxView.getView().setVisibility(View.INVISIBLE); // } else { //
		 * mComboBoxView.getView().setVisibility(View.VISIBLE); //
		 * mComboBoxView.setArrayTitles(mCategories, 0); //
		 * mCircleIndicator.setCurrentItem(0); // } } else {
		 * mCategoriesAdapter.notifyDataSetChanged(); if (mCategories.size() !=
		 * 0) { mTxtCurrCategory.setText(mCategories.get(0).getName());
		 * //mCircleIndicator.setCurrentItem(0); } else {
		 * mTxtCurrCategory.setText(R.string.app_name); } } }
		 */
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU: {
			if (isMobile) {
				mBtnMenu.setChecked(!mBtnMenu.isChecked());
			} /*
			 * else { int[] location = new int[2];
			 * mBtnSetting.getLocationInWindow(location); Point point = new
			 * Point(location[0] + mBtnSetting.getWidth() - 5, location[1] -
			 * mBtnSetting.getHeight() / 2 + 5); showPopupSetting(point); }
			 */
			return true;

		}
		case KeyEvent.KEYCODE_BACK: {
			if (isProgressing)
				return true;
			break;

		}

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (!isMobile && mPopupSetting != null) {
			mPopupSetting.dismiss();
		}

		switch (v.getId()) {
		case R.id.menuCMSServer:
			/*
			 * showDialogInputCMS(); if (mBtnMenu != null)
			 * mBtnMenu.setChecked(false); break;
			 */

		case R.id.menuDeviceSN:
			showDialogDeviceSN();

			if (mBtnMenu != null)
				mBtnMenu.setChecked(false);
			break;

		case R.id.menuTerm:
		case R.id.menuAbout:
		case R.id.btnAbout:
			if (mBtnMenu != null)
				mBtnMenu.setChecked(false);
			/*
			 * Toast toast = Toast.makeText(this, "Coming soon!!!",
			 * Toast.LENGTH_LONG); toast.setGravity(Gravity.CENTER, 0, 0);
			 * toast.show();
			 */
			if (v.getId() == R.id.menuTerm)
				showDialogTermAbout(false);
			else
				showDialogTermAbout(true);
			break;

		case R.id.btnSearch:
			startActivity(new Intent(this, SearchAcitvity.class));
			break;

		case R.id.btnMenu:
			if (mSlidingMenu != null) {
				mSlidingMenu.showMenu();
			}
			break;

		/*
		 * case R.id.btnSetting : int[] location = new int[2];
		 * v.getLocationInWindow(location); Point point = new Point(location[0]
		 * + v.getWidth() - 5, location[1] - v.getHeight() / 2 + 5);
		 * showPopupSetting(point); break;
		 */

		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		if (mComboBoxView != null) {
			mComboBoxView.setChooseIndex(arg0);
		}
		if (!isMobile && arg0 < mCategories.size()) {
			mTxtCurrCategory.setText(mCategories.get(arg0).getName());
			mListCategories.setSelection(arg0);
			mListCategories.setItemChecked(arg0, true);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == mBtnMenu && mSlidingMenu != null) {
			if (isChecked)
				mSlidingMenu.showMenu();
			else
				mSlidingMenu.showContent();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Category category = mCategoriesAdapter.getItem(arg2);
		if (category != null) {
			mTxtCurrCategory.setText(category.getName());
			mViewPager.setCurrentItem(arg2);
			if (isMobile && mSlidingMenu != null)
				mSlidingMenu.showContent();

			// mCircleIndicator.setCurrentItem(arg2);
		}
	}

	@SuppressWarnings("deprecation")
	protected void showDialogDeviceSN() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// TextView txtDeviceSN = new TextView(this);
		// txtDeviceSN.setLayoutParams(new
		// LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// txtDeviceSN.setText(Utils.getDeviceId(this));
		// txtDeviceSN.setGravity(Gravity.CENTER);
		builder.setCancelable(true)
				.setNegativeButton(R.string.btn_cancel, null)
				.setTitle("DeviceSN:  " + Utils.getDeviceId(this));
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
	}

	protected void showDialogTermAbout(boolean showAbout) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// TextView txtDeviceSN = new TextView(this);
		// txtDeviceSN.setLayoutParams(new
		// LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// txtDeviceSN.setText(Utils.getDeviceId(this));
		// txtDeviceSN.setGravity(Gravity.CENTER);
		WebView webView = new WebView(this);
		webView.setPadding(10, 50, 10, 10);
		if (showAbout) {
			webView.loadUrl("file:///android_asset/About2.htm");
			builder.setTitle(R.string.menu_about);
		} else {
			builder.setTitle(R.string.menu_term);
			webView.loadUrl("file:///android_asset/TermOfUs2.htm");
		}
		builder.setView(webView);
		builder.setCancelable(true)
				.setNegativeButton(R.string.btn_cancel, null);
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialog.show();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadTvContentView() {
		mTxtCurrCategory = (TextView) findViewById(R.id.txtNameCategory);
		mListCategories = (ListView) findViewById(R.id.listCategories);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		// mCircleIndicator = (CirclePageIndicator)
		// findViewById(R.id.viewIndicator);
		/*
		 * mBtnSetting = (ImageView) findViewById(R.id.btnSetting);
		 * 
		 * mBtnSetting.setOnClickListener(this);
		 */
		mListCategories.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListCategories.setOnItemClickListener(this);
		mCategoriesAdapter = new CategoryAdapter(this, R.layout.category_item,
				mCategories);
		mListCategories.setAdapter(mCategoriesAdapter);
		if (mCategories.size() > 0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				mListCategories.setActivated(true);
			mListCategories.setSelected(true);
			mListCategories.setSelection(0);
			mListCategories.setItemChecked(0, true);
			mTxtCurrCategory.setText(mCategories.get(0).getName());
		}
	}

	private void loadMobileContentView() {
		mBtnMenu = (ToggleButton) findViewById(R.id.btnMenu);
		mBtnSearch = (ImageView) findViewById(R.id.btnSearch);
		mBtnSearch.setOnClickListener(this);
		mBtnMenu.setChecked(false);
		mBtnMenu.setOnCheckedChangeListener(this);
		mTxtCurrCategory = (TextView) findViewById(R.id.txtNameCategory);
		mTxtCurrCategory.setSelected(true);
		loadSlidingCategories();
		// mViewMiddleTopbar = (RelativeLayout) findViewById(R.id.emptyView);
		// createComboBoxGroups();
	}

	private ArrayList<Fragment> generateFragments() {
		ArrayList<Fragment> array = new ArrayList<Fragment>();
		for (int i = 0; i < mCategories.size(); i++) {
			array.add(ListChannelsFragment.newInstance(i));
		}

		return array;
	}

	@SuppressLint("NewApi")
	private void loadSlidingCategories() {
		View vMenu = LayoutInflater.from(this).inflate(R.layout.category_list,
				null);
		mListCategories = (ListView) vMenu.findViewById(R.id.listCategories);
		mListCategories.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mListCategories.setOnItemClickListener(this);
		mCategoriesAdapter = new CategoryAdapter(this, R.layout.category_item,
				mCategories);
		mListCategories.setAdapter(mCategoriesAdapter);
		if (mCategories.size() > 0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				mListCategories.setActivated(true);
			mListCategories.setSelected(true);
			mListCategories.setSelection(0);
			mListCategories.setItemChecked(0, true);
			mTxtCurrCategory.setText(mCategories.get(0).getName());
		}

		/*
		 * final TextView mBtnCMSServer = (TextView)
		 * vMenu.findViewById(R.id.menuCMSServer); final TextView mBtnDeviceSN =
		 * (TextView) vMenu.findViewById(R.id.menuDeviceSN); final TextView
		 * mBtnTerm = (TextView) vMenu.findViewById(R.id.menuTerm); final
		 * TextView mBtnAbout = (TextView) vMenu.findViewById(R.id.menuAbout);
		 * mBtnAbout.setOnClickListener(this);
		 * mBtnDeviceSN.setOnClickListener(this);
		 * mBtnTerm.setOnClickListener(this);
		 * mBtnCMSServer.setOnClickListener(this);
		 */

		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		mSlidingMenu.setMenu(vMenu);
		mSlidingMenu.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				mBtnMenu.setChecked(false);
			}
		});
		mSlidingMenu.setOnOpenListener(new OnOpenListener() {

			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
				mBtnMenu.setChecked(true);
			}
		});
	}

	/*
	 * private void showPopupSetting(Point point) { if (mPopupSetting == null) {
	 * mPopupSetting = new PopupView(this); View vSetting =
	 * LayoutInflater.from(this).inflate(R.layout.popup_menu, null); final
	 * TextView mBtnCMSServer = (TextView)
	 * vSetting.findViewById(R.id.menuCMSServer); final TextView mBtnDeviceSN =
	 * (TextView) vSetting.findViewById(R.id.menuDeviceSN); final TextView
	 * mBtnTerm = (TextView) vSetting.findViewById(R.id.menuTerm); final
	 * TextView mBtnAbout = (TextView) vSetting.findViewById(R.id.menuAbout);
	 * 
	 * mBtnAbout.setOnClickListener(this);
	 * mBtnDeviceSN.setOnClickListener(this); mBtnTerm.setOnClickListener(this);
	 * mBtnCMSServer.setOnClickListener(this);
	 * 
	 * mPopupSetting.setContentView(vSetting); vSetting.setFocusable(true);
	 * vSetting.requestFocus(); }
	 * 
	 * point.y -= this.getResources().getDimension(R.dimen.popup_list_height);
	 * mPopupSetting.showWithLeftBottomLocation(point); }
	 */

}
