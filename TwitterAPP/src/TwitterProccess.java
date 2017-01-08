
import java.sql.SQLException;
import java.util.ArrayList;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.Twitter;

/* 
 * This class is directly related to authenticate Twitter.
 * We use that class for the process such as twitter authentication, configuration, and accessing our list of terms in the database.
 */
public class TwitterProccess {
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private static TwitterProccess twProcess = null;
	protected static DbConnection conn = new DbConnection();
	protected static DbProcess dbprocess = new DbProcess();
	private Twitter twitter = null;
	private ArrayList<String> list;
	
	/* 
	 * Application can access the user account without userid/password combination given.
	 * Authenticate for Twitter by using consumerKey, and consumerSecret, accessTokenKey and accessTokenSecret.
	 */
	private TwitterProccess(String[] args) throws ClassNotFoundException, SQLException{
		if (args.length < 2) {
			System.out.println("Error! There isn't an usuable OAuthConsumerKey/Secret!!!");
			System.exit(0);
		}

		String accessToken = "", accessSecret = "";
		AuthenticationData oauth = new AuthenticationData(args[0], args[1]);

		if (args.length <= 2) { /* There isn't a OAuthConsumerSecret and secret */
			String[] access = oauth.getAccessToken();
			if (access.length < 2) {
				System.out.println("Error! There isn't an usuable OAuthAccessToken/Secret!!!");
				System.exit(0);
			}
			accessToken = access[0];
			accessSecret = access[1];
		} else {
			accessToken = args[2];
			accessSecret = args[3];
		}
		conn.setConn();
		setTwitter(args[0], args[1], accessToken, accessSecret);
	}
	
	/*
	 * Authenticate the user before getting data from Twitter is started.
	 * This authentication operation is called only for once because of using Singleton Pattern.
	 * 
	 * @return TwitterProccess
	 */
	public static TwitterProccess authenticate(String[] args) throws ClassNotFoundException, SQLException{
		if(twProcess == null) twProcess = new TwitterProccess(args);
		return twProcess;
	}

	/* Functions to get variables from other classes. */ 
	public int getListSize() { return (list = dbprocess.getTerm()).size(); }
	public twitter4j.Twitter getTwitter() {  return twitter;  }
	
	/* 
	 * Set configuration data for Twitter.
	 * 
	 * @return Twitter
	 */
	void setTwitter(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) throws ClassNotFoundException, SQLException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);

		twitter = new TwitterFactory(cb.build()).getInstance();
	}
	
	/* 
	 * Create the query to get all terms in database as a string parameter that is used to Query.
	 * @parameters => integer : beginning index of the list of query
	 * 				  integer : ending index of the list of query
	 * @return String
	 */
	public String createQuery(int start, int end) {
		if(start<0 || end>getListSize()){ return ""; }
		
		String query = list.get(0);
		int i;
		for (i = start; i < end; i = i + 2) {
			if (i + 1 != list.size()) {
				query = query + " OR " + list.get(i) + " OR " + list.get(i + 1);
			} else {
				query = query + " OR " + list.get(i);
				break;
			}
		}
		query="("+query+") AND"+" (turkey OR t�rkiye OR turkiye)";
		System.out.println(query);
		return query;
	}
}


/*
public String createQuery1() {
	if((list = conn.getTerm()).size() == 0){
		return "";
	}
	
	String query = list.get(0);
	int i;
	for (i = 1; i < list.size()/3; i = i + 2) {
		if (i + 1 != list.size()) {
			query = query + " OR " + list.get(i) + " OR " + list.get(i + 1);
		} else {
			query = query + " OR " + list.get(i);
			break;
		}
	}
	query="("+query+") AND"+" (turkey OR türkiye OR turkiye)";
	System.out.println(query);
	return query;
}
	
	public String createQuery2() {
		if((list = conn.getTerm()).size() == 0){
			return "";
		}
	String query1 = list.get(list.size()/3);
	int i;
	for (i = list.size()/3+ 1; i < (2*list.size()/3); i = i + 2) {
		if (i + 1 != list.size()) {
			query1 = query1 + " OR " + list.get(i) + " OR " + list.get(i + 1);
		} else {
			query1 = query1 + " OR " + list.get(i);
			break;
		}
	}
	query1="("+query1+") AND"+" (turkey OR türkiye OR turkiye)";
	System.out.println(query1);
	return query1;
}
	
public String createQuery3() {
	if((list = conn.getTerm()).size() == 0){
		return "";
	}
	String query3 = list.get(2*list.size()/3);
	int i;
	for (i = (2*list.size()/3)+1; i < list.size(); i = i + 2) {
		if (i + 1 != list.size()) {
			query3 = query3 + " OR " + list.get(i) + " OR " + list.get(i + 1);
		} else {
			query3 = query3 + " OR " + list.get(i);
			break;
		}
	}
	query3="("+query3+") AND"+" (turkey OR türkiye OR turkiye)";
	System.out.println(query3);
	return query3;
}
*/