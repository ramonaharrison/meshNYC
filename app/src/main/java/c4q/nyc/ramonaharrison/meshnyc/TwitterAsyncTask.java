//package c4q.nyc.ramonaharrison.meshnyc;
//
///**
// * Created by Hoshiko on 8/2/15.
// */
//import android.app.ListActivity;
//import android.text.method.LinkMovementMethod;
//import android.widget.ArrayAdapter;
//import android.os.AsyncTask;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.javapapers.social.twitter.TwitterAPI;
//import com.javapapers.social.twitter.TwitterTweet;
//
//import java.util.ArrayList;
//
//public class TwitterAsyncTask extends AsyncTask<Object, Void, ArrayList<TwitterTweet>> {
//    ListActivity callerActivity;
//
//    final static String TWITTER_API_KEY = "NEAeRWtJJx42UJJHqAqJPmXmo";
//    final static String TWITTER_API_SECRET = "dVumvk0WltZnSqrWmJNMOFseS99lgsaY5AsCNSSJ0X5G6xfGFz";
//
//    @Override
//    protected ArrayList<TwitterTweet> doInBackground(Object... params) {
//        ArrayList<TwitterTweet> twitterTweets = null;
//        callerActivity = (ListActivity) params[1];
//        if (params.length > 0) {
//            TwitterAPI twitterAPI = new TwitterAPI(TWITTER_API_KEY,TWITTER_API_SECRET);
//            twitterTweets = twitterAPI.getTwitterTweets(params[0].toString());
//        }
//        return twitterTweets;
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<TwitterTweet> twitterTweets) {
//        ArrayAdapter<TwitterTweet> adapter =
//                new ArrayAdapter<TwitterTweet>(callerActivity, R.layout.twitter_tweets_list,
//                        R.id.listTextView, twitterTweets);
//        callerActivity.setListAdapter(adapter);
//        ListView lv = callerActivity.getListView();
//        lv.setDividerHeight(0);
//        //lv.setDivider(this.getResources().getDrawable(android.R.color.transparent));
//        lv.setBackgroundColor(callerActivity.getResources().getColor(R.color.Twitter_blue));
//    }
//}