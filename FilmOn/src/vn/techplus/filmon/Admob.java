package vn.techplus.filmon;

import android.app.Activity;
import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class Admob {
	
	public static void loadAds(Activity activity) {
		// Create the interstitial
	    final InterstitialAd interstitial = new InterstitialAd(activity, "a152df3740ba1f9");

	    // Create ad request
	    AdRequest adRequest = new AdRequest();

	    // Begin loading your interstitial
	    interstitial.loadAd(adRequest);

	    // Set Ad Listener to use the callbacks below
	    interstitial.setAdListener(new AdListener() {
			
			@Override
			public void onReceiveAd(Ad arg0) {
				// TODO Auto-generated method stub
				Log.d("OK", "Received ad");
			    if (arg0 == interstitial) {
			      interstitial.show();
			    }
			}
			
			@Override
			public void onPresentScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLeaveApplication(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDismissScreen(Ad arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
