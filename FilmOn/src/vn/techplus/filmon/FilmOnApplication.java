package vn.techplus.filmon;

import vn.techplus.filmon.configuration.DefaultGoogleAnalytics;
import vn.techplus.filmon.pushNotification.PushNotification;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class FilmOnApplication extends Application {
	public static final String GOOGLE_API_PROJECT_ID = "1068935811360";
	public static final String PUSHAPPS_APP_TOKEN = "ce913df0-316b-4fce-95e3-fee8f2988fb2";
	
	public static String myCategory;
	public static String specialCategory;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		// load String
		myCategory = getString(R.string.my_category);
		specialCategory = getString(R.string.special_category);
		
		DefaultGoogleAnalytics.initializeGa(this);
		PushNotification.register(getApplicationContext());
		
		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				/*.memoryCacheExtraOptions(260, 260)
				// default = device screen dimensions
				// .discCacheExtraOptions(480, 800, CompressFormat.PNG, 70,
				// null)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)*/
				//.discCacheSize(10 * 1024 * 1024).discCacheFileCount(100)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.threadPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 1)
				.build();
		
		Log.i(getClass().getSimpleName(), "size ThreadPool: " + Runtime.getRuntime().availableProcessors());
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions getDisplayOption(int imagePlaceholder) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(imagePlaceholder) // resource or drawable
				.showImageOnFail(imagePlaceholder) // resource or drawable
				.showImageOnLoading(imagePlaceholder)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();

		return options;
	}
	
}
