/* 
 * We use Twitter4J that is an unofficial Java library for the Twitter API.
 * With Twitter4J, we can easily integrate our Java application with the Twitter service.
 */
import java.util.ArrayList;
import twitter4j.Query;
import twitter4j.Twitter;

public class Main extends Thread{

	/* 
	 * This class is directly related to authenticate Twitter.
	 * We use that class for the process such as twitter authentication, configuration, and accessing our list of terms in the database.
	 */
	protected static TwitterProccess twProcess = null;
	
	/* 
	 * This class is directly related to connect Twitter.
	 * We use that class for the process such as closing database, accessing twitter connection and getting the list of terms in the database.
	 */
	protected static DbConnection dbConn = null;
	
	/* 
	 * This class is directly related to create the query for accessing database' data.
	 * We use that class for the process such as getting list of terms, finding term' id and inserting any Twitter data for our tables, 
	 */
	protected static DbProcess dbprocess = new DbProcess();
	
	/* 
	 * This variable holds our terms in the database.
	 */
	protected static ArrayList<String> list = null;
	
	/*
	 * To search for Tweets by using Query class and Twitter.search(twitter4j.Query) method.
	 */
	protected static Query query[] = new Query[3];
	
	/* 
	 * twitter4j.Twitter interface is that has some available functions such as searching, posting, getting a Tweet, OAuth etc. 
	 */
	protected static Twitter twitter = null;				
	
	public static void main(String[] args) {
		String queryString1 = "", queryString2 = "", queryString3 = "";
		int listSize = 0;
		try {
			
			/* 
			 * Authenticate Twitter to access its data by using the information of consumer and accessToken as a arg parameter. 
			 * Set the Configuration for twitter by using these arg parameters
			 */
			twProcess = TwitterProccess.authenticate(args);

			/* Get some details such as connection, twitter object and our term' list */
			dbConn = TwitterProccess.conn;
			twitter = twProcess.getTwitter();
			listSize = twProcess.getListSize();
			
			/* Create the query by using terms list, then Check it weather it's empty. */
			if((queryString1 = twProcess.createQuery(0, 10)).isEmpty() /*||(queryString2 = twProcess.createQuery(listSize/3+1, listSize*2/3)).isEmpty() || (queryString3 = twProcess.createQuery(listSize*2/3+1, listSize)).isEmpty()*/){
				System.out.println("There is not any term in terms table");
				System.exit(0);
			}
			
			/* 
			 * Add the query for our Query arrays.
			 * Execute the searching.
			 */
			query[0] = new Query(queryString1); /*query[1] = new Query(queryString2); query[2] = new Query(queryString3);*/
			QueryExecution qExecute = new QueryExecution();
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("HATA !!! Exception : " + e.getMessage());
		}
	}
}
