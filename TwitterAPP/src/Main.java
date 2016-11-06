
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.mysql.jdbc.Connection;

public class Main {
	
	private static String accessToken;
	private static String accessSecret;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		DbConnection db = new DbConnection();
		
		try {
			
			connection = db.openDb();
			//db.createQuery("Select * from tweets", connection);
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			if(args.length < 2){ System.out.println("Error! There isn't an usuable OAuthConsumerKey/Secret!!!"); System.exit(0); }
			AuthenticationData oauth = new AuthenticationData(args[0], args[1]);
			
			// There isn't a OAuthConsumerSecret and secret
			if(args.length <= 2){
				String[] access = oauth.getAccessToken();
				if(access.length < 2){ System.out.println("Error! There isn't an usuable OAuthAccessToken/Secret!!!"); System.exit(0); }
				
				accessToken = access[0];
				accessSecret = access[1];
			}
			else{
				accessToken = args[2];
				accessSecret = args[3];
			}
			
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey(args[0])
			  .setOAuthConsumerSecret(args[1])
			  .setOAuthAccessToken(accessToken)
			  .setOAuthAccessTokenSecret(accessSecret);
			

			long lastID = Long.MAX_VALUE;
			int numberOfTweets = 100000, tweetsize=0;
			ArrayList<Status> tweets = new ArrayList<Status>();
			
			twitter4j.Twitter twitter = new TwitterFactory(cb.build()).getInstance();
			Query query = new Query("cyberSecurity");
			
			while (tweets.size () < numberOfTweets) {
				
			    if (numberOfTweets - tweets.size() > 100)
			    	query.setCount(100);
			    else 
			    	query.setCount(numberOfTweets - tweets.size());
			    
			    QueryResult result = twitter.search(query);
			    tweetsize = tweets.size();
			    tweets.addAll(result.getTweets());
			    
			    if(tweetsize == tweets.size())
			    	break;
			    
			    System.out.println("Gathered " + tweets.size() + " tweets");
			    for (Status t: tweets) 
			    	if(t.getId() < lastID) lastID = t.getId();

			    query.setMaxId(lastID-1);
			  }
			
			  File file = new File("dosya1.txt");
			  if (!file.exists()) {
				  file.createNewFile();
			  }
	
			  FileWriter fileWriter = new FileWriter(file, false);
			  BufferedWriter bWriter = new BufferedWriter(fileWriter);
			  
			  String user, msg, time;
			  for (int i = 0; i < tweets.size(); i++) {
				  Status t = (Status) tweets.get(i);
				  GeoLocation loc = t.getGeoLocation();

				  user = t.getUser().getScreenName();
				  msg = t.getText();
				  time = "";
				  
				  if (loc!=null) {
					  Double lat = t.getGeoLocation().getLatitude();
					  Double lon = t.getGeoLocation().getLongitude();
					  System.out.println(i + " USER: " + user + " wrote: " + msg + " located at " + lat + ", " + lon);

					  bWriter.write(i + " USER: " + user + " wrote: " + msg + " located at " + lat + ", " + lon);
					  bWriter.write(" ");

				  } 
				  else {
					  System.out.println(i + " USER: " + user + " wrote: " + msg);
					  bWriter.write(i + " USER: " + user + " wrote: " + msg);
					  bWriter.write(" ");
				  }
			  }
		} catch (TwitterException | IOException te) {
	    	System.out.println("Couldn't connect: " + te);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				db.closeDb(connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
