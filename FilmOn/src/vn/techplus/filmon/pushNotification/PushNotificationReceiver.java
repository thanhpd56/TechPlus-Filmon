package vn.techplus.filmon.pushNotification;

import vn.techplus.filmon.R;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.groboot.pushapps.PushManager;

public class PushNotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		Bundle bundle = arg1.getExtras();
		if (bundle == null) 
			return;
		
		String title = null;
		String msg = null;
		String link = null;
		String sound = null;
		if (bundle.containsKey(PushManager.NOTIFICATION_MESSAGE_KEY))  {
			msg = arg1.getExtras().getString(PushManager.NOTIFICATION_MESSAGE_KEY);
			Log.d("DemoApplication", "Message: " + msg);
		}
		if (bundle.containsKey(PushManager.NOTIFICATION_TITLE_KEY))  {
			title = arg1.getExtras().getString(PushManager.NOTIFICATION_TITLE_KEY);
			Log.d("DemoApplication", "Title: " + title);
		}
		if (bundle.containsKey(PushManager.NOTIFICATION_LINK_KEY))  {
			link = arg1.getExtras().getString(PushManager.NOTIFICATION_LINK_KEY);
			Log.d("DemoApplication", "Link: " + link);
		}
		if (bundle.containsKey(PushManager.NOTIFICATION_SOUND_KEY))  {
			sound = arg1.getExtras().getString(PushManager.NOTIFICATION_SOUND_KEY);
			Log.d("DemoApplication", "Sound: " + sound);
		}
		
		if (msg == null)
			return;
		
		showOnStatusBar(arg0, msg, title);
		showCustomToast(arg0, msg, title);
	}

	private void showOnStatusBar(Context context, String msg, String title) {
		if (TextUtils.isEmpty(title))
			title = context.getResources().getString(R.string.app_name);
		
		NotificationManager notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setAutoCancel(true)
				.setContentText(msg);
		
		Class<Activity> clazz = PushNotification.getActivityNotification();
		if (clazz != null) {
			Intent iNotification = new Intent(context, clazz);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(clazz);
			stackBuilder.addNextIntent(iNotification);
			PendingIntent pendingNotification = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(pendingNotification);
		}
		else {
			Intent iNotification = new Intent(Intent.ACTION_MAIN);
			iNotification.setPackage(context.getPackageName());
			PendingIntent pendingNotification = PendingIntent.getActivity(context, 100, iNotification, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.setContentIntent(pendingNotification);
		}
		notifManager.notify(0, builder.build());
	}
	
	private void showCustomToast(final Context context, String msg, String title) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.custom_toast, null);
		final TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		final TextView txtMsg = (TextView) view.findViewById(R.id.txtMessage);
		txtMsg.setText(msg);
		if (!TextUtils.isEmpty(title))
			txtTitle.setText(title);
		
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}
	
}
