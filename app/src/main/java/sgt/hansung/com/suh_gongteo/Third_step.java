package sgt.hansung.com.suh_gongteo;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by user on 2015-10-21.
 */


public class Third_step extends Fragment {

    private SupportMapFragment fragment;
    private GoogleMap mMap;
    Geocoder geo;

    EditText signSearchMap;
    ImageView searchBtn;
    String searchAddress="";

    Double lat=0.0,log=0.0; String address=""; //위도,경도,주소값이 값을 전달해주면 될듯!
    public Third_step() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout linearLayout= (RelativeLayout)  inflater.inflate(R.layout.fragment_third_step, container, false);
        signSearchMap = (EditText)linearLayout.findViewById(R.id.signSearchMap);
        searchBtn = (ImageView)linearLayout.findViewById(R.id.searchBtn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAddress = signSearchMap.getText().toString();
                getAddress();
            }
        });
        signSearchMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchAddress = signSearchMap.getText().toString();
                        getAddress();
                        break;
                }
                return false;
            }
        });
        return linearLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        geo = new Geocoder(getContext(), Locale.KOREAN );
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.signMap);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.signMap, fragment).commit();
        }


    }
    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = fragment.getMap();
        }
        if (mMap != null) {
            setUpMap();
        }
    }

    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setMyLocationEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5665350, 126.9779690), 17));

        final Marker[] m1 = new Marker[1];
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (m1[0] != null) {
                    m1[0].remove();
                }

                lat = latLng.latitude;
                log = latLng.longitude;
                address = getAddress(latLng);
                m1[0] = mMap.addMarker(new MarkerOptions().position(latLng).title("주소 : "+ address).icon(BitmapDescriptorFactory.defaultMarker(24.0f)));
                m1[0].showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //인포윈도우를 선택하면 할 동작을 넣으면 됩니다.
            }
        });
    }

    public String getAddress(LatLng latLng){
        String a;
        try {
            List<Address> address = geo.getFromLocation(latLng.latitude,latLng.longitude, 10);
            a = address.get(0).getAddressLine(0);
        } catch (IOException e) {
            a=e.toString();
        }
        return a;
    }
    public void getAddress(){
        try {
            double lowerLeftLatitude=37.4721429,lowerLeftLongitude=126.7582425,upperRightLatitude=37.6939475,upperRightLongitude=127.2133787;
            List<Address> addr = geo.getFromLocationName(searchAddress,1,lowerLeftLatitude,lowerLeftLongitude,upperRightLatitude,upperRightLongitude);
            if (addr.size() > 0) {
                searchAddress =addr.get(0).getAddressLine(0);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(addr.get(0).getLatitude(),addr.get(0).getLongitude())));
            } else {
                Toast toast=Toast.makeText(getContext(), "정확한 장소를 입력해주세요", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER,0,170);
                toast.show();
            }

        } catch (IOException e) {
            Toast toast=Toast.makeText(getContext(), "    Failed to bringing location    ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER,0,170);
            toast.show();
        }
    }

}