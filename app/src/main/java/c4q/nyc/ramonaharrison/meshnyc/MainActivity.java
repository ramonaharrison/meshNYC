package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView map = (TextView) findViewById(R.id.map);
        TextView settings = (TextView) findViewById(R.id.settings);


        //parses JSON and stores all shelters in SQLite
        if (!noNetwork()) {
            ShelterAsync sa = new ShelterAsync(this);
            sa.execute();
        }

        //for Hoshiko to check that table shelters is created once and get updated
//        SQLHelper helper =SQLHelper.getInstance(this);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        long numRows = DatabaseUtils.queryNumEntries(db, "shelters");
//        Log.i("yuliya", "" + numRows);


        //CHECKS HOW MANY MSGS ARE IN SQLITE TO CONFIRM THEY ARE STORING SUCCESSFULLY
//        long numMsgRows = DatabaseUtils.queryNumEntries(db, "messages");
//        Log.i("alvin", "" + numMsgRows);




        Button messagesButton = (Button) findViewById(R.id.messages);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(getApplicationContext(), MessageActivity.class);
                startActivity(messageIntent);
            }
        });
        Button mapButton = (Button) findViewById(R.id.map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapIntent);
            }
        });
        Button settingsButton = (Button) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(getApplicationContext(), PeerActivity.class);
                startActivity(settingsIntent);
            }
        });

        Button signupButton = (Button) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), SignUpActivty.class);
                startActivity(signupIntent);
            }
        });

        Button button = (Button) findViewById(R.id.button2);
        Button twitterButton = (Button) findViewById(R.id.twitter);

        if (!noNetwork()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent alertIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.org/publicalerts"));
                    startActivity(alertIntent);
                }
            });

            twitterButton.setOnClickListener(new View.OnClickListener() {
                Intent intent = null;

                @Override
                public void onClick(View v) {
                    try {
                        // get the Twitter app if possible
                        getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=226631680"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/nycoem"));
                    }
                    startActivity(intent);

                }
            });
        }else {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Sorry no internet connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
