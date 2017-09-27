package sgt.hansung.com.suh_gongteo;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jang on 2015-10-28.
 */
public class manageListAdapter extends BaseAdapter {

    Context context;
    int layoutId;
    ArrayList<manageListData> manDataArr;
    LayoutInflater Inflater;
    manageListAdapter(Context context,int layoutId, ArrayList<manageListData> manDataArr){
        this.context = context;
        this.layoutId = layoutId;
        this.manDataArr = manDataArr;
        Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return manDataArr.size();
    }

    @Override
    public manageListData getItem(int position) {
        return manDataArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)  {
            convertView = Inflater.inflate(layoutId, parent, false);
        }

        ImageView Img = (ImageView)convertView.findViewById(R.id.manageImage);
        TextView title = (TextView)convertView.findViewById(R.id.manageTitle);
        TextView period = (TextView)convertView.findViewById(R.id.managePeriod);
        TextView price = (TextView)convertView.findViewById(R.id.managePrice);

        Drawable d = new BitmapDrawable(manDataArr.get(position).manImg);
        Img.setBackground(d);
        title.setText(manDataArr.get(position).manTitle);
        period.setText(manDataArr.get(position).manPeriod);
        price.setText(manDataArr.get(position).manPrice);


        return convertView;

    }
}
