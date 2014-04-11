package vn.techplus.filmon;

import java.util.HashMap;

import vn.techplus.filmon.configuration.AppConfig;
import vn.techplus.filmon.configuration.DefaultGoogleAnalytics;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.model.ChannelManagement.OnUpdateFinished;
import vn.techplus.filmon.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.HitTypes;
import com.google.analytics.tracking.android.Tracker;

public class SplashActivity extends Activity implements OnUpdateFinished {
	public static final String NAME_PREFERENCES = "FullIpTv_Pref";
	public static final String FIRST_START_KEY = "first start";

	private boolean mAllowFinish = false;
	private boolean mIsProgressing = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getResources().getString(R.string.model).equalsIgnoreCase("1")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		setContentView(R.layout.splash);
		
		if (!Utils.isNetworkConnected(this)) {
			Toast.makeText(this, "Not found network connection!", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		final ImageView imageLogo = (ImageView) findViewById(R.id.imgLogo);
		final Animation anim_out = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out); 
	    final Animation anim_in  = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in); 	    
	    anim_in.setDuration(500);	    
	    anim_out.setDuration(500);
	    anim_out.setStartOffset(1000);
	    anim_out.setAnimationListener(new AnimationListener()
	    {
	        @Override public void onAnimationStart(Animation animation) {}
	        @Override public void onAnimationRepeat(Animation animation) {}
	        @Override public void onAnimationEnd(Animation animation)
	        {
	            /*final TextView txtSlogan = (TextView) findViewById(R.id.txtSlogan);
	            txtSlogan.setVisibility(View.VISIBLE);
	            txtSlogan.startAnimation(anim_in);*/
	            imageLogo.setImageResource(R.drawable.logo_fmedia_big);
	            anim_in.setAnimationListener(new AnimationListener() {
	                @Override public void onAnimationStart(Animation animation) {}
	                @Override public void onAnimationRepeat(Animation animation) {}
	                @Override public void onAnimationEnd(Animation animation) {}
	            });
	            imageLogo.startAnimation(anim_in);
	        }
	    });
	    imageLogo.startAnimation(anim_out);
		new AsyncCheckAppConfig().execute();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && mIsProgressing) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void loadData() {
		findViewById(R.id.progressView).setVisibility(View.VISIBLE);
		mIsProgressing = true;
		ChannelManagement.getInstance().loadChannels(getApplicationContext(), this);
				
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mAllowFinish) {
					startActivity(new Intent(SplashActivity.this, FilmOnActivity.class));
					finish();
					overridePendingTransition(R.anim.fadeout, R.anim.fadein);
				}
				else {
					mAllowFinish = true;
				}
			}
		}, 2500);
	}
	
	@Override
	public void onFinished(boolean isSuccessful) {
		// TODO Auto-generated method stub
		findViewById(R.id.progressView).setVisibility(View.GONE);
		mIsProgressing = false;
		if (!isSuccessful) {
			// load data fail
			// reset CMS Server
			Toast.makeText(SplashActivity.this, R.string.msg_load_data_fail, Toast.LENGTH_LONG).show();
			findViewById(R.id.progressView).setVisibility(View.GONE);
			mIsProgressing = true;
			finish();
			return;
		}
		
		if (mAllowFinish) {
			startActivity(new Intent(SplashActivity.this, FilmOnActivity.class));
			finish();
			overridePendingTransition(R.anim.fadeout, R.anim.fadein);
		}
		else {
			mAllowFinish = true;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		ChannelManagement.getInstance().loadMyChannels(getApplicationContext());
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//EasyTracker.getInstance(this).activityStop(this);
		Tracker tracker = DefaultGoogleAnalytics.getGaTracker();
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, HitTypes.APP_VIEW);
		//hitParameters.put(Fields.APP_NAME, getString(R.string.app_name));
		hitParameters.put(Fields.SCREEN_NAME, "Splash Screen");
		tracker.send(hitParameters);
		SharedPreferences sPref = getSharedPreferences(SplashActivity.NAME_PREFERENCES, MODE_PRIVATE);
		if (sPref.getBoolean(FIRST_START_KEY, true)) {
			Editor editor = sPref.edit();
			editor.putBoolean(FIRST_START_KEY, false);
			editor.commit();
			DefaultGoogleAnalytics.switchOff(this, true);
		}
		super.onStop();
	}
	
	
	class AsyncCheckAppConfig extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Check Config App from Server
			AppConfig.checkServer();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			if (AppConfig.isExpiredApp()) {
				Toast.makeText(SplashActivity.this, "This app is expired!\nPlease contact to developer team", Toast.LENGTH_LONG).show();
				finish();
			} else {
				loadData();
			}
			
		}
		
	}
	
}
