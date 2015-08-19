package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by July on 8/1/15.
 */
public class MessageActivity extends Activity implements RecentMessagesAsync.MyListener{

    private static final String NAME = "name";
    private static final String MESSAGE_CONTENT = "message";
    private ArrayAdapter<Message> adapter;
    private ListView listView;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        listView = (ListView) findViewById(R.id.messagesList);
        messages = new ArrayList<>();

        RecentMessagesAsync async = new RecentMessagesAsync(this);
        async.setListener(this);
        async.execute();

        adapter = new ArrayAdapter<Message>(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                messages) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // Must always return just a View.
                View view = super.getView(position, convertView, parent);
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

                Message message = messages.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(message.getName());
                text2.setText(message.getMessageContent() + "  " +currentTimestamp);
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MessageActivity.this, ShowConversationActivity.class);
                String name = ((TwoLineListItem) view).getText1().getText().toString();
                String message = ((TwoLineListItem) view).getText2().getText().toString();

                intent.putExtra(NAME, name)
                        .putExtra(MESSAGE_CONTENT, message);

                startActivity(intent);
            }
        });
    }

    public void onLoadComplete(final List<Message> data)
    {
        Log.d("message", data.size() + "on load complete within message activity");
        messages.clear();
        messages.addAll(data);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}


