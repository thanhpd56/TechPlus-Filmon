package vn.techplus.filmon;

import java.util.ArrayList;
import java.util.List;

import vn.techplus.filmon.Adapter.ChannelGridAdapter;
import vn.techplus.filmon.model.Channel;
import vn.techplus.filmon.model.ChannelManagement;
import vn.techplus.filmon.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchAcitvity extends Activity implements OnClickListener, OnItemClickListener, OnEditorActionListener {
	private GridView mListChannel;
	private ImageView mBtnSearch;
	private EditText mTxtBoxSearch;
	private ChannelGridAdapter mChannelAdapter;
	
	private ArrayList<Channel> mResultSearchs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_acitvity);
		
		mListChannel = (GridView) findViewById(R.id.gridChannels);
		mBtnSearch = (ImageView) findViewById(R.id.btnSearch);
		mTxtBoxSearch = (EditText) findViewById(R.id.editTextSearch);
		
		mResultSearchs = new ArrayList<Channel>();
		mChannelAdapter = new ChannelGridAdapter(this, mResultSearchs);		
		mListChannel.setAdapter(mChannelAdapter);
		mListChannel.setOnItemClickListener(this);
		mBtnSearch.setOnClickListener(this);
		mTxtBoxSearch.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Gillsans.ttf"));
		mTxtBoxSearch.setOnEditorActionListener(this);
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		if (v == mTxtBoxSearch && actionId == EditorInfo.IME_ACTION_SEARCH) {
			search();
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			return true;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Channel channel = mChannelAdapter.getItem(arg2);
		if (channel != null) {
			Intent iPlayer = new Intent(this, TVPlayer.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable(TVPlayer.EXTRA_CHANNEL, channel);
			iPlayer.putExtras(bundle);
			startActivity(iPlayer);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnSearch) {
			search();
		}
	}
	
	@SuppressLint("DefaultLocale")
	private void search() {
		String key = mTxtBoxSearch.getText().toString().trim().toLowerCase();
		if (TextUtils.isEmpty(key)) {
			Toast.makeText(this, R.string.hint_search_channel, Toast.LENGTH_SHORT).show();
		}
		else {
			mResultSearchs.clear();
			List<Channel> result = ChannelManagement.getInstance().findChannelsByKey(key);
			if (result == null) 
				return;
			
			mResultSearchs.addAll(result);
			mChannelAdapter.notifyDataSetChanged();
			
		}
	}

}
