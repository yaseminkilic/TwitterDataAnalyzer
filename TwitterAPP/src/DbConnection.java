import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

class DbConnection {
	
	Connection openDb() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Connection");
		return (Connection)DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql","root","12345678");
	}
	
	void createQuery(String sql, Connection con) throws SQLException{
		Statement stmt = (Statement) con.createStatement();
		java.sql.ResultSet rs = stmt.executeQuery(sql);

		while(rs.next()){
			int id  = rs.getInt("id");
			System.out.print("ID: " + id);
		}

		rs.close();
		stmt.close();
	}
	
	void closeDb(Connection con) throws SQLException{
		con.close();
	}
	
}
