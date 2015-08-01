package c4q.nyc.ramonaharrison.meshnyc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;


public class ShowConversationActivity extends ActionBarActivity {

    Button sendButton;
    static EditText messageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_conversation);

        messageContent = (EditText) findViewById(R.id.messageContent);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                Message message = new Message("ID", "SEND", false, "NAME SENT TO HERE", currentTimestamp.toString(), messageContent.getText().toString());
                Log.v("timestamp", currentTimestamp.toString());

                SQLHelper helper = SQLHelper.getInstance(getApplicationContext());
                helper.insertMessageRow(message.getId(), message.getIntention(), message.getIsSent(), message.getName(), message.getTimeStamp(), message.getMessageContent());

            }
        });

    }






}
