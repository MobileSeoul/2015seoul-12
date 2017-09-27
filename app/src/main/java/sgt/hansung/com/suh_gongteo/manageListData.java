package sgt.hansung.com.suh_gongteo;

import android.graphics.Bitmap;

/**
 * Created by jang on 2015-10-28.
 */
public class manageListData {
    Bitmap manImg;
    String manTitle,manPeriod,manPrice,manObjectId,manAddress;
    manageListData(Bitmap manImg, String manTitle, String manPeriod, String manPrice, String manObjectId,String manAddress){
        this.manImg = manImg;
        this.manTitle = manTitle;
        this.manPeriod = manPeriod;
        this.manPrice = manPrice;
        this.manObjectId = manObjectId;
        this.manAddress = manAddress;
    }
}
