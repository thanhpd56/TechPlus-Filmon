package vn.techplus.global;

import vn.techplus.filmon.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager.BadTokenException;

public class Statics {
	public static final boolean	 DEBUG = true;
	
	// Url to get all youtube categories
	public static final String URL_GET_CATS = "http://tuanit.net/thanhpd56_com/ApiFmedia/get_all_categories.php";
	public static final String URL_GET_VIDEOS_BY_ID = "http://tuanit.net/thanhpd56_com/ApiFmedia/get_video_by_id.php?cat_id=";
	public static final String URL_GET_ALL_VIDEOS = "http://tuanit.net/thanhpd56_com/ApiFmedia/get_all_videos.php";
	
	// Custom Image View
	public static float IMAGE_RATIO = (float) (5.0/3.0);
}	
