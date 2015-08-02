package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

/**
 * Created by Hoshiko on 8/1/15.
 */
public class MapActivity extends Activity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener {

    GoogleMap mMap;
    Location location;
    MapFragment mapFragment;
    String zipcode;
    String googleStaticMap;

    private ProgressBar bar;

    // Declare a variable for the cluster manager.
    ClusterManager<MarkerCluster> mClusterManager;
    private String fullAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        bar = (ProgressBar)findViewById(R.id.progressBar);





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


        setUpClusterer();
        // Add cluster items (markers) to the cluster manager.
        ShelterListAsync shelterListTask = new ShelterListAsync();
        shelterListTask.execute();

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



    }



    private void setUpClusterer() {

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.7136487, -74.0087126), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MarkerCluster>(getApplicationContext(), mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);


        mClusterManager.setRenderer(new ClusterRendring(getApplicationContext(), mMap, mClusterManager));

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerCluster>() {
            @Override
            public boolean onClusterItemClick(MarkerCluster markerCluster) {

                String a = markerCluster.getTitle();
                Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                return false;
            }

        });


    }




    @Override
    public void onInfoWindowClick(Marker marker) {

    }


    public class ShelterListAsync extends AsyncTask<Void, Void, ClusterManager<MarkerCluster> > {

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ClusterManager<MarkerCluster> doInBackground(Void... params) {

            SQLHelper helper = SQLHelper.getInstance(getApplicationContext());

            ArrayList<Shelter> shelterList = helper.getAllShelters();
            int shelterNum = shelterList.size();

            for (int i = 0; i < shelterNum; i++) {

                Shelter shelter = shelterList.get(i);
                double lat = shelter.getLongitude();
                double lng = shelter.getLatitude();
                String address = shelter.getAddress();
                String city = shelter.getCity();
                String zip = shelter.getPostal();
                fullAddress = address + ", " + city + " " + zip;
                MarkerCluster mc = new MarkerCluster(lat, lng, fullAddress);
                mClusterManager.addItem(mc);
            }

            return mClusterManager;
        }

        @Override
        protected void onPostExecute(ClusterManager<MarkerCluster> mClusterManager) {

            bar.setVisibility(View.GONE);
        }

    }


    private boolean connectedNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

}

