package c4q.nyc.ramonaharrison.meshnyc;

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;


public class ChooseContactsActivity extends ListActivity {


        @Override
        public long getSelectedItemId() {
            // TODO Auto-generated method stub
            return super.getSelectedItemId();
        }

        @Override
        public int getSelectedItemPosition() {
            // TODO Auto-generated method stub
            return super.getSelectedItemPosition();
        }
        ListView lv;
        Cursor Cursor1;

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
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone._ID}; // get the list items for the listadapter could be TITLE or URI

            int[] to = {R.id.display_name,
                    R.id.phone_number}; //sets the items from above string to listview

            //new listadapter, created to use android checked template
            SimpleCursorAdapter listadapter = new SimpleCursorAdapter(this, R.layout.contacts_fields, Cursor1, from, to );
            setListAdapter(listadapter);

            //adds listview so I can get data from it
            lv = getListView();
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        }
    }