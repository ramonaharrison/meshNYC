package c4q.nyc.ramonaharrison.meshnyc;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Hoshiko on 8/2/15.
 */
public class ClusterRendring extends DefaultClusterRenderer<MarkerCluster> {

    public ClusterRendring(Context context, GoogleMap map,
            ClusterManager<MarkerCluster> clusterManager) {
            super(context, map, clusterManager);
            }


    protected void onBeforeClusterItemRendered(MarkerCluster item, MarkerOptions markerOptions) {

//            markerOptions.icon(item.getIcon());
//            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());
            super.onBeforeClusterItemRendered(item, markerOptions);
            }
}
