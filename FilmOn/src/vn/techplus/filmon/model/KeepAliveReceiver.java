package vn.techplus.filmon.model;

import vn.techplus.filmon.utils.Utils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class KeepAliveReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (Utils.isNetworkConnected(context))
			new AsyncRequestKeepAlive().execute();
	}

	class AsyncRequestKeepAlive extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			FilmOnService service = FilmOnService.getInstance();
			while (!service.requestKeepAlive()) {
				FilmOnService.getInstance().initSessionKey(null);
			}
			
			return null;
		}

	}
}
