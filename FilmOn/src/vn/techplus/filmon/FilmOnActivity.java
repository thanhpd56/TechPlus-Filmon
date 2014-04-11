package vn.techplus.filmon;

import java.util.Observable;
import java.util.Observer;

import vn.techplus.filmon.Adapter.CustomPagerWrapperAdapter;
import vn.techplus.filmon.customwidget.CustomViewPager;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.model.FilmOnService;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.viewpagerindicator.TabPageIndicator;

public class FilmOnActivity extends FragmentActivity implements
		OnClickListener, Observer {
	private Context mContext;
	private ImageView mBtnAbout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// full screen - not action bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getResources().getString(R.string.model).equalsIgnoreCase("1")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		setContentView(R.layout.activity_film_on);
		mContext = getApplicationContext();

		// determine android device (mobile or tablet)
		boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
			Toast.makeText(mContext, "Tablet", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "Mobile", Toast.LENGTH_SHORT).show();
		}
		// get controls
		getControls();
		FragmentPagerAdapter adapter = new CustomPagerWrapperAdapter(
				getSupportFragmentManager());
		CustomViewPager pager = (CustomViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		pager.setPagingEnabled(false);
		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

	}

	public void getControls() {
		mBtnAbout = (ImageView) findViewById(R.id.btnAbout);
		mBtnAbout.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnAbout:
			if (v.getId() == R.id.menuTerm)
				showDialogTermAbout(false);
			else
				showDialogTermAbout(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
	}

	// --function show Dialog Term or About
	protected void showDialogTermAbout(boolean showAbout) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
}
