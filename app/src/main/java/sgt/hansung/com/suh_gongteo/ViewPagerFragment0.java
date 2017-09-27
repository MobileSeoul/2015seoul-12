package sgt.hansung.com.suh_gongteo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by MinJeong on 2015-10-28.
 */
public class ViewPagerFragment0 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.viewpager_children, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.basic);

        return rootView;
    }

}
