package vn.techplus.filmon.configuration;


public class AppConfig {
	//public static final String KEY_MY_CHANNELS = "myChannels";
	
	private static boolean hiddenAds = false;
	private static boolean expiredApp = false;
	
	public static void checkServer() {
		
		/*JSONParser parser = new JSONParser();
		JSONObject jObj = parser.getJSONFromUrl("http://devzone.techplus.com.vn/appconfig/fulliptv_config.json");
		if (jObj == null)
			return;
		
		hiddenAds = jObj.optBoolean("HideAds", true);
		expiredApp = jObj.optBoolean("Expired", false);*/
	}
	
	public static boolean isHiddenAds() {
		return hiddenAds;
	}

	public static boolean isExpiredApp() {
		return expiredApp;
	}
}
