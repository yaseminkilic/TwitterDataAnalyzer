import java.sql.Date;
import java.sql.SQLException;

import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;


public class QueryExecution extends Main{
	
	private QueryExecution instance = null;
	QueryExecution() {
		execute();
	}
	
	public QueryExecution getInstance(){
		if(instance == null) instance = new QueryExecution();
		return instance;
	}
	
	private void execute(){
		QueryResult[] results = new QueryResult[3];
		int tweetid = 0;
		try{
			while (true) {
				results[0] = twitter.search(query[0]);
				query[0].setCount(100);
				/*results[1] = twitter.search(query[1]);
				query[1].setCount(100);
				results[2] = twitter.search(query[2]);
				query[2].setCount(100);*/
				
				for(int i=0; i<1/*results.length*/; i++){
					for (Status status : results[i].getTweets()) {
						tweetid = getTweets(status);
						dbConn.insertTweetAndTerm(status, dbConn, tweetid);
					}
					results[i] = twitter.search(query[i]);
				}
			}
		} catch(TwitterException e) {
			System.out.println("Try to Connect for Twitter : " + e.getMessage());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConn.closeDb();
			} catch (ClassNotFoundException | SQLException e) {
				//System.out.println("ClassNotFoundException/SQLException : " + e.getMessage());
			}
		}
	}
	
	private int getTweets(Status status) throws ClassNotFoundException, SQLException{
		int userid = (int) status.getUser().getId();
		int tweetid = (int) status.getId();
		String msg = status.getText();
		Date time = new java.sql.Date(status.getCreatedAt().getTime());
		boolean state = dbConn.insertTweet("OriginalTweets", userid, tweetid, msg, time);
		if(!state) return 0;
		
		list = dbConn.getTerm();
		System.out.println("---> " + "userid : " + userid + " tweetid : " + tweetid + " msg : " + msg + " time : " + time);
		return tweetid;
	}
}
