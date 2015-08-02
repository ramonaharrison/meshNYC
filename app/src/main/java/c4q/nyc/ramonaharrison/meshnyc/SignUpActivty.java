package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class SignUpActivty extends ActionBarActivity {

    TextView welcomeMessage;
    EditText ZIP_CODE, firstName, lastName;
    public static String userName = "", zipCode = "";
    Button submit;
    Button contacts;
    TextView savedZip, savedUsername;


    public static final String MY_PREFS = "c4q.nyc.ramonaharrison.meshnyc.MyPrefsFile";
    public static final String ZIP_CODE_KEY = "zip_code_key";
    public static final String USERNAME_CODE_KEY = "username_code_key";


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activty);

        //getting zip information (if it exists)
        prefs = getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        zipCode = prefs.getString(ZIP_CODE_KEY, "");
        userName = prefs.getString(USERNAME_CODE_KEY, "");


        //welcome message
        welcomeMessage = (TextView) findViewById(R.id.welcome_message);

        //zip views
        ZIP_CODE = (EditText) findViewById(R.id.zip_code);
        submit = (Button) findViewById(R.id.submit_button);
        contacts = (Button) findViewById(R.id.contact_invite_button);
        savedZip = (TextView) findViewById(R.id.saved_zip_code);


        //username views
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        savedUsername = (TextView) findViewById(R.id.saved_username);


        if (!zipCode.equals("") && !userName.equals("")) {
            savedZip.setText("your zip code is:   " + zipCode);
            savedUsername.setText("your username is:   " + userName.toUpperCase());
        }

        //submitting new zip code/moving on to next screen
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //validating zip length
                if (!ZIP_CODE.getText().toString().equals("") && !firstName.getText().toString().equals("") && !lastName.getText().toString().equals("")) {
                    editor = prefs.edit();
                    zipCode = ZIP_CODE.getText().toString();
                    editor.putString(ZIP_CODE_KEY, zipCode);
                    userName = firstName.getText().toString() + lastName.getText().toString();
                    editor.putString(USERNAME_CODE_KEY, userName);
                    editor.apply();
                    savedZip.setText("your zip code is: " + ZIP_CODE.getText().toString());
                    savedUsername.setText("your username is:   " + userName);
                } else {

                }


                //getting rid of views to add new fields
//                view.setVisibility(View.GONE);
//                ZIP_CODE.setVisibility(View.GONE);
//                savedZip.setVisibility(View.GONE);
//                welcomeMessage.setVisibility(View.GONE);

            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ChooseContactsActivity.class);
                startActivity(intent);
            }
        });

    }
}
