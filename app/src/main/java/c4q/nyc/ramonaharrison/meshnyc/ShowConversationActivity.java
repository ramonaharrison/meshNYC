package c4q.nyc.ramonaharrison.meshnyc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class ShowConversationActivity extends ActionBarActivity {

    ConversationAdapter adapter;
    ArrayList<Message> conversationArray;
    ListView messageList;
    Button sendButton;
    static EditText messageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_conversation);

        conversationArray = new ArrayList<>();
        messageList = (ListView) findViewById(R.id.messageList);
        adapter = new ConversationAdapter(getApplicationContext(), conversationArray);
        messageList.setAdapter(adapter);


        messageContent = (EditText) findViewById(R.id.messageContent);
        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                // TODO: FILL IN WITH THE PROPER PARAMETERS
                Message message = new Message("ID", "SEND", 0, "NAME SENT TO HERE", currentTimestamp.toString(), messageContent.getText().toString());
                conversationArray.add(message);

                SQLHelper helper = SQLHelper.getInstance(getApplicationContext());
                helper.insertMessageRow(message.getId(), message.getIntention(), message.getIsSent(), message.getName(), message.getTimeStamp(), message.getMessageContent());
                messageContent.setText("");

            }
        });



    }






}
