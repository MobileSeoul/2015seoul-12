package sgt.hansung.com.suh_gongteo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by MinJeong on 2015-10-28.
 */
public class ViewPagerFragment1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.viewpager_children, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

        try {
            Bundle args = getArguments();
            byte[] data = args.getByteArray("image");
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

            imageView.setImageBitmap(bmp);
        }catch (NullPointerException e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.basic);
        }

        return rootView;
    }

    public static ViewPagerFragment1 newInstance(byte[] data) {
        Bundle args = new Bundle();
        args.putByteArray("image", data);
        ViewPagerFragment1 fragment = new ViewPagerFragment1();
        fragment.setArguments(args);
        return fragment;
    }

}
