import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

class DbConnection {

	private Connection conn = null;
	private twitter4j.Twitter twitter = null;
	private ArrayList<String> list;

	/* 
	 * Open Connection
	 * Set Twitter keys and secrets to authenticate with Twitter 
	 */
	DbConnection(String[] args) throws ClassNotFoundException, SQLException {
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

		setConn();
		setTwitter(args[0], args[1], accessToken, accessSecret);
	}
	
	/* 
	 * Close database connection 
	 */
	void closeDb() throws SQLException, ClassNotFoundException {
		if (conn != null) {
			conn.close();
		}
	}
	

	public String createQuery() {
		if((list = getTerm()).size() == 0){
			return "";
		}
		
		String query = list.get(0);
		int i;
		for (i = 1; i < (list.size() > 10 ? 10 : list.size()); i = i + 2) {
			if (i + 1 != list.size()) {
				query = query + " OR " + list.get(i) + " OR " + list.get(i + 1);
			} else {
				query = query + " OR " + list.get(i);
				break;
			}
		}
		System.out.println(query);
		return query;
	}
	
	/*
	 * Get List of Terms
	 */
	public ArrayList<String> getList() {
		return list;
	}
	
	/* 
	 * Get - Set Connection 
	 */
	public Connection getConn() { return conn; }
	void setConn() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetdata","root","12345678");
	}
	
	/* 
	 * Get - Set Twitter 
	 */
	public twitter4j.Twitter getTwitter() { return twitter; }
	void setTwitter(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) 
			throws ClassNotFoundException, SQLException {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);

		twitter = new TwitterFactory(cb.build()).getInstance();
	}

	
	/* 
	 * Find TERM of tweet into TWITTER table 
	 */
	ArrayList<String> getTerm() {
		list = new ArrayList<String>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT term FROM Terms");
			
			while (rs.next()) {
				list.add(rs.getString("term"));
			}
		} catch (SQLException e) {
			//System.err.println("Hata in getTerm ! " + e.getMessage());
		}
		
		return list;
	}
	
	/* 
	 * Find ID of tweet into TWITTER table 
	 */
	int findId(String term) throws ClassNotFoundException, SQLException {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id, term FROM Terms");

			while (rs.next()) {
				if (rs.getString("term").equals(term)) {
					return rs.getInt("id");
				}
			}
		} catch (SQLException e) {
			//System.err.println("Hata in findId ! " + e.getMessage());
		}

		return 0;
	}
	
	/* 
	 * Insert tweet datas into TWEETS table 
	 */
	boolean insertTweet(int userid, int tweetid, String text, Date date) throws ClassNotFoundException, SQLException {
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(
					"INSERT INTO OriginalTweets (userid, tweetid, text, date) values (?, ?, ?, ?)");
			preparedStatement.setLong(1, userid);
			preparedStatement.setLong(2, tweetid);
			preparedStatement.setString(3, text);
			preparedStatement.setDate(4, (java.sql.Date) date);
			return preparedStatement.executeUpdate() > 0 ? true : false;

		} catch (Exception e) {
			//System.err.println("Hata in insertTweet ! " + e.getMessage());
		}
		
		return false;
	}
}
