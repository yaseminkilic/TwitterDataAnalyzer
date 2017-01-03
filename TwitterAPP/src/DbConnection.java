
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
}