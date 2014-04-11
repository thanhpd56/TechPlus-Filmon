package vn.techplus.filmon.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.Tracker;

public class DefaultGoogleAnalytics {
	private static GoogleAnalytics mGa;
	private static Tracker mTracker;

	/*
	 * Google Analytics configuration values.
	 */
	// Placeholder property ID.
	private static final String GA_PROPERTY_ID = "UA-47205952-1";

	// Key used to store a user's tracking preferences in SharedPreferences.
	private static final String TRACKING_PREF_KEY = "trackingPreference";

	/*
	 * Method to handle basic Google Analytics initialization. context call will
	 * not block as all Google Analytics work occurs off the main thread.
	 */
	public static void initializeGa(final Context context) {
		mGa = GoogleAnalytics.getInstance(context);
		mTracker = mGa.getTracker(GA_PROPERTY_ID);
		
	    // Set Logger verbosity.
	    mGa.getLogger().setLogLevel(LogLevel.WARNING);

		// Set the opt out flag when user updates a tracking preference.
		SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		userPrefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
					@Override
					public void onSharedPreferenceChanged(
							SharedPreferences sharedPreferences, String key) {
						if (key.equals(TRACKING_PREF_KEY)) {
							GoogleAnalytics.getInstance(context)
									.setAppOptOut(sharedPreferences.getBoolean(key, false));
						}
					}
				});
	}
	
	public static void switchOff(Context context, boolean isOff) {
		SharedPreferences userPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = userPrefs.edit();
		editor.putBoolean(TRACKING_PREF_KEY, isOff);
		editor.commit();
	}
	

	  /*
	   * Returns the Google Analytics tracker.
	   */
	  public static Tracker getGaTracker() {
	    return mTracker;
	  }

	  /*
	   * Returns the Google Analytics instance.
	   */
	  public static GoogleAnalytics getGaInstance() {
	    return mGa;
	  }
}
