package sgt.hansung.com.suh_gongteo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cluster.Cluster;
import cluster.ClusterManager;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;

    private ParseUser user;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ClusterManager<MyItem> mClusterManager;  // Declare a variable for the cluster manager.
    private RelativeLayout layoutRoot;
    EditText searchMap;
    TextView resultAddress;
    String address,result ="";
    SlidingDrawer mapListDrawer;
    ListView clusterListView;
    ArrayList<ClusterListData> clusterListDataArr;
    ClusterListAdapter clusterListAdapter;
    String listPrice="",listAddress="",listTitle="";
    ParseFile imageFile;
    byte[] parseBitmapByte;
    Bitmap parseBitmap;

    Menu curMenu;
    View navView;


    ProgressDialog progressDialog; // ProgressDialog
    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("서울 공간 나눔터");

        backPressCloseHandler = new BackPressCloseHandler(this);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);

        searchMap = (EditText)findViewById(R.id.edtSearch);
        address = "";

        //로그인,로그아웃 상태에 따른 클릭 리스너 설정//
        navView = (View) getLayoutInflater().inflate(R.layout.nav_header_main_logout, null);
        ImageView navAdd = (ImageView) navView.findViewById(R.id.nav_navAdd);
        ImageView navLogin = (ImageView)findViewById(R.id.nav_navLogin);

        navLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        navAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Write.class);
                startActivity(intent);
            }
        });
        //로그인,로그아웃 상태에 따른 클릭 리스너 설정 끝//


        //▶SlidingDrawer setting start
        final LinearLayout test = (LinearLayout)findViewById(R.id.content);
        mapListDrawer = (SlidingDrawer) findViewById(R.id.slide);
        mapListDrawer.getHandle().setAlpha(1);
        mapListDrawer.getHandle().setBackgroundColor(Color.rgb(255, 132, 31));
        mapListDrawer.lock();
        final ImageView arrow = (ImageView)findViewById(R.id.handle);
        mapListDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                arrow.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.downarrow));
            }
        });
        mapListDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                arrow.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.uparrow));
            }
        });
        //▶SlidingDrawer setting end

        //▶parse init start

//        Parse.enableLocalDatastore(this);

        try{
            Parse.initialize(this, "xsoLb3SWvzsozo4Wr3rUe8dXlnoKC2jj19LZM32t", "4wDfUFQzVezBawdmcgZbytiZzUZN5jpkg3CHPd65");
            ParseFacebookUtils.initialize("943601879045027");

        } catch (Exception e) {
            e.printStackTrace();
        }


        user = ParseUser.getCurrentUser();

        //▶parse init end

        //▶이메일 버튼 시작
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //▶이메일 버튼 끝

        //▶툴바

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        //mTitle.setText("툴바타이틀 센터에!");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //▶툴바 끝

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        MenuItem a = navigationView.getMenu().getItem(1);
//        a.setVisible(!a.isVisible());

        isUserLogin(navigationView);

        navigationView.setNavigationItemSelectedListener(this);


        //▶google Map setting start

        setUpMapIfNeeded();
        setUpClusterer();
        //▶google Map setting end

        //▶clusterListView init start
        clusterListView = (ListView)findViewById(R.id.clusterListView);
        clusterListDataArr = new ArrayList<ClusterListData>();
        clusterListAdapter = new ClusterListAdapter(this,R.layout.list_item,clusterListDataArr);
        clusterListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        clusterListView.setAdapter(clusterListAdapter);

        clusterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra("address", clusterListDataArr.get(position).address);
                startActivity(intent);

            }
        });
        //▶clusterListView init end



        //▶클러스터 선택시
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(final Cluster<MyItem> myItemCluster) {

                progressDialog = ProgressDialog.show(MainActivity.this, "로딩중입니다","잠시만 기다려주세요",true);
                mapListDrawer.unlock();
                clusterListDataArr.removeAll(clusterListDataArr);
                ArrayList<MyItem> items = new ArrayList<MyItem>(myItemCluster.getItems());
                final int[] finalI = {0};
                for (int i = 0; i < myItemCluster.getSize(); i++) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("post");
                    query.whereEqualTo("lat", items.get(i).getPosition().latitude);
                    query.whereEqualTo("log", items.get(i).getPosition().longitude);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object != null) {
                                imageFile = (ParseFile) object.get("image1");
                                try {
                                    parseBitmapByte = imageFile.getData();
                                    parseBitmap = BitmapFactory.decodeByteArray(parseBitmapByte, 0, parseBitmapByte.length);
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                    parseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_play);
                                } catch (NullPointerException e2) {
                                    parseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_parent_rounded_corner);
                                }
                                listAddress = object.getString("address");
                                listPrice = object.getString("price");
                                listTitle = object.getString("spaceTitle");
                                clusterListDataArr.add(new ClusterListData(parseBitmap, listPrice, listTitle, listAddress));
                            } else {
                                Log.d("onClusterClickListener", e.getMessage());
                            }
                            finalI[0]++;
                            if (finalI[0] == myItemCluster.getSize()) {
                                if (clusterListDataArr.size() == 1)
                                    mapListDrawer.getLayoutParams().height = convertDpToPixel(180, getApplicationContext());
                                else
                                    mapListDrawer.getLayoutParams().height = convertDpToPixel(300, getApplicationContext());
                                clusterListAdapter.notifyDataSetChanged();
                                if (mapListDrawer.isOpened() == true) {
                                    mapListDrawer.open();
                                } else {
                                    mapListDrawer.animateOpen();
                                }

                            }

                            progressDialog.dismiss();
                        }
                    });
                }

                return false;
            }
        });
        //▶마커 선택시
//        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
//            @Override
//            public boolean onClusterItemClick(MyItem myItem) {
//                double a = myItem.getPosition().latitude;
//                double b = myItem.getPosition().longitude;
//                Log.d(a + "", b + "");
//                return false;
//            }
//        });


    }

    //▶onCreate end


    public void isUserLogin(NavigationView navigationView){
        MenuItem[] a = new MenuItem[navigationView.getMenu().size()];
        LinearLayout navHeader1 = (LinearLayout)findViewById(R.id.nav_login_pre);

        for(int i=0;i<navigationView.getMenu().size();i++){
            a[i] = navigationView.getMenu().getItem(i);
        }
        a[0].setVisible(true);//홈
        a[3].setVisible(false);//개발자정보
        a[2].setVisible(true); // 공간등록
        if(user!=null){
            //로그인 됬을때
            navigationView.removeHeaderView(navHeader1);
            navigationView.addHeaderView(navView);
            navView.getLayoutParams().height = convertDpToPixel(250,MainActivity.this);
            TextView navText = (TextView)navView.findViewById(R.id.welcome);
            navText.setText("환영합니다");
            a[1].setVisible(false);//로그인
            a[4].setVisible(true);//찜한공간
            a[5].setVisible(true);//공간관리
            a[6].setVisible(true);//로그아웃
        }else{
            //로그인 안됐을 때
            navigationView.removeHeaderView(navHeader1);
            navigationView.removeHeaderView(navView);
            navigationView.addHeaderView(navHeader1);
            a[1].setVisible(false);
            a[4].setVisible(false);
            a[5].setVisible(false);//공간관리
            a[6].setVisible(false);//로그아웃
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    public static int convertDpToPixel(int dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * (metrics.densityDpi / 160);
        return px;
    }

    public void getAddress(){
        try {
            double country=0,area=0;
            result="";

            Geocoder geo = new Geocoder(this, Locale.KOREA);
            double lowerLeftLatitude=37.4721429,lowerLeftLongitude=126.7582425,upperRightLatitude=37.6939475,upperRightLongitude=127.2133787;
            List<Address> addr = geo.getFromLocationName(address,1,lowerLeftLatitude,lowerLeftLongitude,upperRightLatitude,upperRightLongitude);
            if (addr.size() > 0) {
                country = addr.get(0).getLatitude();
                area = addr.get(0).getLongitude();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(country, area)));

            } else {
                Toast toast=Toast.makeText(this, "정확한 장소를 입력해주세요", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER,0,170);
                toast.show();
            }

        } catch (IOException e) {
            Toast toast=Toast.makeText(this, "    Failed to bringing location    ", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER,0,170);
            toast.show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        progressDialog = ProgressDialog.show(MainActivity.this, "로딩중입니다","잠시만 기다려주세요",true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.setMyLocationEnabled(true);

    }
    private void setUpClusterer() {

        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5665350, 126.9779690), 17));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
    private void addItems() {
        final double[] lat = new double[1];
        final double[] lng = new double[1];
        final MyItem[] offsetItem = new MyItem[1];

        ParseQuery<ParseObject> query = ParseQuery.getQuery("post");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> object, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < object.size(); i++) {
                        lat[0] = object.get(i).getDouble("lat");
                        lng[0] = object.get(i).getDouble("log");
                        offsetItem[0] = new MyItem(lat[0], lng[0]);
                        mClusterManager.addItem(offsetItem[0]);
                    }
                    progressDialog.dismiss();
                } else {
                    Log.d("addItems", "Error: " + e.getMessage());
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5665350, 126.9779690), 16));

            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }

        backPressCloseHandler.onBackPressed();

        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        curMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_search){
            handleMenuSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        progressDialog = ProgressDialog.show(MainActivity.this, "로딩중입니다","잠시만 기다려주세요",true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        if (id == R.id.nav_login) {
            // Handle the camera action
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }else if(id == R.id.home){
            drawer.closeDrawer(GravityCompat.START);
        }else if (id == R.id.nav_spaceadd) {
            if(ParseUser.getCurrentUser()!=null){
                Intent intent = new Intent(this, Write.class);
                startActivity(intent);}
            else{
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_info) {
            Intent intent = new Intent(this, Management.class);
            startActivity(intent);
        }else if (id == R.id.like){
            Intent intent = new Intent(this, MyInterest.class);
            startActivity(intent);
        }else if (id == R.id.nav_spaceManagement){
            Intent intent = new Intent(this,Management.class);
            startActivity(intent);
        }else if (id == R.id.logout){
            progressDialog = ProgressDialog.show(MainActivity.this, "로딩중입니다","잠시만 기다려주세요",true);
            ParseUser.logOut();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            progressDialog.dismiss();
            finish();
        }

        progressDialog.dismiss();
        drawer.dispatchSetSelected(false);
        drawer.setSelected(false);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar();

        if(isSearchOpened){ //test if the search is open
            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchMap.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.search_icon));

            isSearchOpened = false;
        } else { //open the search entry

            isSearchOpened = true;

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setDisplayShowTitleEnabled(false); //hide the title
            action.setCustomView(R.layout.search_bar);//add the custom view

            searchMap = (EditText)action.getCustomView().findViewById(R.id.edtSearch);
            searchMap.getLayoutParams().width = convertDpToPixel(240,getApplicationContext());
            //this is a listener to do a search when the user clicks on search button
            searchMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch();
                        return true;
                    }
                    return false;
                }
            });

            searchMap.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchMap, InputMethodManager.SHOW_IMPLICIT);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.close_icon));


        }
    }

    private void doSearch() {
        if(searchMap.getVisibility()==View.VISIBLE) {
            address = searchMap.getText().toString();
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(searchMap.getWindowToken(), 0);
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.close_icon));
            getAddress();
        }else{
            searchMap.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchMap, InputMethodManager.SHOW_FORCED);
            resultAddress.setText("");
        }
    }


}