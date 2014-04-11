package vn.techplus.filmon.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.techplus.filmon.utils.JSONParser;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

/**
 * @author chuyen.nguyen
 *
 */
public class FilmOnService {
	private static final String URL_INIT = "http://www.filmon.com/tv/api/init";
	private static final String URL_KEEP_ALIVE = "http://www.filmon.com/tv/api/keep-alive?session_key=";
//	private static final String URL_LOGIN = "http://www.filmon.com/tv/api/login?session_key=";
	private static final String URL_CHANNEL_INFO = "http://www.filmon.com/tv/api/channel/channelId?session_key=";
	private static final String URL_GROUPS = "http://www.filmon.com/tv/api/groups?session_key=";
	private static final String URL_CHANNEL_GROUP = "http://www.filmon.com/tv/api/group/groupId?session_key=";
	private static final String URL_CHANNELS = "http://www.filmon.com/tv/api/channels?session_key=";
	
	/*JSON keys*/
	private static final String GROUP_ID = "group_id";
	private static final String GROUP_TITLE = "group";
	private static final String CHANNEL_ID = "id";
	private static final String CHANNEL_TITLE = "title";
	private static final String CHANNEL_LOGO = "big_logo";
	private static final String FREE_HD = "is_free";
	private static final String FREE_SD = "is_free_sd_mode";
	private static final String SEEKABLE = "seekable";
	private static final String IS_VOD = "is_vod";
	private static final String IS_VOX = "is_vox";
	
	private static final int INTERVAL_REQ_KEEP_ALIVE = 5 * 60000;
	
	private static FilmOnService mService;
	private String mSessionKey;
	private PendingIntent mPendingKeepAlive;
	
	public static FilmOnService getInstance() {
		if (mService == null) {
			synchronized (FilmOnService.class) {
				if (mService == null) {
					mService = new FilmOnService();
				}
			}
		}
		
		return mService;
	}
	
	private FilmOnService() { }

	/**
	 * initial session key what be used to transaction with server 
	 * @param context
	 * @return
	 */
	public String initSessionKey(Context context) {
		JSONParser parser = new JSONParser();
		JSONObject jObj = parser.getJSONFromUrl(URL_INIT);
		if (jObj == null)
			return null;
		
		mSessionKey = jObj.optString("session_key");
		if (context != null)
			startLoopKeepAlive(context);
		
		Log.i(getClass().getSimpleName(), "session_key: " + mSessionKey);
		return mSessionKey;
	}
		
	/**
	 * start alarm to sending request keep-alive for current session key
	 * @param context
	 */
	public void startLoopKeepAlive(Context context) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent iKeepAlive = new Intent("vn.techplus.filmon.KEEP_ALIVE");
		mPendingKeepAlive = PendingIntent.getBroadcast(context, 10, iKeepAlive, PendingIntent.FLAG_UPDATE_CURRENT);
		manager.setInexactRepeating(AlarmManager.RTC, INTERVAL_REQ_KEEP_ALIVE, INTERVAL_REQ_KEEP_ALIVE, mPendingKeepAlive);
	}
	
	/**
	 * stop alarm to sending request keep-alive for current session key
	 * @param context
	 */
	public void stopLoopKeepAlive(Context context) {
		if (mPendingKeepAlive != null) {
			AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			manager.cancel(mPendingKeepAlive);
		}
	}
	
	public static void release() {
		mService = null;
	}
	
	// send request for keep session key
	public boolean requestKeepAlive() {
		JSONParser parser = new JSONParser();
		JSONObject jObj = parser.getJSONFromUrl(URL_KEEP_ALIVE + mSessionKey);
		if (jObj == null || !jObj.optBoolean("success"))
			return false;
			
		Log.i(getClass().getSimpleName(), "resp keep-alive: " + jObj.toString());
		return true;
	}
	
	public List<Category> getAllCategory(Context context) {
		if (TextUtils.isEmpty(mSessionKey)) {
			String sessionKey = initSessionKey(context);
			if (TextUtils.isEmpty(sessionKey)) 
				return null;
		}
		
		
		String categoriesUrl = URL_GROUPS + mSessionKey;
		JSONParser parser = new JSONParser();
		JSONArray jArray = parser.getJSONArrayFromUrl(categoriesUrl);
		if (jArray == null)
			return null;
		
		int total = jArray.length();
		List<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < total; i++) {
			JSONObject jCategory = jArray.optJSONObject(i);
			if (jCategory == null)
				continue;
			
			Category category = new Category();
			category.setId(jCategory.optString("id"));
			category.setName(jCategory.optString("name"));
			category.setLogo(jCategory.optString("logo_uri"));
			categories.add(category);
		}
		return categories;
	}
	
	public SparseArray<Object> getChannels(Context context) {
		if (TextUtils.isEmpty(mSessionKey)) {
			String sessionKey = initSessionKey(context);
			if (TextUtils.isEmpty(sessionKey)) 
				return null;
		}
		
		String channelsUrl = URL_CHANNELS + mSessionKey;
		JSONParser parser = new JSONParser();
		JSONArray jArray = parser.getJSONArrayFromUrl(channelsUrl);
		if (jArray == null)
			return null;
		
		int total = jArray.length();
		ChannelManagement channelManager = ChannelManagement.getInstance();
		HashMap<String, List<Channel>> dictChannels = new HashMap<String, List<Channel>>();
		List<Category> categories = new ArrayList<Category>();
		List<Channel> channels = null;
		for (int i = 0; i < total; i++) {
			JSONObject jChannel = jArray.optJSONObject(i);
			if (jChannel == null)
				continue;
			
			Channel channel = new Channel();
			String freeHD = jChannel.optString(FREE_HD);
			String freeSD = jChannel.optString(FREE_SD);
			if (!freeHD.equals("1") && !freeSD.equals("1")) 
				// this channel is not free
				continue;
				
			channel.setHD(freeHD.equals("1") ? true : false);
			channel.setName(jChannel.optString(CHANNEL_TITLE));
			channel.setId(jChannel.optString(CHANNEL_ID));
			channel.setCategoryId(jChannel.optString(GROUP_ID));
			channel.setLogo(jChannel.optString(CHANNEL_LOGO));
			/*if (!TextUtils.isEmpty(channel.getLogo())) {
				channel.setLogo(channel.getLogo().replace("big_logo", "extra_big_logo"));
			}*/
			channel.setSeekable(jChannel.optBoolean(SEEKABLE, false));
			channel.setMyChannel(channelManager.isMyChannel(context, channel.getId()));
			
			String nameCategory = jChannel.optString(GROUP_TITLE);
			channels = dictChannels.get(nameCategory);
			if (channels != null) {
				channels.add(channel);
			} else {
				Category category = new Category();
				category.setId(jChannel.optString(GROUP_ID));
				category.setName(nameCategory);
				category.setLogo("http://static.filmon.com/couch/groups/" + category.getId() + "/logo.png?v2");
				categories.add(category);
				
				channels = new ArrayList<Channel>();
				channels.add(channel);
				dictChannels.put(nameCategory, channels);
			}
		}
		
		SparseArray<Object> result = new SparseArray<Object>();
		result.put(1, categories);
		result.put(2, dictChannels);
		return result;
	}
	
	public List<Channel> getChannelsByCategory(Context context, String categoryId) {
		if (TextUtils.isEmpty(mSessionKey)) {
			String sessionKey = initSessionKey(context);
			if (TextUtils.isEmpty(sessionKey)) 
				return null;
		}
		
		String channelsByCategoryUrl = URL_CHANNEL_GROUP.replace("groupId", categoryId) + mSessionKey;
		JSONParser parser = new JSONParser();
		JSONObject jObj = parser.getJSONFromUrl(channelsByCategoryUrl);
		if (jObj == null)
			return null;
		
		JSONArray jArray = jObj.optJSONArray("channels");
		if (jArray == null)
			return null;
		
		int total  = jArray.length();
		ChannelManagement channelManager = ChannelManagement.getInstance();
		List<Channel> channels = new ArrayList<Channel>();
		for (int i = 0; i < total; i++) {
			JSONObject jChannel = jArray.optJSONObject(i);
			if (jChannel == null)
				continue;
			
			Channel channel = new Channel();
			boolean freeHD = jChannel.optBoolean(FREE_HD, false);
			boolean freeSD = jChannel.optBoolean(FREE_SD, false);
			if (!freeHD && !freeSD)
				continue;
			
			channel.setHD(freeHD);
			channel.setId(jChannel.optString(CHANNEL_ID));
			channel.setName(jChannel.optString(CHANNEL_TITLE));
			channel.setCategoryId(categoryId);
			channel.setLogo(jChannel.optString(CHANNEL_LOGO));
			/*if (!TextUtils.isEmpty(channel.getLogo())) {
				channel.setLogo(channel.getLogo().replace("big_logo", "extra_big_logo"));
			}*/
			channel.setSeekable(jChannel.optBoolean(IS_VOD, false) || jChannel.optBoolean(IS_VOX, false));
			channel.setMyChannel(channelManager.isMyChannel(context, channel.getId()));
			channels.add(channel);
		}		
		return channels;
	}
	
	public List<String> getLinkStreamingOfChannel(Context context, String channelId, boolean hightQuality) {
		if (TextUtils.isEmpty(mSessionKey)) {
			String sessionKey = initSessionKey(context);
			if (TextUtils.isEmpty(sessionKey)) 
				return null;
		}
		
		String urlChannelInfo = URL_CHANNEL_INFO.replace("channelId", channelId) + mSessionKey;
		JSONParser parser = new JSONParser();
		JSONObject jObj = parser.getJSONFromUrl(urlChannelInfo);
		if (jObj == null)
			return null;
		
		JSONArray jStreams = jObj.optJSONArray("streams");
		List<String> linkStreams = new ArrayList<String>();
		if (jStreams != null) {
			
			int total = jStreams.length();
			for (int i = 0; i < total; i++) {
				JSONObject jStream = jStreams.optJSONObject(i);
				if (jStream.optString("quality").contains("low")
						|| (hightQuality && jStream.optString("quality").contains("high"))) {
					
					String link = extractFullLink(jStream);
					if (TextUtils.isEmpty(link))
						continue;
					linkStreams.add(link);					
				}
			}						
		}
		
		if (linkStreams == null || linkStreams.size() < 1)
			Log.i(getClass().getSimpleName(), jObj.toString() + "\n url: " + urlChannelInfo);
		return linkStreams;
	}
	
	private String extractFullLink(JSONObject jStream) {
		String linkStream = null;		
		String url = jStream.optString("url");
		if (TextUtils.isEmpty(url))
			return linkStream;
		
		String name = jStream.optString("name");
		if (url.contains("?id="))
			linkStream = url + "&here=now/" + name;
		else
			linkStream = url + "/" + name;
		return linkStream;
	}
}
