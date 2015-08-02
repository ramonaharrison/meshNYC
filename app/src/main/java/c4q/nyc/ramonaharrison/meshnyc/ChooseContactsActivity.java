package c4q.nyc.ramonaharrison.meshnyc;

import android.app.ListActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class ChooseContactsActivity extends ListActivity {


    Button button;
    ListView lv;
    Cursor Cursor1;
    ArrayList<String> phoneNumbers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //create a cursor to query the Contacts on the device to start populating a listview
        Cursor1 = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                null);
        startManagingCursor(Cursor1);
        String[] from = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to = {R.id.display_name,
                R.id.phone_number};
        final SimpleCursorAdapter listadapter = new SimpleCursorAdapter(this, R.layout.contacts_fields, Cursor1, from, to);
        setListAdapter(listadapter);
        lv = getListView();
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Cursor1.moveToPosition(i);
                phoneNumbers = new ArrayList<String>();
                String data = Cursor1.getString(Cursor1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumbers.add(data);
                view.setBackgroundColor(Color.parseColor("#80c1e3"));
                Log.d("data", "" + data);
            }
        });

        /**The following should be a button that when clicked sends a text message to all phone numbers stored in the
        phone numbers array. But gives a null pointer exception when trying to compile

        activity_chosen_contacts_result.xml was made to display the chosen contacts and then confirm that these
        were the ones you wanted to send the text to, this was made along with SendAppInviteActivity
         but has not been implemented yet. Those two are safe to delete if unnecessary.

        activity_choose_contacts.xml is the the listview and the button reside. The fields populated by the simple
        adapter are in  contacts_fields.xml*/


//        button = (Button) findViewById(R.id.sendInviteButton);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                String itemText = data;
//                // fetch the Sms Manager
//                SmsManager sms = SmsManager.getDefault();
//                // the message
//                String messageText = "Hey ";
//                // the phone numbers we want to send to
////                String[] numbers = {itemText};
//
//                for (String contactNumber : phoneNumbers) {
//                    sms.sendTextMessage(contactNumber, null, messageText, null, null);
//                }
//                Toast.makeText(getApplicationContext(), "Invite sent!", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}