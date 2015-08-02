package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

/**
 * Created by Hoshiko on 8/1/15.
 */
public class MapActivity extends Activity implements OnMapReadyCallback, LocationListener {

    GoogleMap mMap;
    Location location;
    MapFragment mapFragment;
    String zipcode;
    String googleStaticMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alertIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.org/publicalerts"));
                startActivity(alertIntent);

            }
        });


        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available


            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            // Getting GoogleMap object from the fragment
            mMap = mapFragment.getMap();

            // Enabling MyLocation Layer of Google Map
            mMap.setMyLocationEnabled(true);

//            zipcode = "11217";
//
//            googleStaticMap = "http://maps.google.com/maps/api/staticmap?center=" + zipcode + "&zoom=17&size=600x600&sensor=&maptype=roadmap" +
//                    "&markers=color:blue%7Clabel:S%7C" + zipcode;
//


            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200000, 0, this);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200000, 0, this);
//        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 200000, 0, this);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }


        }

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap.addMarker(new MarkerOptions()
//                .position(new LatLng(10, 10))
//                .title("Hello world"));
//
//
//        final LatLng MELBOURNE = new LatLng(-37.813, 144.962);
//         Marker melbourne = mMap.addMarker(new MarkerOptions()
//                .position(MELBOURNE)
//                .title("Melbourne")
//                .snippet("Population: 4,137,400"));

        setUpClusterer();
    }


    // Declare a variable for the cluster manager.
    ClusterManager<MarkerCluster> mClusterManager;

    private void setUpClusterer() {

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7136487, -74.0087126), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        ShelterListAsync shelterTask = new ShelterListAsync();
        shelterTask.execute();
//        addItems();
    }

//    private void addItems() {
//
//        // Set some lat/lng coordinates to start with.
//        double lat = 40.7069308;
//        double lng = -74.0053852;
//
//        // Add ten cluster items in close proximity, for purposes of this example.
////        for (int i = 0; i < 10; i++) {
////            double offset = i / 60d;
////            lat = lat + offset;
////            lng = lng + offset;
//            MarkerCluster offsetItem = new MarkerCluster(lat, lng);
//            mClusterManager.addItem(offsetItem);
////        }
////        ArrayList <Shelter> shelterList = new ArrayList<Shelter>();
////
////        int shelterNum = shelterList.size();
////        for (int i = 0; i < 20; i++) {
////            Shelter shelter = shelterList.get(i);
////            double lng = shelter.getLongitude();
////            double lat = shelter.getLatitude();
////            MarkerCluster mc = new MarkerCluster(lat, lng);
////            mClusterManager.addItem(mc);
////        }
//    }


    public class ShelterListAsync extends AsyncTask<Void, Void, ArrayList<Shelter>> {
//        private Context context;
//
//        public ShelterListAsync(Context context) {
//            this.context = context;
//        }

        @Override
        protected ArrayList<Shelter> doInBackground(Void... params) {

            SQLHelper helper = SQLHelper.getInstance(getApplicationContext());

            return helper.getAllShelters();
        }

        @Override
        protected void onPostExecute(ArrayList<Shelter> shelterList) {
            int shelterNum = shelterList.size();
            for (int i = 0; i < 5; i++) {
                Shelter shelter = shelterList.get(i);
                double lng = shelter.getLongitude();
                double lat = shelter.getLatitude();
                MarkerCluster mc = new MarkerCluster(lat, lng);
                mClusterManager.addItem(mc);
            }
        }
    }

}

