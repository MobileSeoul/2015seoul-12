package sgt.hansung.com.suh_gongteo;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by jang on 2015-10-20.
 */
public class MyItem implements ClusterItem, cluster.ClusterItem {
    private LatLng mPosition;
    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    public void setPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }
}
