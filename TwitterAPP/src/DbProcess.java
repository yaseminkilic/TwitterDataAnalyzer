import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import twitter4j.Status;

public class DbProcess extends DbConnection{

	private ArrayList<String> list;

	
	/* Find TERM of tweet into TWITTER table */
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

	/*  Find ID of tweet into TWITTER table */
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
	
	boolean insertTweetAndTerm(Status status, DbConnection dbConn, int tweetid) throws ClassNotFoundException{
		int termid;
		try {
			for (int j = 0; j < list.size(); j++) {
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

	/* Insert tweet datas into TWEETS table  */
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
