import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

public class Main extends Thread{

	private static DbConnection dbConn;

	public static void main(String[] args) {
		String queryString = "";
		ArrayList<String> list;
		
		try {
			dbConn = new DbConnection(args);
			twitter4j.Twitter twitter = dbConn.getTwitter();
			
			if((queryString = dbConn.createQuery()).isEmpty()){
				System.out.println("There is not any term in terms table");
				System.exit(0);
			}
			
			Query query = new Query(queryString);

			QueryResult result = twitter.search(query);
			while (true) {
				query.setCount(100);

				if(result.getRateLimitStatus().getSecondsUntilReset() == 0){
				   try {
					   Thread.sleep(result.getRateLimitStatus().getSecondsUntilReset()*1000);
					   result = twitter.search(query);
				   } catch (InterruptedException e) {
					   // TODO Auto-generated catch block
					   e.printStackTrace();
				   }
				}
				
				Date time;
				String msg;
				int userid, tweetid, termid;
				boolean state = false;
				for (Status status : result.getTweets()) {
					userid = (int) status.getUser().getId();
					tweetid = (int) status.getId();
					msg = status.getText();
					time = new java.sql.Date(status.getCreatedAt().getTime());
					
					state = dbConn.insertTweet(userid, tweetid, msg, time);
					if(!state) continue;
					
					list = dbConn.getTerm();
					System.out.println(query);
					System.out.println("---> " + "userid : " + userid + " tweetid : " + tweetid + " msg : " + msg + " time : " + time);
					for (int j = 0; j < list.size(); j++) {
						if (state && status.getText().toLowerCase().contains(list.get(j).toLowerCase())) {
							termid = dbConn.findId(list.get(j));
							PreparedStatement preparedStatement = dbConn.getConn().prepareStatement("INSERT INTO tweetandterm (tweetid, termid) values (?, ?)");
							preparedStatement.setLong(1, tweetid);
							preparedStatement.setLong(2, termid);
							preparedStatement.executeUpdate();	
						}
					}
				}

				result = twitter.search(query);
			}

		} catch (TwitterException e) {
			//System.out.println("TwitterException : " + e);
		} catch (ClassNotFoundException | SQLException e) {
			//System.out.println("ClassNotFoundException/SQLException : " + e);
		} finally {
			try {
				dbConn.closeDb();
			} catch (ClassNotFoundException | SQLException e) {
				//System.out.println("ClassNotFoundException/SQLException : " + e);
			}
		}
	}
}
