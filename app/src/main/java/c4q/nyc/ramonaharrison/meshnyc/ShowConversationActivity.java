package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.Calendar;
import java.util.List;


public class ShowConversationActivity extends ActionBarActivity {

    Button sendButton;
    EditText messageContent;
    RecentMessagesAsync async;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_conversation);

        //get name of the person + message content when item on the list is clicked
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String message = intent.getStringExtra("message");
        Log.d("message", name + " " + message);

        messageContent = (EditText) findViewById(R.id.messageContent);
        TextView messageTV =  (TextView) findViewById(R.id.message);
        TextView receiver = (TextView) findViewById(R.id.receiver);

        messageTV.setText(message);
        receiver.setText(name);

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                Message message = new Message("SEND", 0, "NAME SENT TO HERE", currentTimestamp.toString(), messageContent.getText().toString());

                SQLHelper helper = SQLHelper.getInstance(getApplicationContext());
                helper.insertMessageRow(message.getIntention(), message.getIsSent(), message.getName(), message.getTimeStamp(), message.getMessageContent());

                messageContent.setText("");

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);

    }
}
