package sgt.hansung.com.suh_gongteo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;

/*
 * Created by MinJeong on 2015-10-25.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    HashMap<String, Object> data;


    public ViewPagerAdapter(FragmentManager fm, HashMap<String, Object> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return ViewPagerFragment1.newInstance((byte[]) data.get("image1"));
            case 1:
                return ViewPagerFragment2.newInstance((byte[]) data.get("image2"));
            case 2:
                return ViewPagerFragment3.newInstance((byte[]) data.get("image3"));
            default:
                return new ViewPagerFragment0();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
