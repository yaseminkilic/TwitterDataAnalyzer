
class Oauth {

	private String consumerKey;
	private String consumerSecret;
	
	Oauth(String key, String secret){
		consumerKey = key;
		consumerSecret = secret;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	
	void setConsumerKey(String key) {
		consumerKey = key;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	
	void setConsumerSecret(String secret) {
		consumerSecret = secret;
	}
}
