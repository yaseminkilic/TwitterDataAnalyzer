
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("58ima3mcngzPCv0KnGwCVPllc")
				.setOAuthConsumerSecret("NVnV0RCstWEiRxGq5VcQloHEfgEpxsiSNcD8KFGrjcV4hQRyXA")
				.setOAuthAccessToken("1830753932-3JaV7m18EEpkfFbabLSXhmNkjtueVvzzdclFEse")
				.setOAuthAccessTokenSecret("NUIcsvmBisuuEe26AWzCsRKKC2h5v7oRTek1paNdGyXfk");
		TwitterFactory tf = new TwitterFactory(cb.build());

		// The factory instance is re-useable and thread safe.
		twitter4j.Twitter twitter = tf.getInstance();

		int pageno = 1;
		String user = "google";
		List statuses = new ArrayList();

		while (true) {

			try {

				int size = statuses.size();
				Paging page = new Paging(pageno++, 100);
				statuses.addAll(twitter.getUserTimeline(user, page));
				if (statuses.size() == size)
					break;
			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
		List<Twitter> tweet = new ArrayList();

		for (int j = 0; j < statuses.size(); j++) {
			Status st = (Status) statuses.get(j);
			String[] a=st.getText().split("http");
			String[] b=a[0].split(" ",2);
			tweet.add(new Twitter(st.getId(), st.getInReplyToScreenName(), b[1], st.getCreatedAt()));

		}

		for (Twitter t : tweet) {
			System.out.println("*** " + t.id + "\n\t--- " + t.name + "\n\t--- " + t.text + "\n\t--- " + t.date);
		}
		
		System.out.println("Total: " + statuses.size());
	}

}
