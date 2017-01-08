import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import twitter4j.Status;

/* 
 * This class is directly related to create the query for accessing database' data.
 * We use that class for the process such as getting list of terms, finding term' id and inserting any Twitter data for our tables, 
 */
public class DbProcess extends DbConnection{
	
	/* Some useful variables to control class' operations and interaction with other classes. */ 
	private ArrayList<String> list;

	
	/* 
	 * Get all terms from Terms table 
	 * 
	 * @return ArrayList<String>
	 */
	ArrayList<String> getTerm() {
		list = new ArrayList<String>();
		try {
			Statement st = getConn().createStatement();
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
	 * Find tweetID from Terms table
	 * @parameters  String:term
	 * 
	 * @return integer
	 */
	int findId(String term) throws ClassNotFoundException, SQLException {
		try {
			Statement st = getConn().createStatement();
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
	 * Insert tweetId and termID into Tweetandterm table if its match one of our terms.
	 * @parameters  status:Status, dbConn:DbConnection, tweetid:integer
	 * 
	 * @return boolean
	 */
	boolean insertTweetAndTerm(Status status, DbConnection dbConn, int tweetid) throws ClassNotFoundException{
		int termid;
		try {
			for (int j = 0; j < getTerm().size(); j++) {
				if (status.getText().toLowerCase().contains(list.get(j).toLowerCase())) {
						termid = findId(list.get(j));
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

	/*  
	 * Insert tweet into table that is given as a parameter
	 * @parameters  table:String, userid:integer, tweetid:integer, text:String, date:Date
	 * 
	 * @return boolean
	 */
	boolean insertTweet(String table, int userid, int tweetid, String text, Date date)
			throws ClassNotFoundException, SQLException {
		try {
			PreparedStatement preparedStatement = getConn()
					.prepareStatement("INSERT INTO " + table + " (userid, tweetid, text, date) values (?, ?, ?, ?)");
			preparedStatement.setLong(1, userid);
			preparedStatement.setLong(2, tweetid);
			preparedStatement.setString(3, text);
			preparedStatement.setDate(4, (java.sql.Date) date);
			return preparedStatement.executeUpdate() > 0 ? true : false;

		} catch (Exception e) {
			// System.err.println("Hata in insertTweet ! " + e.getMessage());
		}

		return false;
	}
}
