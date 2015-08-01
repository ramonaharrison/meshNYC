//package c4q.nyc.ramonaharrison.meshnyc;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
//public class MessagesList extends ActionBarActivity {
//
//    static ListView messageList;
//    static ArrayAdapter<String> adapter;
//    static TextView messageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_messages_list);
//
//        ArrayList<String> messageList = new ArrayList<>();
//
//        messageList = (ListView) findViewById(R.id.messagesList);
//        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, messageListArray) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView recipientView = (TextView) view.findViewById(android.R.id.text1);
//                recipientView.setTextColor(Color.BLACK);
//                messageView = (TextView) view.findViewById(android.R.id.text2);
//                messageView.setTextColor(Color.DKGRAY);
//
//
//                recipientView.setText("Name");
//                messageView.setText("Message Preview");
//                return view;
//            }
//        };        messageList.setAdapter(adapter);
//
//        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent showMessageIntent = new Intent(getApplicationContext(), ShowConversationActivity.class);
//                startActivity(showMessageIntent);
//            }
//        });
//
//    }
//}
