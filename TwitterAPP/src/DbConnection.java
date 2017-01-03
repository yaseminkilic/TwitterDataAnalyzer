
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import twitter4j.Status;

class DbConnection {
	
	private static Connection conn = null;
	private ArrayList<String> list = null;

	DbConnection(){}
	
	/* 
	 * Close database connection 
	 */
	void closeDb() throws SQLException, ClassNotFoundException {
		if (conn != null) {
			conn.close();
		}
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
		if(conn == null) {
			Class.forName("com.mysql.jdbc.Driver");
			conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tweetdata?useUnicode=true","root","12345678");
		}
	}
	
	/* 
	 * Find TERM of tweet into TWITTER table 
	 */
	ArrayList<String> getTerm() {
		list = new ArrayList<String>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT term FROM terms");
			
			while (rs.next()) {
				list.add(rs.getString("term"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	/* 
	 * Find ID of tweet into TWITTER table 
	 */
	int findId(String term) {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id, term FROM terms");

			while (rs.next()) {
				if (rs.getString("term").equals(term)) {
					return rs.getInt("id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	/* 
	 * Insert tweet datas into TWEETS table 
	 */
	boolean insertTweet(String table, int userid, int tweetid, String text, Date date) {
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(
					"INSERT INTO " + table +" (userid, tweetid, text, date) values (?, ?, ?, ?)");
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
	
	boolean insertTweetAndTerm(Status status, DbConnection dbConn, int tweetid){
		int termid;
		try {
			for (int j = 0; j < list.size(); j++) {
				if (status.getText().toLowerCase().contains(list.get(j).toLowerCase())) {
						termid = dbConn.findId(list.get(j));
						PreparedStatement preparedStatement = dbConn.getConn().prepareStatement("INSERT INTO tweetandterm (tweetid, termid) values (?, ?)");
						preparedStatement.setLong(1, tweetid);
						preparedStatement.setLong(2, termid);
						return preparedStatement.executeUpdate() > 0 ? true : false;	
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}