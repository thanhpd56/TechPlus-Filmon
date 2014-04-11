package vn.techplus.filmon;

import vn.techplus.filmon.youtube.model.MovieParser;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class Youtube extends Activity {

	private TextView txtResult;
	private MovieParser movieParser;
	private Context mContext;
	static final String PATH = "YT_Channels.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_youtube);
		mContext = getApplicationContext();
		txtResult = (TextView) findViewById(R.id.txtResult);
		txtResult.setText(movieParser.getDataProvider(mContext, PATH)
				.toString());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.youtube, menu);
		return true;
	}

}
