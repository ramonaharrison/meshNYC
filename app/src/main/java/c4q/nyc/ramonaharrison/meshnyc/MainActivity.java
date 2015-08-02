package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
