package vn.techplus.filmon;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnHWRenderFailedListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.widget.CenterLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import vn.techplus.filmon.Adapter.ListChannelAdapter;
import vn.techplus.filmon.configuration.AppConfig;
import vn.techplus.filmon.model.Category;
import vn.techplus.filmon.model.Channel;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.ads.AdView;
import com.purplebrain.adbuddiz.sdk.AdBuddiz;
import com.purplebrain.adbuddiz.sdk.AdBuddizDelegate;
import com.purplebrain.adbuddiz.sdk.AdBuddizError;

public class TVPlayer extends Activity implements OnCheckedChangeListener,
		OnClickListener, OnItemClickListener {
	public static final String EXTRA_CHANNEL = "channel";
	//public static final String EXTRA_CATEGORY_ID = "categoryId";
	//public static final String EXTRA_STREAM = "linkStream";
	//"rtmp://live305.edge.filmon.com/live/?id=0ad5aac39bb13fbef9880e6b15cd72aba93f79cdfb3a5d6ba9a8ad576a89a9124bad33af9217e03fbc43da440949570d9b6dbea0ba0c5c1da5f14cfe38325818cd4b9f8f6b33231d79ade84187d433653b5ee41c7e2a5a6603aedaaec75a7e3846d2662d78f6f67254a2a3f87fecf2dd811a0c59940f8041597b6dcbb5f53ffd583ef4fb90fbf4b65544610e4aaf64e0523dfd7b79cc81b9b1dc24b2bb2128132841c2f92433b1d2d72dce2532b49613&here=now/1045.low.stream";//"rtmp://bantin.netlink.vn/vod2/mp4:thethao/15-12/eveton.mp4";
	//"http://cms.storm-media.net/viewChannel?channelId=20&deviceSn=570786737&hashKey=63fd22fa86f5a138b0835a9b4e0ec142&hashNum=channel1";
	
	private static final int INTERVAL_HIDE_CONTROL = 3000;
	private static final int INTERVAL_ADS = 15 * 60 * 1000;
	private static final boolean PREFER_HW = false;
	
	private ProgressBar mProgressBar;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private RelativeLayout mVideoLayout;
	private ToggleButton mTogglePlayPause, mToggleMyChannel;
	private ListView mListChannel;
	private View mPlayerOption;
	private RadioGroup mRadioVideoQuality;
	
	private boolean mIsVideoReady;
	private boolean mIsVideoSizeKnown;
	private int mVideoHeight;
	private int mVideoWidth;
	private boolean isProgressing = false;
	private String mLinkStream;
	//private String mChannelId;
	//private String mCategoryId;
	private Channel mCurChannel;
	private Category mCategory;
	
	private MediaPlayer mPlayer;	
	private Handler mHandler;
	private ListChannelAdapter mChannelsAdapter;
	private List<Channel> mChannels;
	private List<String> mLinkStreams;
	private AsyncExtractLinkStream mExtractingThread;
	
	/* listeners for MediaPlayer */
	private SurfaceHolder.Callback mHolderCallback = new Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "surfaceDestroyed");
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "surfaceCreated");
			
			if (!TextUtils.isEmpty(mLinkStream))
				setupPlayer();
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "surfaceChanged");
		}
	};
	
	private OnPreparedListener mPreparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "OnPrepared");
			
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.GONE);

			mIsVideoReady = true;
			setOptionEnablable(true);
			
			// show metadata
			/*Metadata metadata = mp.getMetadata();
			showMetaData(metadata);*/
			if (mIsVideoReady && mIsVideoSizeKnown) {
				// start media player
				mTogglePlayPause.setChecked(true);
				mTogglePlayPause.setEnabled(true);
			}
		}
	};
	
	private OnCompletionListener mCompletionListener = new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "OnCompletion");
		}
	};
	
	private OnErrorListener mErrorListener = new OnErrorListener() {
		
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "OnError");
			
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.GONE);
			
			showError();
			return true;
		}
	};
	
	private OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
		
		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "OnVideoSizeChanged");
			
			if (width == 0 || height == 0) {
				Log.w(getClass().getName(), "Invalid dimension");
				return;
			}

			mIsVideoSizeKnown = true;
			mVideoHeight = height;
			mVideoWidth = width;
			resizeSurface(mVideoLayout.getMeasuredWidth(),
					mVideoLayout.getMeasuredHeight());

			if (mIsVideoReady && mIsVideoSizeKnown) {
				// mIsPlay = true;
				startPlayback(true);
			}
		}
	};
	
	private OnHWRenderFailedListener mHWRenderFailedListener = new OnHWRenderFailedListener() {
		
		@Override
		public void onFailed() {
			// TODO Auto-generated method stub
			if (BuildConfig.DEBUG)
				Log.i(getClass().getSimpleName(), "OnHWFailed");
		}
	};
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tv_player);
		
		// Check network connection, if not connected then close app
		if (!Utils.isNetworkConnected(this)) {
			Toast.makeText(this, getClass().getName(), Toast.LENGTH_LONG).show();
			finish();
		}			
		
		// check lib vitamio, if not installed, then finish Activity to install
		if (!LibsChecker.checkVitamioLibs(this))
			return;

		// extract data
		mHandler = new Handler();		
		Bundle bundle = getIntent().getExtras();		
		if (bundle != null) {
			mCurChannel = bundle.getParcelable(EXTRA_CHANNEL);
		}

		if (mCurChannel == null) {
			showError();
			finish();
			return;			
		}				
		
		mLinkStream = mCurChannel.getStreamUrl();
		String categoryId = mCurChannel.getCategoryId();
		
		mListChannel = (ListView) findViewById(R.id.listChannels);
		mVideoLayout = (RelativeLayout) findViewById(R.id.videoLayout);
		mVideoLayout.setOnClickListener(this);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mTogglePlayPause = (ToggleButton) findViewById(R.id.playtogglebutton);
		mTogglePlayPause.setOnCheckedChangeListener(this);
		mPlayerOption = findViewById(R.id.playerOption);
		loadViewPlayerOption(mPlayerOption);
		if (TextUtils.isEmpty(mCurChannel.getStreamUrl())) {
			// this is not FilmOn Category
			mPlayerOption.setVisibility(View.GONE);
		}

		AdBuddiz.cacheAds(this);
		AdBuddiz.setDelegate(new AdBuddizDelegate() {
			
			@Override
			public void didShowAd() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void didHideAd() {
				// TODO Auto-generated method stub
				startCountTimeShowAds();
			}
			
			@Override
			public void didFailToShowAd(AdBuddizError arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void didClick() {
				// TODO Auto-generated method stub
				
			}
		});
		
		ChannelManagement manager = ChannelManagement.getInstance();
		List<Category> categories = manager.getCategories();
		for (Category category: categories) {
			if (category.getId().equals(categoryId)) {
				mCategory = category;				
				break;
			}
		}		
		
		getWindow().setFormat(PixelFormat.TRANSLUCENT);		
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);
		mHolder = mSurfaceView.getHolder();
		mHolder.setFormat(PixelFormat.RGBX_8888);
		mHolder.addCallback(mHolderCallback);
		
		// checkout type category is radio or not
		/*if (currCategory != null && isRadioChannel(currCategory.getName())) {
			mSurfaceView.setBackgroundResource(R.drawable.on_air);
		}
		else {
			mSurfaceView.setBackgroundResource(0);
		}*/
								
		AdView adview = (AdView) findViewById(R.id.adView);
		AdView adview2 = (AdView) findViewById(R.id.adView2);
		if (AppConfig.isHiddenAds()) {
			if (adview != null)
				adview.setVisibility(View.GONE);
			if (adview2 != null)
				adview2.setVisibility(View.GONE);
		}
		
		if (!TextUtils.isEmpty(mLinkStream)){
			
		} else if (mCurChannel != null) {
			mExtractingThread = new AsyncExtractLinkStream();
			mExtractingThread.execute(mCurChannel);
		}
		else {
			Toast.makeText(getApplicationContext(), R.string.msg_faild_found_channel, Toast.LENGTH_SHORT).show();
		}
		
		if (mCategory != null) {
			// load list channels
			new AsyncLoadListChannels(getApplicationContext()).execute();
		}
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		releasePlayer();
		AdBuddiz.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		/*if (AdBuddiz.isReadyToShowAd(TVPlayer.this))
			AdBuddiz.showAd(this);*/
		mHandler.removeCallbacks(mShowAdsRunnable);
		mHandler.removeCallbacks(showControllerRunnable);
		releasePlayer();
		super.onStop();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		ShowMediaControl();
		if (keyCode == KeyEvent.KEYCODE_BACK && isProgressing)
			return true;
				
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (buttonView == mTogglePlayPause && mPlayer != null) {
			if (isChecked) {
				mPlayer.start();
				startCountTimeShowAds();				
			}
			else {
				mPlayer.pause();
				mHandler.removeCallbacks(mShowAdsRunnable);
			}
		} else if (buttonView == mToggleMyChannel) {			
			if (mCurChannel != null)
				mCurChannel.setMyChannel(isChecked);
			
			if (isChecked)
				ChannelManagement.getInstance().addMyChannels(getApplicationContext(), mCurChannel);
			else
				ChannelManagement.getInstance().removeMyChannels(getApplicationContext(), mCurChannel.getId());
									
		}
	}	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.videoLayout :
			ShowMediaControl();
			break;
			
			default :
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == mListChannel) {
			Channel channel = mChannelsAdapter.getItem(arg2);
			if (channel == null) {
				showError();
				return;
			}
			if (mCurChannel.getId().equalsIgnoreCase(channel.getId())) 
				return;							
			
			mCurChannel = channel;
			setOptionEnablable(false);
			mListChannel.setSelected(true);
			mListChannel.setSelection(arg2);
			mListChannel.setItemChecked(arg2, true);
			//mToggleMyChannel.setEnabled(true);
			mToggleMyChannel.setChecked(mCurChannel.isMyChannel());
			if (!TextUtils.isEmpty(mCurChannel.getStreamUrl())) {
				mLinkStream = mCurChannel.getStreamUrl();
				playChannel();
				mRadioVideoQuality.check(R.id.radio1);
			} else {				
				if (mExtractingThread != null) 
					mExtractingThread.cancel(true);
				mExtractingThread = new AsyncExtractLinkStream();
				mExtractingThread.execute(mCurChannel);								
			}
		}
	}
	
	
	@SuppressLint("NewApi")
	private void loadListChannel(List<Channel> channels) {
		mChannels = channels;
		if (mChannels != null) {
			mChannelsAdapter = new ListChannelAdapter(this, R.layout.channel_item_text, mChannels);
			mListChannel.setAdapter(mChannelsAdapter);
			mListChannel.setOnItemClickListener(this);
			mListChannel.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				mListChannel.setActivated(true);
			mListChannel.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub		
					ShowMediaControl();
					return mListChannel.onTouchEvent(event);
				}
			});
			
			// find index of current channel
			for (Channel channel : mChannels) {
				if (channel.getId().equalsIgnoreCase(mCurChannel.getId())) {
					mCurChannel = channel;
					int index = mChannels.indexOf(channel);
					mListChannel.setSelected(true);
					mListChannel.setSelection(index);
					mListChannel.setItemChecked(index, true);
					//mToggleMyChannel.setEnabled(true);
					mToggleMyChannel.setChecked(channel.isMyChannel());
					break;
				}
			}
		}
	}
	
	private void loadViewPlayerOption(View view) {
		mToggleMyChannel = (ToggleButton) findViewById(R.id.toggleMyChannel);
		mToggleMyChannel.setOnCheckedChangeListener(this);
		//mToggleMyChannel.setEnabled(false);
		
		mRadioVideoQuality = (RadioGroup) view.findViewById(R.id.radioGroupQuality);
		mRadioVideoQuality.check(R.id.radio1);
		setOptionEnablable(false);
		mRadioVideoQuality.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) { 
				case R.id.radio0 : 
					if (mLinkStreams != null && mLinkStreams.size() > 1) {
						mLinkStream = mLinkStreams.get(0);
						playChannel();
					}
					break;
				case R.id.radio1 : 
					if (mLinkStreams != null) {
						mLinkStream = mLinkStreams.get(mLinkStreams.size() - 1);
						playChannel();
					}
					break;
					
					default :
						break;
				}
			}
		});
	}

	private void setOptionEnablable(boolean enabled) {
		mToggleMyChannel.setEnabled(enabled);
		for (int i = 0; i < mRadioVideoQuality.getChildCount(); i++) {
			mRadioVideoQuality.getChildAt(i).setEnabled(enabled);
		}
	}
	private void setupPlayer() {
		resetPlayer();
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
				mPlayer = new MediaPlayer(this);
			else 
				mPlayer = new MediaPlayer(this, PREFER_HW);
			mPlayer.setDataSource(mLinkStream);
			mPlayer.setDisplay(mHolder);
			mPlayer.prepareAsync();
			mPlayer.setOnCompletionListener(mCompletionListener);
			mPlayer.setOnPreparedListener(mPreparedListener);
			mPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
			mPlayer.setOnErrorListener(mErrorListener);			
			mPlayer.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
			mPlayer.setOnHWRenderFailedListener(mHWRenderFailedListener);						
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.VISIBLE);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void playChannel() {
		
		resetPlayer();
		releasePlayer();
		
		Canvas canvas = null;
		synchronized (mHolder) {
			try {
				if (mHolder.getSurface().isValid()) {

					canvas = mHolder.lockCanvas();
					//canvas.drawARGB(255, 0, 0, 0);
					canvas.drawColor(Color.BLACK);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (canvas != null)
					mHolder.unlockCanvasAndPost(canvas);
			}
		}
		
		// mIsPlay = false;		
		setupPlayer();
		
	}

	private void startPlayback(boolean isPlay) {
		if (mHolder == null || mPlayer == null)
			return;
		
		if (isPlay) {
			mHolder.setFixedSize(mVideoWidth, mVideoHeight);
			if (mTogglePlayPause != null)
				mTogglePlayPause.setChecked(true);
			// mIsPlay = true;
		} else {
			mHolder.setFixedSize(mVideoWidth, mVideoHeight);
			if (mTogglePlayPause != null)
				mTogglePlayPause.setChecked(false);
		}
		ShowMediaControl();
	}

	private void resetPlayer() {
		mVideoHeight = 0;
		mVideoWidth = 0;
		mIsVideoReady = false;
		mIsVideoSizeKnown = false;		
		if (mTogglePlayPause != null) {
			mTogglePlayPause.setChecked(false);
			mTogglePlayPause.setEnabled(false);
		}
	}

	private void releasePlayer() {		
		resetPlayer();				
		if (mPlayer != null) {
			if (mPlayer.isPlaying())
				mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	private void resizeSurface(int widthParentView, int heightParentView) {

		if (mVideoHeight == 0 || mVideoWidth == 0)
			return;

		CenterLayout.LayoutParams params = (CenterLayout.LayoutParams) mSurfaceView
				.getLayoutParams();
		params.height = heightParentView;
		params.width = widthParentView;
		float scaleX = (float) params.width / (float) mVideoWidth;
		float scaleY = (float) params.height / (float) mVideoHeight;
		// Log.i(TAG, mVideoHeight + " - " + mVideoWidth + " - " + params.height
		// + " - " + params.width + " - " );
		if (scaleX < scaleY) {
			params.width = (int) (mVideoWidth * scaleY);
		} else {
			params.height = (int) (mVideoHeight * scaleX);
		}

		// Log.i(TAG, scaleY + " - " + scaleX + " - " + params.height + " - " +
		// params.width + " - " );
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		mSurfaceView.setLayoutParams(params);
	}
	
	@SuppressLint({ "NewApi", "InlinedApi" })
	private Runnable showControllerRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mTogglePlayPause.setVisibility(View.GONE);
			mListChannel.setVisibility(View.GONE);
			mPlayerOption.setVisibility(View.GONE);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				View decorView = getWindow().getDecorView();
				// Hide both the navigation bar and the status bar.
				// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
				// a general rule, you should design your app to hide the status bar whenever you
				// hide the navigation bar.
				int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
				decorView.setSystemUiVisibility(uiOptions);
			}
		}
	};
	private void ShowMediaControl() {
		mTogglePlayPause.setVisibility(View.VISIBLE);
		mListChannel.setVisibility(View.VISIBLE);
		mPlayerOption.setVisibility(View.VISIBLE);
		if (mHandler == null)
			mHandler = new Handler();		
		mHandler.removeCallbacks(showControllerRunnable);
		mHandler.postDelayed(showControllerRunnable, INTERVAL_HIDE_CONTROL);
	}
	
	private void showError() {
		Toast toast = Toast.makeText(getBaseContext(), "Not connect to server! \nPlease try again later", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		toast.setText(mLinkStream);
		toast.show();
		finish();
	}

	private void startCountTimeShowAds() {
		if (AppConfig.isHiddenAds())
			return;
		
		if (mHandler == null)
			mHandler = new Handler();
		
		mHandler.removeCallbacks(mShowAdsRunnable);
		mHandler.postDelayed(mShowAdsRunnable , INTERVAL_ADS);
	}
	
	private Runnable mShowAdsRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (AdBuddiz.isReadyToShowAd(TVPlayer.this))
				AdBuddiz.showAd(TVPlayer.this);
			//startCountTimeShowAds();
		}
	};
	

	class AsyncExtractLinkStream extends AsyncTask<Channel, Void, List<String>> {

		@Override
		protected List<String> doInBackground(Channel... params) {
			// TODO Auto-generated method stub
			if (params == null || params.length < 1)
				return null;
			
			return ChannelManagement.getInstance().getLinkStream(getBaseContext(), params[0]);
			// return null;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			if (mProgressBar != null) {
				mProgressBar.setVisibility(View.GONE);
			}
			
			Log.i(getClass().getName(), "link:" + result);
			mLinkStreams = result;
			if (mLinkStreams != null && mLinkStreams.size() > 0) {
				ShowMediaControl();
				mLinkStream = mLinkStreams.get(mLinkStreams.size() - 1);
				mRadioVideoQuality.check(R.id.radio1);
				if (mLinkStreams.size() == 1) {
					mRadioVideoQuality.getChildAt(0).setVisibility(View.GONE);
				} else if (mLinkStreams.size() == 1) {
					mRadioVideoQuality.getChildAt(0).setVisibility(View.VISIBLE);
				}
				playChannel();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// super.onPreExecute();
			if (mProgressBar != null) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
		}
		
	}
		
	class AsyncLoadListChannels extends AsyncTask<Void, Void, List<Channel>> {
		WeakReference<Context> weakContext;
		
		public AsyncLoadListChannels(Context context) {
			weakContext = new WeakReference<Context>(context);
		}

		@Override
		protected List<Channel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (weakContext.get() == null)
				return null;
			
			return ChannelManagement.getInstance().getChannelsByCategory(weakContext.get(), mCategory);
		}

		@Override
		protected void onPostExecute(List<Channel> result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.GONE);
			
			if (result != null) {
				loadListChannel(result);
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
			if (mProgressBar != null)
				mProgressBar.setVisibility(View.VISIBLE);
		}
				
	}
	
}
