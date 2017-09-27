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
 * Created by jang on 2015-10-22.
 */
public class ClusterListAdapter extends BaseAdapter {

    Context context;

    int layoutId;

    ArrayList<ClusterListData> clusterListDataArr;

    LayoutInflater Inflater;



    ClusterListAdapter(Context _context, int _layoutId, ArrayList<ClusterListData> _clusterListDataArr){

        context = _context;

        layoutId = _layoutId;

        clusterListDataArr = _clusterListDataArr;

        Inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return clusterListDataArr.size();
    }

    @Override
    public ClusterListData getItem(int i) {
        return clusterListDataArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //getView는 ListView의 화면을 생성해주는 함수이다.
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        // 1번 그룹 :
        // 스크롤시 기존에 생성된 리스트 아이템에 대해서 재생성하지 않도록 하는 것
        if (convertView == null)  {
            convertView = Inflater.inflate(layoutId, parent, false);
        }
        // 1번 그룹 끝

        ImageView lmg = (ImageView)convertView.findViewById(R.id.clusterListImg);
        TextView price = (TextView)convertView.findViewById(R.id.clusterListPrice);
        TextView address = (TextView)convertView.findViewById(R.id.clusterListAddress);
        TextView title = (TextView)convertView.findViewById(R.id.clusterListTitle);

        Drawable d = new BitmapDrawable(clusterListDataArr.get(i).img);
        lmg.setBackground(d);
        price.setText(clusterListDataArr.get(i).price);
        address.setText(clusterListDataArr.get(i).address);
        title.setText(clusterListDataArr.get(i).title);


        return convertView;
    }
}
