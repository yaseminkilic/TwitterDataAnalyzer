
class Oauth {

	private static String consumerKey ;
	private static String consumerSecret ;
	
	Oauth(String key, String secret){
		consumerKey = key;
		consumerSecret = secret;
	}
	
	
	public static String getConsumerKey() {
		return consumerKey;
	}
	
	public static void setConsumerKey(String consumerKey) {
		Oauth.consumerKey = consumerKey;
	}
	
	public static String getConsumerSecret() {
		return consumerSecret;
	}
	
	public static void setConsumerSecret(String consumerSecret) {
		Oauth.consumerSecret = consumerSecret;
	}
	
}
