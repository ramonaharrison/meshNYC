package c4q.nyc.ramonaharrison.meshnyc;

/**
 * Created by Ramona Harrison
 * on 8/1/15.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ClientSocket implements Runnable {


    private Socket socket = null;
    private InputStream iStream;
    private static final String TAG = "SocketListener";

    String mHostAddr;
    int mPort;
    String message;

    public ClientSocket(String hostAddr, int port, String message) {
        this.socket = new Socket();
        this.mHostAddr = hostAddr;
        this.mPort = port;
        this.message = message;
    }

    @Override
    public void run() {

        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(mHostAddr, mPort), 500);

            /**
             * Create a byte stream and pipe it to the output stream
             * of the socket. This data will be retrieved by the server device.
             */
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.getBytes());
            outputStream.close();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                socket.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }
}