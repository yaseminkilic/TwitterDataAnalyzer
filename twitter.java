import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


public class twitter {
	public static void main(String[] args) throws TwitterException, IOException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("BGSjFBrtkrQ9Tc3t7Bc9N0cqf")
		  .setOAuthConsumerSecret("IBhoaudCoBp6duz6SEYJWNc9GOrDLG4OoUXvKSpCMDjBDHo2AN")
		  .setOAuthAccessToken("1830753932-EfIjo644zQsDS5SVT6j8z3TrY5tKRbU9ii6HxPh")
		  .setOAuthAccessTokenSecret("gP8xSLSvwCF3oJwkAKxN85Ut5XlPyy2CYbJopft20x96r");
		
		

		  Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		  Query query = new Query("ybu");
		  int numberOfTweets = 100000;
		  long lastID = Long.MAX_VALUE;
		  ArrayList<Status> tweets = new ArrayList<Status>();
		  int tweetsize=0;
		  while (tweets.size () < numberOfTweets) {
		    if (numberOfTweets - tweets.size() > 100)
		      query.setCount(100);
		    else 
		      query.setCount(numberOfTweets - tweets.size());
		    try {
		    	
		      QueryResult result = twitter.search(query);
		      tweetsize=tweets.size();
		      tweets.addAll(result.getTweets());
		      if(tweetsize==tweets.size())
		    	  break;
		      System.out.println("Gathered " + tweets.size() + " tweets");
		      for (Status t: tweets) 
		        if(t.getId() < lastID) lastID = t.getId();

		    }

		    catch (TwitterException te) {
		    System.out.println("Couldn't connect: " + te);
		    }; 
		    query.setMaxId(lastID-1);
		  }
		  File file = new File("dosya1.txt");
	        if (!file.exists()) {
	            file.createNewFile();
	        }

	        FileWriter fileWriter = new FileWriter(file, false);
	        BufferedWriter bWriter = new BufferedWriter(fileWriter);
	       // bWriter.close();

		  for (int i = 0; i < tweets.size(); i++) {
		    Status t = (Status) tweets.get(i);

		    GeoLocation loc = t.getGeoLocation();

		    String user = t.getUser().getScreenName();
		    String msg = t.getText();
		    String time = "";
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

	}
}


