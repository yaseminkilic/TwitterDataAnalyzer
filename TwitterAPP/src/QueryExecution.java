
import java.sql.Date;
import java.sql.SQLException;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

/* 
 * This class is directly related to search the data from Twitter, and insert them into database.
 */
public class QueryExecution extends ApplicationExecution {
	
	/* Instance to create only one execution for searching operation. */ 
	private QueryExecution instance = null;
	
	QueryExecution() {
		execute();
	}
	
	public QueryExecution getInstance(){
		if(instance == null) instance = new QueryExecution();
		return instance;
	}
	
	/* 
	 * Create query to search Twitter' data.
	 * Get the result by using getTweets() function as a QueryResult.
	 * Insert them into some tables in the database.
	 */
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
						tweetid = insertOriginalTweets(status);
						if(tweetid == 0) continue; 
						dbprocess.insertTweetAndTerm(status, dbConn, tweetid);
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
	
	/*
	 * Insert twitter data into  OriginalTweets table.
	 */
	private int insertOriginalTweets(Status status) throws ClassNotFoundException, SQLException{
		int userid = (int) status.getUser().getId();
		int tweetid = (int) status.getId();
		String msg = status.getText();
		Date time = new java.sql.Date(status.getCreatedAt().getTime());
		boolean state = dbprocess.insertTweet("OriginalTweets", userid, tweetid, msg, time);
		if(!state) return 0;
		
		this.list = dbprocess.getTerm();
		System.out.println("---> " + "userid : " + userid + " tweetid : " + tweetid + " msg : " + msg + " time : " + time);
		return tweetid;
	}
}
