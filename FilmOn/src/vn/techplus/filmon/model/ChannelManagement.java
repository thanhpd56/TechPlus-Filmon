package vn.techplus.filmon.model;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

import vn.techplus.filmon.FilmOnApplication;
import vn.techplus.filmon.utils.Utils;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.SparseArray;


public class ChannelManagement extends Observable {
	/*interface listener when updating data finished*/
	public interface OnUpdateFinished {
		public void onFinished(boolean isSuccessful);
	}
		
	public static final String MY_CATEGORY_ID = "-1";	
		
	private static ChannelManagement mManager;
	
	private ArrayList<Category> mAllCategories;
	private HashMap<String, List<Channel>> mAllChannels;
	private List<Channel> mMyChannels;
	
	private OnUpdateFinished mOnUpdateFinished; 
	
	
	public static ChannelManagement getInstance() {
		if (mManager == null) {
			synchronized(ChannelManagement.class) {
				if (mManager == null)
					mManager = new ChannelManagement();
			}
		}
		
		return mManager;
	}
	
	private ChannelManagement() {}
	
	public void release() {
		clear();
		mManager = null;
	}
	
	/**
	 * Remove all data
	 */
	public void clear() {
		if (mAllCategories != null) {
			mAllCategories.clear();
		}
		if (mAllChannels != null) {
			mAllChannels.clear();
		}
	}
	
	/**
	 * Load new data to replace for old data, 
	 * When finish loading data, callback onUpdateFinished 
	 * 
	 * @param context
	 * @param updateFinishedListener
	 */
	public void reloadChannels(Context context, OnUpdateFinished updateFinishedListener) {
		mOnUpdateFinished = updateFinishedListener;
		new AsyncLoadData(context).execute();
	}
	
	/**
	 * retrieve all category
	 * 
	 * @return list category or null if have not data
	 */
	public List<Category> getCategories() {
		return mAllCategories;
	}
	
	/**
	 * retrieve list channels in a same category
	 * 
	 * @param context
	 * @param category Category is need retrieve channels
	 * @return list channels if is found, else null
	 */
	public List<Channel> getChannelsByCategory(Context context, Category category) {
		List<Channel> channels = null;
		if (category.getId().equals(MY_CATEGORY_ID)) {
			// retrieve list my channels
			channels = new ArrayList<Channel>(mMyChannels);
			
		} else {
			// retrieve list channels in the same category
			if (mAllChannels != null) {			
				channels = mAllChannels.get(category.getName());
			}
					
			if (channels == null && Looper.myLooper() != Looper.getMainLooper()) {
				if (category.getId().equals(ManualChannel.SPECIAL_CATEGORY_ID)) {
					channels = ManualChannel.update(context);
				} else {
					FilmOnService service = FilmOnService.getInstance();
					channels = service.getChannelsByCategory(context, category.getId());					
				}
				
				if (mAllChannels == null)
					mAllChannels = new HashMap<String, List<Channel>>();
				mAllChannels.put(category.getName(), channels);
				
			}
		}
		return channels;
	}
	
	/**
	 * Retrieve list channels, result of searching with key
	 * 
	 * @param keySearch
	 * @return
	 */
	public ArrayList<Channel> findChannelsByKey(String keySearch) {
		if (mAllChannels == null || mAllChannels.values().size() < 1)
			return new ArrayList<Channel>();
		
		ArrayList<Channel> arrayResult = new ArrayList<Channel>();
		for (List<Channel> array: mAllChannels.values()) {
			for (Channel channel: array) {
				if (channel.getName().toLowerCase().contains(keySearch)) {
					arrayResult.add(channel);
				}
			}
		}
		return arrayResult;
	}
	
	/**
	 * load list favorite channels from database to cache 
	 * @param context
	 * @return
	 */
	public List<Channel> loadMyChannels(Context context) {
		MyChanelDatabase db = new MyChanelDatabase(context);
		try {
			db.open();
			mMyChannels = db.getMyChannels();
			db.close();
			return mMyChannels;
		} catch (SQLException e) {
			e.printStackTrace();
			if (db != null)
				db.close();
		}
		
		return null;
	}
	
	/**
	 * add a channel to list favorite channels 
	 * @param context
	 * @param channel
	 */
	public void addMyChannels(Context context, Channel channel) {
		MyChanelDatabase db = new MyChanelDatabase(context);
		try {
			db.open();
			if (!db.isExist(channel)) {
				db.addChannel(channel);
				setChanged();
				mMyChannels.add(channel);
				notifyObservers();
			}
			db.close();			
		} catch (SQLException e) {
			e.printStackTrace();
			if (db != null)
				db.close();
		}
	}
	
	/**
	 * remove the channel from list favorite channels
	 * @param context
	 * @param channelId
	 */
	public void removeMyChannels(Context context, String channelId) {
		MyChanelDatabase db = new MyChanelDatabase(context);
		try {
			db.open();
			db.removeChannel(channelId);
			db.close();
			for (Channel channel: mMyChannels) {
				if (channel.getId().equals(channelId)) {
					mMyChannels.remove(channel);
					break;
				}
			}
			setChanged();
			notifyObservers();
		} catch (SQLException e) {
			e.printStackTrace();
			if (db != null)
				db.close();
		}
	}
		
	/**
	 * get link stream with highest quality what is supported by server
	 * shouldn't call this method in main thread 
	 * 
	 * @param context
	 * @param channel
	 * @return
	 */
	public List<String> getLinkStream(Context context, Channel channel) {
		FilmOnService service = FilmOnService.getInstance();
		List<String> links = service.getLinkStreamingOfChannel(context, channel.getId(), channel.isHD());
		if (links == null || links.size() < 1) {
			return null;
		} else {
			return links;
		}
	}
	
	/**
	 * check the channel is in list favorite channels or not
	 * @param context
	 * @param channelId
	 * @return boolean
	 */
	public boolean isMyChannel(Context context, String channelId) {
		MyChanelDatabase db = new MyChanelDatabase(context);
		try {
			db.open();
			return db.isExist(channelId);		
		} catch (SQLException e) {
			e.printStackTrace();			
		} finally {
			if (db != null)
				db.close();
		}
		return false;
	}
	
	
	private void updateCategoryFilmOn(Context context) {
		FilmOnService service = FilmOnService.getInstance();
		List<Category> categories = service.getAllCategory(context);
		if (categories != null && categories.size() > 0) {
			
			if (mAllCategories == null) {
				mAllCategories = new ArrayList<Category>(categories);
			} else {
				mAllCategories.clear();
				mAllCategories.addAll(categories);
			}			
			mAllCategories.add(0, new Category(FilmOnApplication.myCategory, MY_CATEGORY_ID, ""));
			mAllCategories.add(1, new Category(FilmOnApplication.specialCategory, ManualChannel.SPECIAL_CATEGORY_ID, ""));
			
			if (mAllChannels != null) {
				mAllChannels.clear();
			}
		}
	}
	
	private void updateChannelsFilmOn(Context context) {
		FilmOnService service = FilmOnService.getInstance();
		SparseArray<Object> array = service.getChannels(context);
		if (array == null || array.size() < 2) {
			return;
		}
		
		List<Category> categories = (List<Category>) array.get(1);
		HashMap<String, List<Channel>> dictChannels = (HashMap<String, List<Channel>>) array.get(2);
		if (categories != null && categories.size() > 0) {
			
			if (mAllCategories == null) {
				mAllCategories = new ArrayList<Category>(categories);
			} else {
				mAllCategories.clear();
				mAllCategories.addAll(categories);
			}			
			mAllCategories.add(0, new Category(FilmOnApplication.myCategory, MY_CATEGORY_ID, ""));
			mAllCategories.add(1, new Category(FilmOnApplication.specialCategory, ManualChannel.SPECIAL_CATEGORY_ID, ""));
			
			if (mAllChannels != null) {
				mAllChannels.putAll(dictChannels);;
			}
		}
	}
	
	
	class AsyncLoadData extends AsyncTask<Void, Void, Boolean> {
		WeakReference<Context> mWeakContext = null;
		
		public AsyncLoadData(Context context) {
			mWeakContext = new WeakReference<Context>(context);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub			
			if (mWeakContext.get() == null)
				return false;
			
			if (!Utils.isNetworkConnected(mWeakContext.get()))
				return false;
			
			updateCategoryFilmOn(mWeakContext.get());
			//updateChannelsFilmOn(mWeakContext.get());
			if (getCategories() == null || getCategories().size() == 0)
				return false;
			else
				return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			if (mOnUpdateFinished != null) {
				mOnUpdateFinished.onFinished(result.booleanValue());
			}
			
			if (result) {
				setChanged();
				notifyObservers();
			}
		}
		
	}

}
