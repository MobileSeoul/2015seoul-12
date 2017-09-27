package sgt.hansung.com.suh_gongteo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Detail extends AppCompatActivity {

    public static final int imageCard = 0;
    public static final int infoCard = 1;
    public static final int optionCard = 2;

    private static final int OPTIONS = 12;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private HashMap<String, Object> dataSet;
    private int mDatasetTypes[] = {imageCard, infoCard, optionCard};

    private Toolbar mToolbar;
    private Menu curMenu;
    private Button ask;


    ProgressDialog progressDialog; // ProgressDialog

    ParseFile imageFile;
    byte[] parseBitmapByte;
    Bitmap parseBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dataSet = new HashMap<String, Object>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(Detail.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mToolbar = (Toolbar)findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ask = (Button) findViewById(R.id.ask);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();

//        progressDialog = ProgressDialog.show(Detail.this, "로딩중입니다","잠시만 기다려주세요",true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("post");
        query.whereEqualTo("address", intent.getStringExtra("address"));
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object != null) {

                    mToolbar.setTitle(object.getString("spaceTitle"));
                    try {
                        imageFile = (ParseFile) object.get("image1");
                        parseBitmapByte = imageFile.getData();
                        dataSet.put("image1",parseBitmapByte);
                    } catch (NullPointerException ne) {
                        dataSet.put("image1", getByteImage());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        imageFile = (ParseFile) object.get("image2");
                        imageFile = (ParseFile) object.get("image1");
                        parseBitmapByte = imageFile.getData();
                        dataSet.put("image2", parseBitmapByte);
                    } catch (NullPointerException ne) {
                        dataSet.put("image2", getByteImage());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        imageFile = (ParseFile) object.get("image3");
                        imageFile = (ParseFile) object.get("image1");
                        parseBitmapByte = imageFile.getData();
                        dataSet.put("image3", parseBitmapByte);
                    } catch (NullPointerException ne) {
                        dataSet.put("image3", getByteImage());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

//                    title 공간이(address) 나눔 진행 중 입니다.
//                    자세한 정보는 앱에서 확인하세요
                    dataSet.put("spaceTitle", object.getString("spaceTitle"));
                    dataSet.put("phone", object.getString("phone"));
                    dataSet.put("address", object.getString("address"));
                    dataSet.put("spaceDetail", object.getString("spaceDetail"));
                    dataSet.put("maxPeople", object.getString("maxPeople"));
                    dataSet.put("price", object.getString("price"));

                    dataSet.put("spaceType", object.getString("spaceType"));
                    dataSet.put("startTime", object.getDate("StartTime"));
                    dataSet.put("endTime", object.getDate("EndTime"));

                    dataSet.put("lat",object.getNumber("lat"));
                    dataSet.put("log",object.getNumber("log"));

                    String str = object.get("option_all").toString();
                    try {
                        JSONObject jsonObject = new JSONObject(str);

                        for (int i = 0; i < OPTIONS; i++) {
                            dataSet.put("option" + i, jsonObject.getString("option" + i));
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                    ask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+dataSet.get("phone").toString()));
                            startActivity(intent);
                        }
                    });

                    //Adapter is created in the last step
                    mAdapter = new RecyclerAdapter(dataSet, mDatasetTypes,
                            getSupportFragmentManager(), getLayoutInflater());
                    mRecyclerView.setAdapter(mAdapter);

                } else {
                    Log.d("###", e.getMessage());
                }
                progressDialog.dismiss();
            }
        });
    }

    public byte[] getByteImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.basic);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        return stream.toByteArray();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        curMenu = menu;
        getMenuInflater().inflate(R.menu.detail, menu);

        // 지금 보고 있는 공간이 찜 추가 되어 있는지 아닌지 확인
        try {
            progressDialog = ProgressDialog.show(Detail.this, "로딩중입니다","잠시만 기다려주세요",true);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
            query.whereEqualTo("username", ParseUser.getCurrentUser());
            query.whereEqualTo("address", getIntent().getStringExtra("address"));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        curMenu.findItem(R.id.toggle_interest).setIcon(R.drawable.favorite_add);
                    } else {
                        curMenu.findItem(R.id.toggle_interest).setIcon(R.drawable.favorite);
                    }
                    progressDialog.dismiss();
                }
            });
        } catch (IllegalArgumentException ie) {
            Log.d("###", "로그인을 안했나보다");
            curMenu.findItem(R.id.toggle_interest).setIcon(R.drawable.favorite);
            progressDialog.dismiss();
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toggle_interest) {
            final MenuItem mItem = item;
            progressDialog = ProgressDialog.show(Detail.this, "로딩중입니다","잠시만 기다려주세요",true);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
            query.whereEqualTo("username", ParseUser.getCurrentUser());
            query.whereEqualTo("address", dataSet.get("address").toString());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    try {
                        if (e == null) {
                            object.deleteInBackground();
                            mItem.setIcon(R.drawable.favorite);
                            Toast.makeText(Detail.this, "찜 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            ParseObject objectNew = new ParseObject("Favorite");
                            objectNew.put("username", ParseUser.getCurrentUser());
                            objectNew.put("address", dataSet.get("address").toString());
                            objectNew.saveInBackground();

                            mItem.setIcon(R.drawable.favorite_add);
                            Toast.makeText(Detail.this, "찜 추가되었습니다.", Toast.LENGTH_LONG).show();

                        }
                    } catch (IllegalArgumentException ie) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(Detail.this, "로그인이 필요합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Detail.this, login.class);
                        intent.putExtra("prevActivity", dataSet.get("address").toString());
                        startActivity(intent);
                        finish();
                    }
                    progressDialog.dismiss();
                }
            });
        }else if(item.getItemId()==R.id.detail_share){
            String mySharedLink = dataSet.get("spaceTitle")+"("+dataSet.get("address")+") 공간이"+ "나눔 진행 중 입니다. 자세한 정보는 앱에서 확인하세요.";
            String mySubject = "서울공간 나눔터";
            int myRequestId = 1001;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, mySubject);
            intent.putExtra(Intent.EXTRA_TEXT, mySharedLink);

    // 공유할 대상을 고르는 리스트를 출력
            startActivityForResult(Intent.createChooser(intent, "Share Chooser Title"), myRequestId);
        }

        return super.onOptionsItemSelected(item);
    }

}