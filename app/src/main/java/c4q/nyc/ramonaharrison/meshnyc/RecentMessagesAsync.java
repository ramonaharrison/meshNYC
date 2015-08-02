package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.util.List;

/**
 * Created by July on 8/2/15.
 */
public class RecentMessagesAsync extends AsyncTask<Void, Void, List<Message>> {
    private Context context;
    private MyListener listener;

    public RecentMessagesAsync(Context context) {
        this.context = context;
    }

    //create a listener interface to know when Async is done loading data
    public interface MyListener {
        void onLoadComplete(List<Message> messages);
    }

    public void setListener(MyListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Message> doInBackground(Void[] params) {
        SQLHelper helper = SQLHelper.getInstance(context);
        return helper.getMostRecentMessages();
    }

    @Override
    protected void onPostExecute(List<Message> messages) {
        Log.d("message", messages.size() + " onPostExecute");
        super.onPostExecute(messages);
        if (listener != null) {
            listener.onLoadComplete(messages);
       }
    }
}
