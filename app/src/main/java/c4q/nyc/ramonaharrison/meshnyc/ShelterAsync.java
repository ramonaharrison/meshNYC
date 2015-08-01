package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by July on 8/1/15.
 */
public class ShelterAsync extends AsyncTask<String, Void, Void> {

    private static final String API_DATA = "https://searchbertha-hrd.appspot.com/_ah/api/search/v1/zipcodes/10101/programs?api_key=3007cb21281f817773bd7a1aff9adb75&serviceTag=emergency%20shelter";
    private ArrayList<Shelter> shelters;
    private Context context;

    public ShelterAsync(Context context) {
        this.context = context;
    }

//        //create a listener interface to know when jobAsync is done loading data
//        public interface MyListener {
//            void onLoadComplete(List<JobPosition> jobs);
//        }
//
//        private MyListener listener;
//
//        public void setListener(MyListener listener) {
//            this.listener = listener;
//        }

    @Override
    protected Void doInBackground(String... params) {
        String result = null;
        try {
            result = openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            parseJSON(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



//        @Override
//        protected void onPostExecute(ArrayList<JobPosition> jobPositions) {
//            super.onPostExecute(jobPositions);
//            if (listener != null) {
//                listener.onLoadComplete(jobPositions);
//            }
//        }

    //method to parse JSON
    public void parseJSON(String rawString) throws JSONException {
        JSONObject obj = new JSONObject(rawString);
        JSONArray main = obj.getJSONArray("programs");
        int num = main.length();
        Log.i("yuliya", main.length() + "" );

        for (int i = 0; i < num; i++) {
            JSONObject program = main.getJSONObject(i);
            JSONArray nextStep = program.getJSONArray("offices");
            Log.i("yuliya", i + "" );


//            for (int k = 0; k < nextStep.length(); k++) {
//                JSONObject info = nextStep.getJSONObject(k);
//                JSONObject location = info.getJSONObject("location");
//                double latitude = location.getLong("latitude");
//                double longitude = location.getLong("longitude");
//                String city = info.getString("city");
//                String address = info.getString("address1");
//                String postal = info.getString("postal");
//                Shelter shelter = new Shelter(city, address, latitude, longitude, postal);
//                Log.i("yuliya", city + " " + address + " " + postal + " " + latitude + " " + longitude);
//            }
        }
    }

    public String openConnection() throws IOException {
        String line;
        URL urlString = new URL(API_DATA);
        HttpsURLConnection connection = (HttpsURLConnection) urlString.openConnection();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
        String resultString = stringBuilder.toString();
        return resultString;
    }
}
