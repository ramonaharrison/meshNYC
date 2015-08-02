package c4q.nyc.ramonaharrison.meshnyc;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Hoshiko on 8/1/15.
 */
public class MarkerCluster implements ClusterItem {
    private final LatLng mPosition;

    public MarkerCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
