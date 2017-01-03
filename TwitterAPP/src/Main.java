
import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.Twitter;

public class Main extends Thread{

	protected static DbConnection dbConn = null;
	protected static TwitterProccess twProcess = null;
	protected static ArrayList<String> list = null;
	protected static Query query[] = new Query[3];
	protected static Twitter twitter = null;
	
	public static void main(String[] args) {
		String queryString1 = "", queryString2 = "", queryString3 = "";
		int listSize = 0;
		try {
			twProcess = TwitterProccess.authenticate(args);
			dbConn = TwitterProccess.conn;
			twitter = twProcess.getTwitter();
			listSize = twProcess.getList();
			
			if((queryString1 = twProcess.createQuery(0, 10)).isEmpty() 
/*||(queryString2 = twProcess.createQuery(listSize/3+1, listSize*2/3)).isEmpty() 
|| (queryString3 = twProcess.createQuery(listSize*2/3+1, listSize)).isEmpty()*/
					){
				System.out.println("There is not any term in terms table");
				System.exit(0);
			}
			
			query[0] = new Query(queryString1);
			//query[1] = new Query(queryString2);
			//query[2] = new Query(queryString3);
			QueryExecution qExecute = new QueryExecution();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("HATA !!! Exception : " + e.getMessage());
		}
	}
}