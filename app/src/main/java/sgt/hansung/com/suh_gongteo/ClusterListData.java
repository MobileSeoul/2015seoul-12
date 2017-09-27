package sgt.hansung.com.suh_gongteo;

import android.graphics.Bitmap;

/**
 * Created by jang on 2015-10-22.
 */
public class ClusterListData {
    Bitmap img;
    String price,title,address;
    ClusterListData(Bitmap img, String price,String title, String address){
        this.img = img;
        this.price = price;
        this.title = title;
        this.address = address;
    }
}
