package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by July on 8/1/15.
 */
public class MessageActivity extends Activity {
    TextView messages;
    TextView map;
    TextView settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);

        messages = (TextView) findViewById(R.id.messages);
        map = (TextView) findViewById(R.id.map);
        settings = (TextView) findViewById(R.id.settings);
    }
}
