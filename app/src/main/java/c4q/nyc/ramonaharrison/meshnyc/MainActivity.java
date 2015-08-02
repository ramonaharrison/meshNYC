package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button bt = (Button)findViewById(R.id.mapbutton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, MapActivity.class);
                MainActivity.this.startActivity(mapIntent);
            }
        });


        TextView map = (TextView) findViewById(R.id.map);
        TextView settings = (TextView) findViewById(R.id.settings);


        //parses JSON and stores all shelters in SQLite
        if (!noNetwork()) {
            ShelterAsync sa = new ShelterAsync(this);
            sa.execute();
        }

//        //for Hoshiko to check that table shelters is created once and get updated
//        SQLHelper helper =SQLHelper.getInstance(this);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        long numRows = DatabaseUtils.queryNumEntries(db, "shelters");
//        Log.i("yuliya", "" + numRows);


        //CHECKS HOW MANY MSGS ARE IN SQLITE TO CONFIRM THEY ARE STORING SUCCESSFULLY
//        long numMsgRows = DatabaseUtils.queryNumEntries(db, "messages");
//        Log.i("alvin", "" + numMsgRows);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //method to check Internet connection
    private boolean noNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return true;
        } else
            return false;
    }

}
