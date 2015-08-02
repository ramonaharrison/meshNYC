package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ramona Harrison
 * on 8/1/15.
 */
public class MessageServerAsyncTask extends AsyncTask<Void, Void, String> {

    public AsyncResponse delegate = null;

    private static final String TAG = "MessageServerAsyncTask";


    private Context context;
    private String response;

    private int port;



    public MessageServerAsyncTask(Context context, int port) {
        this.context = context;
        this.port = port;
    }


    @Override
    protected String doInBackground(Void... params) {
        try {


            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(port);


            Socket client = serverSocket.accept();


            /**
             * If this code is reached, a client has connected and transferred data
             *
             */

            InputStream inputStream = client.getInputStream();
            response = convertStreamToString(inputStream);


            serverSocket.close();

            return response;

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return "message error";
        }
    }

    private boolean isHandshake(String response) {
        Pattern p = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
        Matcher m = p.matcher(response);
        return m.find();

    }


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /**
     * Start activity that can handle the text
     */

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {

            if(isHandshake(response)) {
                delegate.receiveHandshake(result);
            } else {
                delegate.setMessageBoard(result);
            }

        }
    }

    public interface AsyncResponse {

        void setMessageBoard(String result);

        void receiveHandshake(String result);
    }


}