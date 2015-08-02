package c4q.nyc.ramonaharrison.meshnyc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PeerActivity extends ActionBarActivity implements ChannelListener, ConnectionInfoListener, PeerListListener, MessageServerAsyncTask.AsyncResponse {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private Button findPeers;
    private ListView peersList;
    private List<WifiP2pDevice> peers;
    private WiFiPeerListAdapter peerAdapter;
    private ProgressBar progressBar;
    private TextView progressText;
    private MessageServerAsyncTask messageServerAsyncTask;
    private TextView messageBoard;
    private EditText messageBox;
    private GroupNode thisNode;
    private GroupNode connectedNode;
    private boolean isConnected;
    private boolean isGroupOwner;


    final static String TAG = "PeerActivity";

    final private String SEARCHING_FOR_PEERS = "Searching for peers...";
    final private String CONNECTED_TO_PEER = "Connected to peer.";
    final private String NO_PEERS = "No peers available.";
    final private String SOMETHING_WENT_WRONG = "Something went wrong.";
    final private String HANDSHAKE_RECIEVED = "Handshake received.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer);
        peers = new ArrayList<>();
        setConnected(false);

        initializeUI();
        initializeWifiP2p();
        initializeIntentFilter();

    }

    private void initializeUI() {
//        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        messageBoard = (TextView) findViewById(R.id.message_board);
        messageBox = (EditText) findViewById(R.id.message_edit_text);
        progressText = (TextView) findViewById(R.id.progress_textview);
        findPeers = (Button) findViewById(R.id.find_peers);
        peersList = (ListView) findViewById(R.id.peer_list_view);
        peerAdapter = new WiFiPeerListAdapter(
                this,
                android.R.layout.simple_list_item_1,
                peers);

        peersList.setAdapter(peerAdapter);

        findPeers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressText.setText(SEARCHING_FOR_PEERS);
                discoverPeers();
            }
        });

        peersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                if (!isConnected) {
                    connectToPeer(position);
                } else {
                    String message = messageBox.getText().toString();
                    thisNode.sendMessage(connectedNode, '"' + " " + message  + " " + '"');
                    startServer(isGroupOwner);
                }
            }
        });


    }



    public void setMessageBoard(String response) {
        messageBoard.setText('"' + " " + response + " " + '"');
        startServer(isGroupOwner);
    }

    @Override
    public void receiveHandshake(String result) {
        connectedNode = new GroupMember(result, ((GroupLeader)thisNode).getGroupLeaderAddress());
        ((GroupLeader)thisNode).addGroupMember((GroupMember)connectedNode);
        Log.d(TAG, HANDSHAKE_RECIEVED + result);
        Toast.makeText(getApplicationContext(), HANDSHAKE_RECIEVED, Toast.LENGTH_SHORT).show();
        startServer(isGroupOwner);
    }

    private void discoverPeers() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(getApplicationContext(), SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                progressText.setText(NO_PEERS);
            }

        });
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        peers.clear();
        peers.addAll(peerList.getDeviceList());
        peerAdapter.notifyDataSetChanged();


        if (peers.size() == 0) {
            progressText.setText(NO_PEERS);
            Toast.makeText(getBaseContext(), NO_PEERS, Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressText.setVisibility(View.INVISIBLE);
        }
    }

    private void connectToPeer(final int position) {
        //obtain a peer from the WifiP2pDeviceList

        final WifiP2pDevice device = peers.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
                setConnected(true);
                Toast.makeText(getApplicationContext(), CONNECTED_TO_PEER, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                //failure logic
                Toast.makeText(getApplicationContext(), SOMETHING_WENT_WRONG + reasonCode, Toast.LENGTH_SHORT).show();

            }

        });
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {

        // Start the async server socket
        if (info.groupFormed) {

            // Delegate leader or members
            if (info.isGroupOwner) {
                isGroupOwner = true;
                thisNode = new GroupLeader(info.groupOwnerAddress.getHostAddress());
                startServer(isGroupOwner);
            } else {
                isGroupOwner = false;
                thisNode = new GroupMember(info.groupOwnerAddress.getHostAddress());
                ((GroupMember)thisNode).sendHandshake();
                connectedNode = new GroupLeader(info.groupOwnerAddress.getHostAddress());
                startServer(isGroupOwner);
            }
            Log.d(TAG, "Group formed.");
            Toast.makeText(getApplicationContext(), "Group formed.", Toast.LENGTH_SHORT).show();
        }

    }

    private void initializeWifiP2p() {
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
    }

    private void initializeIntentFilter() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void startServer(boolean leader) {

        if (leader) {
            messageServerAsyncTask = new MessageServerAsyncTask(this, 8888);
            messageServerAsyncTask.delegate = this;
            messageServerAsyncTask .execute();
        } else {
            messageServerAsyncTask = new MessageServerAsyncTask(this, 8889);
            messageServerAsyncTask.delegate = this;
            messageServerAsyncTask .execute();
        }

    }

    @Override
    public void onChannelDisconnected() {

    }

//    private void showProgressBar(boolean show) {
//        if (show) {
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//    }

    private void setConnected(boolean connected) {
        if (connected) {
            isConnected = true;
        } else {
            isConnected = false;
        }
    }


    /**
     * Adapter class for populating the listview with peer info
     */


    public class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

        private List<WifiP2pDevice> items;

        /**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                                   List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.peer_list_view_item, null);
            }

            WifiP2pDevice device = items.get(position);

            if (device != null) {

                TextView top = (TextView) v.findViewById(R.id.peer_name);
                if (top != null) {

                    top.setText(device.deviceName);

                }
            }

            return v;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
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

}
