package vn.techplus.filmon.pushNotification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.groboot.pushapps.PushAppsMessageInterface;
import com.groboot.pushapps.PushAppsRegistrationInterface;
import com.groboot.pushapps.PushManager;

public class PushNotification implements PushAppsMessageInterface {
	private static final String GOOGLE_API_PROJECT_NUMBER = "584465696748";
	private static final String PUSHAPPS_APP_TOKEN = "eed78c40-534d-4ed4-94a8-77b2d2a3dfd5";
	
	private static Class<Activity> mActivityNotification = null;

	public static void register(Context context) {
		PushManager.init(context, GOOGLE_API_PROJECT_NUMBER, PUSHAPPS_APP_TOKEN);

		// optional, default is the app icon
		// PushManager.getInstance(context).setNotificationIcon(R.drawable.ic_launcher);
		// optional , if not set then the launcher activity will called after clicking the notification
		// PushManager.getInstance(getApplicationContext()).setIntentNameToLaunch("com.groboo.pushapps.SomeActivity");
		PushManager.getInstance(context).registerForRegistrationEvents(
						new PushAppsRegistrationInterface() {

							@Override
							public void onUnregistered(Context paramContext,
									String paramString) {
								// TODO Auto-generated method stub
								Log.d("DemoApplication", "unREGISTERED: "
										+ paramString);
							}

							@Override
							public void onRegistered(Context paramContext,
									String paramString) {
								Log.d("DemoApplication", "REGISTERED: "
										+ paramString);

					}
				});
		// Can override this listener by setOnMessageEventListener
		PushManager.getInstance(context).registerForMessagesEvents(new PushNotification());
	}
	
	
	public static void unregister(Context context) {
		PushManager.getInstance(context).unregister(context);
	}
	
	public static void setOnMessageEventListener(Context context, PushAppsMessageInterface messageListener) {
		PushManager.getInstance(context).registerForMessagesEvents(messageListener);
	}

	public static void setActivityForNotfication(Class<Activity> activity) throws Exception {
		if (activity == Activity.class) {
			mActivityNotification = activity;
		}
		else {
			throw new RuntimeException("This is not a Activity");
		}
	}
	
	public static Class<Activity> getActivityNotification() {
		return mActivityNotification;
	}


	@Override
	public void onMessage(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.w(getClass().getName(), "OnMessage");		
		Bundle bundle = arg1.getExtras();
		if (bundle == null)
			return;
		
		Intent iNotification = new Intent("vn.techplus.filmon.PUSH_NOTIFICATION");
		iNotification.putExtras(bundle);
		arg0.sendBroadcast(iNotification);
	}
}
