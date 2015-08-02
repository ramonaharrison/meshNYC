//package c4q.nyc.ramonaharrison.meshnyc;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.google.maps.android.clustering.ClusterManager;
//
//import java.util.ArrayList;
//
///**
// * Created by Hoshiko on 8/1/15.
// */
//public class ShelterListAsync extends AsyncTask<Void, Void, ArrayList<Shelter>> {
//    private Context context;
//    ClusterManager<MarkerCluster> mClusterManager;
//
//    public ShelterListAsync(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected ArrayList<Shelter> doInBackground(Void... params) {
//
//        SQLHelper helper = SQLHelper.getInstance(context);
//        return helper.getAllShelters();
//    }
////
////    @Override
////    protected void onPostExecute(final ArrayList<Shelter>shelterList) {
////        int shelterNum = shelterList.size();
////
////        for (int i = 0; i <= shelterNum; i++) {
////            Double lng = shelterList.get(i).getLongitude();
////            Double lat = shelterList.get(i).getLatitude();
////            MarkerCluster offsetItem = new MarkerCluster(lat, lng);
////            mClusterManager.addItem(offsetItem);
////        }
////    }
//
//
//
//}
