package vn.techplus.filmon.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;

public class Utils {
	
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isAvailable() && info.isConnected())
			return true;
		
		return false;
	}
	
	public static String generateMd5FromString(String plainText) {
		try {
			MessageDigest md5Encode = MessageDigest.getInstance("MD5");
			md5Encode.update(plainText.getBytes());
	        byte messageDigest[] = md5Encode.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++) {
	        	String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getDeviceId(Context context) {		
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);		//"b24d9a76431f0e6f";//
	}
	
	@SuppressLint("DefaultLocale")
	public static String addProtocolToUrl(String urlString) {
		String lowerUrl = urlString.toLowerCase();
		if (!lowerUrl.contains("https:") && !lowerUrl.contains("http:")) {
			urlString = "http://" + urlString;
		}
		
		return urlString;
	}
	
	public static final String generateRandomString(int lenString) {
		final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lenString; i++) {
			Random random = new Random();
			int index = random.nextInt(characters.length());
			builder.append(characters.charAt(index));
		}
		
		return builder.toString();
	}
	
	public static Point getSizeWindowPixel(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		Point size = new Point();
		size.x = metrics.widthPixels;
		size.y = metrics.heightPixels;
		return size;
	}
	
	public static Point getSizeWindowDp(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float density = metrics.density;
		Point sizeDp = new Point();
		sizeDp.x = (int) (metrics.widthPixels / density);
		sizeDp.y = (int) (metrics.heightPixels / density);
		return sizeDp;
	}
}
