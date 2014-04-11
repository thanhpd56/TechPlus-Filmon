package vn.techplus.filmon.moviesreview;

import java.util.List;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MovieReviewDetailActivity extends Activity implements OnClickListener {
	public static final String EXTRA_MOVIE = "movie information";
	
	private TextView mTxtTitle, mTxtOverview, mTxtDirector, mTxtCasts, mTxtReleaseDate, mTxtVote;
	private ImageView mImgPoster;
	private LinearLayout mDescView;
	private RelativeLayout mViewRoot;
	private View mViewTrailer;
	
	private MovieInfo mMovieInfo;
	private boolean mIsLoading = false;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions mDisplayOptions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_review_detail);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle == null || (mMovieInfo = bundle.getParcelable(EXTRA_MOVIE)) == null) {
			finish();
			return;
		}
		
		mViewRoot = (RelativeLayout) findViewById(R.id.root);
		mViewTrailer = findViewById(R.id.btnTrailer);
		mTxtTitle = (TextView) findViewById(R.id.txtTitleMovie);
		mTxtOverview = (TextView) findViewById(R.id.txtOverview);
		mTxtDirector = (TextView) findViewById(R.id.txtDirector);
		mTxtCasts = (TextView) findViewById(R.id.txtCast);
		mTxtReleaseDate = (TextView) findViewById(R.id.txtReleaseDate);
		mTxtVote = (TextView) findViewById(R.id.txtImdbVote);
		mDescView = (LinearLayout) findViewById(R.id.desArea);
		mImgPoster = (ImageView) findViewById(R.id.imgPoster);
		
		mViewRoot.setOnClickListener(this);
		mDescView.setOnClickListener(this);
		mViewTrailer.setOnClickListener(this);
		
		mDisplayOptions = FilmOnApplication.getDisplayOption(R.drawable.ic_app);
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.displayImage(mMovieInfo.getPosterImage(), mImgPoster, mDisplayOptions);
		mImageLoader.loadImage(mMovieInfo.getBackdropImage(), mDisplayOptions, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					mDescView.setBackground(new BitmapDrawable(getResources(), arg2));
				}
				else {
					mDescView.setBackgroundDrawable(new BitmapDrawable(getResources(), arg2));
				}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		
		new AsyncLoadingDetailMovie().execute(mMovieInfo.getId());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.desArea :
			break;
		case R.id.root :
			finish();
			break;
			
		case R.id.btnTrailer :
			Toast.makeText(getApplicationContext(), "coming soon!", Toast.LENGTH_SHORT).show();
			break;
			
			default: 
				break;
		}
	}
	
	private void setContents() {
		mTxtTitle.setText(getTitleMovie(mMovieInfo.getTitle()), BufferType.SPANNABLE);
		mTxtOverview.setText(getOverview(mMovieInfo.getOverview()), BufferType.SPANNABLE);
		mTxtDirector.setText(getDirector(mMovieInfo.getDirector()), BufferType.SPANNABLE);
		mTxtCasts.setText(getCasts(mMovieInfo.getCasts()), BufferType.SPANNABLE);
		mTxtReleaseDate.setText(getReleaseDate(mMovieInfo.getReleaseDate()), BufferType.SPANNABLE);
		mTxtVote.setText(getImdbVote(mMovieInfo.getImdbVote()), BufferType.SPANNABLE);
	}

	private SpannableStringBuilder getTitleMovie(String plainText) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(plainText);
		return builder;
	}
	
	private SpannableStringBuilder getOverview(String plainText) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(plainText);
		builder.setSpan(new ForegroundColorSpan(Color.rgb(237, 218, 116)), 0, plainText.length(), 0);
		return builder;
	}
	
	private SpannableStringBuilder getReleaseDate(String plainText) {
		SpannableStringBuilder builder = new SpannableStringBuilder("Release date :  ");
		builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 165, 00)), 0, builder.length(), 0);
		int currPoint = builder.length();
		builder.append(plainText);
		builder.setSpan(new ForegroundColorSpan(Color.rgb(229, 103, 23)), currPoint, builder.length(), 0);
		return builder;
	}
	
	private SpannableStringBuilder getDirector(String plainText) {
		SpannableStringBuilder builder = new SpannableStringBuilder("Director :  ");
		builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 165, 00)), 0, builder.length(), 0);
		if (!TextUtils.isEmpty(plainText)) {
			int currPoint = builder.length();
			builder.append(plainText);
			builder.setSpan(new ForegroundColorSpan(Color.rgb(229,  103,  23)), currPoint, builder.length(), 0);
		}
		return builder;
	}

	private SpannableStringBuilder getCasts(List<String> plainTexts) {
		SpannableStringBuilder builder = new SpannableStringBuilder("Casts :  ");
		builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 165, 00)), 0, builder.length(), 0);
		int currPoint = builder.length();
		if (plainTexts != null) {
			for (String string: plainTexts) {
				builder.append(string + ", ");
				int posDash = string.indexOf("-");
				if (posDash == -1)
					continue;				
				currPoint += posDash + 1;
				builder.setSpan(new ForegroundColorSpan(Color.rgb(00, 128, 128)), currPoint, builder.length() - 2, 0);
				currPoint = builder.length();
			}
		}
		//builder.append(plainTexts.toString());
		return builder;
	}

	private SpannableStringBuilder getImdbVote(String plainText) {
		SpannableStringBuilder builder = new SpannableStringBuilder("IMDB :  ");
		builder.setSpan(new ForegroundColorSpan(Color.rgb(255, 165, 00)), 0, builder.length(), 0);
		int currPos = builder.length();
		builder.append(plainText);
		builder.setSpan(new ForegroundColorSpan(Color.rgb(248, 128, 23)), currPos, builder.length(), 0);
		return builder;
	}
	
	
	class AsyncLoadingDetailMovie extends AsyncTask<Integer, Void, MovieInfo> {

		@Override
		protected MovieInfo doInBackground(Integer... params) {
			mIsLoading = true;
			if (params == null || params.length == 0)
				return null;
			
			return MovieManager.getInstance().getFullDetailMovie(params[0]);
		}

		@Override
		protected void onPostExecute(MovieInfo result) {
			super.onPostExecute(result);
			mIsLoading = false;
			if (result != null) {
				mMovieInfo = result;
				setContents();
			}
		}
		
	}


}
