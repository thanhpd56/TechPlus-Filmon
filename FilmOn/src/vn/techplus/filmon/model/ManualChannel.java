package vn.techplus.filmon.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.techplus.filmon.utils.JSONParser;
import android.content.Context;

public class ManualChannel {
	public static final String SPECIAL_CATEGORY_ID = "-2";
	private static final String URL_MANUAL_DATA = "http://devzone.techplus.com.vn/appconfig/filmonplus_config.json";
	
	public static List<Channel> update(Context context) {
		JSONParser jParser = new JSONParser();
		JSONArray jChannels = jParser.getJSONArrayFromUrl(URL_MANUAL_DATA);
		if (jChannels == null)
			return null;
		
		List<Channel> arrayChannels = new ArrayList<Channel>();
		int totalStream = jChannels.length();
		JSONObject jChannel = null;
		for (int k = 0; k < totalStream; k++) {
			jChannel = jChannels.optJSONObject(k);
			if (jChannel == null)
				continue;
			
			Channel channel = new Channel();
			channel.setName(jChannel.optString("name"));
			channel.setId(channel.getName());
			channel.setLogo(jChannel.optString("logo"));
			channel.setCategoryId(SPECIAL_CATEGORY_ID);
			channel.setStreamUrl(jChannel.optString("stream"));
			arrayChannels.add(channel);
		}

		return arrayChannels;
	}
}
