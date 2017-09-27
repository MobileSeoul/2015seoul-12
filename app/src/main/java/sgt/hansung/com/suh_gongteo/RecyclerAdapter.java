package sgt.hansung.com.suh_gongteo;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by MinJeong on 2015-10-26.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HashMap<String, Object> dataSet;
    private int[] mDataSetTypes;
    private FragmentManager manager;
    private LayoutInflater mInflater;

    public static final int imageCard = 0;
    public static final int infoCard = 1;
    public static final int optionCard = 2;
    private static final int OPTIONS = 12;


    public static int DRAWABLE[] = {R.drawable.option0, R.drawable.option1, R.drawable.option2,
            R.drawable.option3, R.drawable.option4, R.drawable.option5,
            R.drawable.option6, R.drawable.option7, R.drawable.option8,
            R.drawable.option9, R.drawable.option10, R.drawable.option11 };


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class SpaceTitleViewHolder extends ViewHolder implements OnMapReadyCallback {
        TextView spaceTitle, spacePhone, spaceAddress, spaceDetail, maxPeople, spaceFee;
        ViewPager viewPager;
        ViewPagerAdapter adapter;
        GoogleMap gMap;
        MapView map;
        public SpaceTitleViewHolder(View v) {
            super(v);
            this.spaceTitle = (TextView) v.findViewById(R.id.space_name);
            this.spacePhone = (TextView) v.findViewById(R.id.space_phone);
            this.spaceAddress = (TextView) v.findViewById(R.id.space_address);
            this.spaceDetail = (TextView) v.findViewById(R.id.space_info);
            this.maxPeople = (TextView) v.findViewById(R.id.max_people_value);
            this.spaceFee = (TextView) v.findViewById(R.id.space_fee_value);
            map = (MapView) v.findViewById(R.id.detailMap);

            if (map != null)
            {
                map.onCreate(null);
                map.onResume();
                map.getMapAsync(this);
            }
            viewPager = (ViewPager) v.findViewById(R.id.viewPager);
            adapter = new ViewPagerAdapter(manager, dataSet);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
        }
    }

    public class SpaceInfoViewHolder extends ViewHolder {
        TextView spaceType, time;

        public SpaceInfoViewHolder(View v) {
            super(v);
            this.spaceType = (TextView) v.findViewById(R.id.space_type_value);
            this.time = (TextView) v.findViewById(R.id.space_date_value);

        }
    }

    public class SpaceOptionViewHolder extends ViewHolder {
        LinearLayout layout;

        public SpaceOptionViewHolder(View v) {
            super(v);
            this.layout = (LinearLayout) v.findViewById(R.id.option_layout);
        }
    }

    // Constructor
    public RecyclerAdapter(HashMap<String, Object> dataSet, int[] dataSetTypes,
                           FragmentManager manager, LayoutInflater inflater) {
        this.dataSet = dataSet;
        mDataSetTypes = dataSetTypes;
        this.manager = manager;
        this.mInflater = inflater;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == imageCard) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.content_detail_1, viewGroup, false);
            return new SpaceTitleViewHolder(v);
        } else if (viewType == infoCard) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.content_detail_2, viewGroup, false);
            return new SpaceInfoViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.content_detail_3, viewGroup, false);
            return new SpaceOptionViewHolder(v);
        }
    }
    private  void setUpMap(GoogleMap googleMap,LatLng latlng){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
        googleMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(24.0f)));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder.getItemViewType() == imageCard) {
            SpaceTitleViewHolder holder = (SpaceTitleViewHolder) viewHolder;

            double lat= (Double)dataSet.get("lat");
            double lng= (Double)dataSet.get("log");

            LatLng latlng = new LatLng(lat,lng);
            GoogleMap thisMap = holder.gMap;

            if(thisMap == null) {
                thisMap = holder.map.getMap();
            }
            setUpMap(thisMap, latlng);

            holder.viewPager.setAdapter(holder.adapter);
            holder.spaceTitle.setText(dataSet.get("spaceTitle").toString());
            holder.spaceDetail.setText(dataSet.get("spaceDetail").toString());
            holder.spacePhone.setText(dataSet.get("phone").toString());
            holder.spaceAddress.setText(dataSet.get("address").toString());
            holder.maxPeople.setText(dataSet.get("maxPeople") + "명");
            holder.spaceFee.setText(dataSet.get("price") + "원");
        }
        else if (viewHolder.getItemViewType() == infoCard) {
            SpaceInfoViewHolder holder = (SpaceInfoViewHolder) viewHolder;
            holder.spaceType.setText(dataSet.get("spaceType").toString());
            SimpleDateFormat format = new SimpleDateFormat("MM월 dd일 HH:mm");
            holder.time.setText(format.format(dataSet.get("startTime")) + " ~ " + format.format(dataSet.get("endTime")));
        }
        else {
            SpaceOptionViewHolder optionViewHolder = (SpaceOptionViewHolder) viewHolder;

            for(int j = 0; j < OPTIONS/4; j++) {
                LinearLayout innerLayout = new LinearLayout(mInflater.getContext());
                optionViewHolder.layout.addView(innerLayout);

                for (int i = (j*4); i < ((j*4)+4); i++) {
                    if (dataSet.get("option" + i).toString().equals("true")) {
                        ImageView iv = new ImageView(mInflater.getContext());
                        iv.setImageResource(DRAWABLE[i]);
                        innerLayout.addView(iv);
                    }
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSetTypes[position];
    }
}
