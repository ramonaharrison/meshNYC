package c4q.nyc.ramonaharrison.meshnyc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PeerActivity extends ActionBarActivity implements ChannelListener, PeerListListener {

    private WifiP2pManager mManager;
    private Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private PeerListListener peerListListener;
    private Button findPeers;
    private ListView peersList;
    private List<WifiP2pDevice> peers;
    private WiFiPeerListAdapter peerAdapter;
    private ProgressBar progressBar;
    private TextView progressText;

    final static String TAG = "PeerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peer);
        peers = new ArrayList<>();

        initializeView();
        initializeWifiP2p();
        initializeIntentFilter();

    }

    private void initializeView() {
        getSupportActionBar().setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        showProgressBar(false);

        progressText = (TextView) findViewById(R.id.progress_textview);
        findPeers = (Button) findViewById(R.id.find_peers);
        findPeers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true);
                progressText.setText("Searching for peers...");
                discoverPeers();
            }
        });

        peersList = (ListView) findViewById(R.id.peer_list_view);
        peerAdapter = new WiFiPeerListAdapter(
                this,
                android.R.layout.simple_list_item_1,
                peers);

        peersList.setAdapter(peerAdapter);
        peersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                connectToPeer(position);
            }
        });
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void connectToPeer(int position) {
        //obtain a peer from the WifiP2pDeviceList

        WifiP2pDevice device = peers.get(position);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                //success logic
                Toast.makeText(getApplicationContext(), "Connected to peer.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode) {
                //failure logic
                Toast.makeText(getApplicationContext(), "Something went wrong." + reasonCode, Toast.LENGTH_SHORT).show();

            }

        });
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

    private void discoverPeers() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                showProgressBar(false);
            }

            @Override
            public void onFailure(int reasonCode) {
                showProgressBar(false);
                Toast.makeText(getApplicationContext(), "Something went wrong." + reasonCode, Toast.LENGTH_SHORT).show();
                progressText.setText("No peers found.");
            }
        });
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

    @Override
    public void onChannelDisconnected() {

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {

        peers.clear();
        peers.addAll(peerList.getDeviceList());

//        List<WifiP2pDevice> updatedPeers = (List) peerList.getDeviceList();
//        for (WifiP2pDevice x : updatedPeers){
//            if (!peers.contains(x))
//                peers.add(x);
//        }
        peerAdapter.notifyDataSetChanged();


        if (peers.size() == 0) {
            progressText.setText("No peers found.");
            Toast.makeText(getBaseContext(), "No peers found.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            progressText.setVisibility(View.INVISIBLE);
        }
    }


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

}
