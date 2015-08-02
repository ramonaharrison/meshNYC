package c4q.nyc.ramonaharrison.meshnyc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Button button = (Button) findViewById(R.id.button2);

        if (connectedNetwork()) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent alertIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.org/publicalerts"));
                    startActivity(alertIntent);

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
//        mClusterManager.setOnClusterItemInfoWindowClickListener (MapActivity.this);

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
//        mMap.setOnMarkerClickListener(mClusterManager);
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//
//                                          @Override
//                                          public boolean onMarkerClick(Marker marker) {
//
//                                              String a = marker.getTitle();
////                                              AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
////                                              alertDialog.setTitle("Address of this shelter");
////                                              alertDialog.setMessage("test");
////                                              alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
////                                                      new DialogInterface.OnClickListener() {
////                                                          public void onClick(DialogInterface dialog, int which) {
////                                                              dialog.dismiss();
////                                                          }
////                                                      });
////                                              alertDialog.show();
//
//                                              Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
//                                              return true;
//                                          }
//
//
//                                      });


        mClusterManager.setRenderer(new ClusterRendring(getApplicationContext(), mMap, mClusterManager));

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerCluster>() {
            @Override
            public boolean onClusterItemClick(MarkerCluster markerCluster) {

                String a = markerCluster.getTitle();
                Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                return false;
            }

        });


            // Add cluster items (markers) to the cluster manager.
            ShelterListAsync shelterListTask = new ShelterListAsync();
            shelterListTask.execute();
//        addItems();
        }

//


        @Override
    public void onInfoWindowClick(Marker marker) {

    }

//    private void addItems() {
//
//        // Set some lat/lng coordinates to start with.
//        double lat = 40.7069308;
//        double lng = -74.0053852;
//
//        // Add ten cluster items in close proximity, for purposes of this example.
//        for (int i = 0; i < 10; i++) {
//            double offset = i / 60d;
//            lat = lat + offset;
//            lng = lng + offset;
//
//            Toast.makeText(getApplicationContext(), lat + ", " + lng, Toast.LENGTH_SHORT).show();
//
//            MarkerCluster offsetItem = new MarkerCluster(lat, lng);
//            mClusterManager.addItem(offsetItem);
//        }
//        ArrayList <Shelter> shelterList = new ArrayList<Shelter>();
//
//        int shelterNum = shelterList.size();
//        for (int i = 0; i < 20; i++) {
//            Shelter shelter = shelterList.get(i);
//            double lng = shelter.getLongitude();
//            double lat = shelter.getLatitude();
//            MarkerCluster mc = new MarkerCluster(lat, lng);
//            mClusterManager.addItem(mc);
//        }


    public class ShelterListAsync extends AsyncTask<Void, Void, ClusterManager<MarkerCluster> > {

//        private ClusterManager<MarkerCluster> mClusterManager;

        protected void onPreExecute(){
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


//        @Override
//        protected void onPostExecute(ClusterManager<MarkerCluster> mClusterManager) {
////            int shelterNum = shelterList.size();
////
////            for (int i = 0; i < shelterNum; i++) {
////
////                Shelter shelter = shelterList.get(i);
////                double lat = shelter.getLongitude();
////                double lng = shelter.getLatitude();
////                Toast.makeText(getApplicationContext(), lat + ", " + lng, Toast.LENGTH_SHORT).show();
////                MarkerCluster mc = new MarkerCluster(lat, lng);
////                mClusterManager.addItem(mc);
//
//            }
//        }


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

