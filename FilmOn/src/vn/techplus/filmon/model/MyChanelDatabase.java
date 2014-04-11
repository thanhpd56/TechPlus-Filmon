package vn.techplus.filmon.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyChanelDatabase {
	private static final String DB_NAME = "MyChannelDB";
	private static final int DB_VERSION = 1;
	
	private static final String TB_NAME = "myChannels";
	private static final String COL_ID = "_id";
	private static final String COL_CHANNELID = "CHANNELID";
	private static final String COL_NAME = "name";
	private static final String COL_LOGO = "logo";
	private static final String COL_TYPE = "type";
	private static final String COL_CATEGORY_ID = "categoryId";
	private static final String COL_MY_CHANNEL = "myChannel";
	private static final String COL_SEEKABLE = "seekable";
	private static final String COL_HD = "hd";
	
	//private Context mContext;
	private DataBaseHelper mDBHelper;
	private SQLiteDatabase mDB;
	
	public MyChanelDatabase(Context context) {
		//mContext = context;
		mDBHelper = new DataBaseHelper(context);		
	}
	
	class DataBaseHelper extends SQLiteOpenHelper {
		private static final String CREATE_TABLE = "CREATE TABLE " + TB_NAME + " ( "
						+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
						+ COL_CHANNELID + " TEXT NOT NULL, "
						+ COL_NAME + " TEXT NOT NULL, "
						+ COL_LOGO + " TEXT, "
						+ COL_TYPE + " TEXT, "
						+ COL_CATEGORY_ID + " TEXT, "
						+ COL_MY_CHANNEL + " BOOL,"
						+ COL_HD + " BOOL,"
						+ COL_SEEKABLE + " BOOL"
						+ " )";

		public DataBaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public SQLiteDatabase open() {
		mDB = mDBHelper.getWritableDatabase();
		return mDB;
	}
	
	public void close() {
		if (mDB != null)
			mDB.close();
	}
	
	public long addChannel(Channel channel) {
		ContentValues values = new ContentValues();
		values.put(COL_CHANNELID, channel.getId());
		values.put(COL_NAME, channel.getName());
		values.put(COL_LOGO, channel.getLogo());
		values.put(COL_TYPE, channel.getType());
		values.put(COL_CATEGORY_ID, channel.getCategoryId());
		values.put(COL_MY_CHANNEL, channel.isMyChannel());
		values.put(COL_HD, channel.isHD());
		values.put(COL_SEEKABLE, channel.isSeekable());
		return mDB.insert(TB_NAME, null, values);
	}
	
	public List<Channel> getMyChannels() {
		Cursor cursor = mDB.query(TB_NAME, null, null, null, null, null, null);
		try {
			List<Channel> channels = new ArrayList<Channel>();
			while (cursor.moveToNext()) {
				Channel channel = new Channel();
				channel.setId(cursor.getString(1));
				channel.setName(cursor.getString(2));
				channel.setLogo(cursor.getString(3));
				channel.setType(cursor.getString(4));
				channel.setCategoryId(cursor.getString(5));
				channel.setMyChannel(cursor.getInt(6) > 0 ? true : false);
				channel.setHD(cursor.getInt(7) > 0 ? true : false);
				channel.setSeekable(cursor.getInt(8) > 0 ? true : false);
				channels.add(channel);
			}
			return channels;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
	
	public void removeChannel(Channel channel) {
		removeChannel(channel.getId());
	}
	
	public int removeChannel(String channelId) {
		String whereClause = COL_CHANNELID + " LIKE " + channelId;
		return mDB.delete(TB_NAME, whereClause, null);
	}
	
	public boolean isExist(Channel channel) {
		return isExist(channel.getId());
	}
	
	public boolean isExist(String channelId) {
		String whereClause = COL_CHANNELID + " LIKE " + channelId;		
		Cursor cursor = mDB.query(TB_NAME, new String[]{COL_CHANNELID}, whereClause, null, null, null, null);
		try {
		cursor.moveToFirst();
		if (cursor.getCount() > 0)
			return true;
		
		return false;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}
}
