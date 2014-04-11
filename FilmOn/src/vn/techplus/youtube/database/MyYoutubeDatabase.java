package vn.techplus.youtube.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyYoutubeDatabase {
	private static final String DATABASE_NAME = "myVideos";
	private static final String DATABASE_VERSION = "1";
	private static final String TABLE_NAME = "";
	private static final String COL_ID = "";
	private static final String COL_TITLE = "";
	private static final String COL_THUMB = "";
	private static final String COL_URL = "";
	
	
	class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
