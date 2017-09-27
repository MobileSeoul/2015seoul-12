package sgt.hansung.com.suh_gongteo;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by user on 2015-10-20.
 */

public class Write extends ActionBarActivity {
    private final int FIRST_STEP = 0, SECOND_STEP =1, THIRD_STEP =2;

    private String startD, endD ;
    private Date startDate, endDate;


    public ViewPager mPager;
    public StepPagerAdapter mAdapter;
    public List<Fragment> mItems;
    public int curPage;
    public int maxPage;

    private Button preBtn;
    private Button nextBtn;
    private Menu currentMenu;
    private JSONObject optionList = new JSONObject();



    private Uri uri=null;
    private String bitmapName = null;
    ParseObject post;
    Calendar c = Calendar.getInstance();
    int cyear = c.get(Calendar.YEAR);
    int cmonth = c.get(Calendar.MONTH);
    int cday = c.get(Calendar.DAY_OF_MONTH);

    ProgressDialog progressDialog; // ProgressDialog

    public Write(){
        startDate = new Date();
        endDate = new Date();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setTitle("공간등록");


        post = new ParseObject("post");

        mPager = (ViewPager) findViewById(R.id.step_page);
        mAdapter = new StepPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(3);
        preBtn = (Button) findViewById(R.id.pre_btn);
        nextBtn = (Button) findViewById(R.id.next_btn);


        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                curPage = position;
                if (position == 0) {
                    preBtn.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.VISIBLE);
                    currentMenu.findItem(R.id.post).setVisible(false);
                } else if (position == 2 && position == maxPage - 1) {
                    currentMenu.findItem(R.id.post).setVisible(true);
                    nextBtn.setVisibility(View.GONE);
                } else if (position > 2) {
                    if (position == maxPage - 1) {
                        nextBtn.setVisibility(View.GONE);
                        currentMenu.findItem(R.id.post).setVisible(true);
                    } else {
                        currentMenu.findItem(R.id.post).setVisible(false);
                    }
                } else {
                    preBtn.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    currentMenu.findItem(R.id.post).setVisible(false);
                }

            }
        });

    }

    public void Data_parse(){

        ParseUser user = ParseUser.getCurrentUser();
        post.put("userId", user.getUsername());

        for(int i=0;i<mItems.size();i++) {
            if (i == 0) {
                First_step tmp = (First_step) mItems.get(i);
                for ( int j=0; j<tmp.bitmap.length; j++ ) {
                    if ( tmp.bitmap[j] == null || tmp.bitmap[j] == tmp.default_image ) {
                    }else {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        tmp.bitmap[j].compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteImage = stream.toByteArray();
                        ParseFile ImageFile = new ParseFile("mainImg" + j + ".png", byteImage);
                        post.put("image" + (j + 1), ImageFile);
                    }
                }
                post.put("spaceTitle", tmp.spaceTitle.getText().toString());
                post.put("spaceDetail", tmp.spaceDetail.getText().toString());

            } else if( i == 1 ){

                Second_step tmp = (Second_step) mItems.get(i);
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                try {
                    startDate = df.parse(startD);
                    endDate = df.parse(endD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                post.put("StartTime", startDate);
                post.put("EndTime", endDate);
                post.put("maxPeople", tmp.max_people.getText().toString());
                post.put("phone",tmp.phone.getText().toString());
                post.put("spaceType",tmp.space_Type);
                post.put("price", tmp.cash.getText().toString());

                try {
                    for (int j = 0; j <tmp.checkBox.length; j++){
                        optionList.put("option" + j, tmp.checkBox[j].isChecked());
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

                post.put("option_all",optionList);

            } else if ( i == 2 ){
                Third_step tmp = (Third_step) mItems.get(THIRD_STEP);
                post.put("lat",tmp.lat);
                post.put("log",tmp.log);
                post.put("address",tmp.address);
            }
        }
        progressDialog = ProgressDialog.show(Write.this, "로딩중입니다","잠시만 기다려주세요",true);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                Third_step tmp3 = (Third_step) mItems.get(THIRD_STEP);
                Toast.makeText(Write.this, "글 등록 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Write.this, Detail.class);
                intent.putExtra("address", tmp3.address);
                startActivity(intent);
                finish();
                progressDialog.dismiss();
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_step, menu);
        currentMenu=menu;
        currentMenu.findItem(R.id.post).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.post) {

            First_step tmp1= (First_step)mItems.get(FIRST_STEP);
            Second_step tmp2= (Second_step)mItems.get(SECOND_STEP);
            Third_step tmp3= (Third_step)mItems.get(THIRD_STEP);

            int imageNullCheck_1 =0, imageNullCheck_2 = 0;
            for( int i = 0; i<tmp1.bitmap.length; i++ ) {
                if( tmp1.bitmap[i] == null ){
                    imageNullCheck_1++;
                }else if ( tmp1.bitmap[i] == tmp1.default_image ){
                    imageNullCheck_2++;
                }
            }

            if ( imageNullCheck_1 == 4  || imageNullCheck_2 == 4 || (imageNullCheck_1+imageNullCheck_2) == 4 ){
                Toast.makeText(this, "이미지를 한장 이상 입력 해주세요", Toast.LENGTH_SHORT).show();
                curPage = 0;
                mPager.setCurrentItem(curPage);
            }else if (tmp1.spaceTitle.getText().toString().equals("")) {
                Toast.makeText(this, "공간명을 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 0;
                mPager.setCurrentItem(curPage);
            }else if (tmp1.spaceDetail.getText().toString().equals("")) {
                Toast.makeText(this, "상세설명을 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 0;
                mPager.setCurrentItem(curPage);
            }else if (tmp2.cash.getText().toString().equals("")) {
                Toast.makeText(this, "가격을 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 1;
                mPager.setCurrentItem(curPage);
            }else if (tmp2.phone.getText().toString().equals("")) {
                Toast.makeText(this, "담당자 연락처를 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 1;
                mPager.setCurrentItem(curPage);
            }else if (tmp2.max_people.getText().toString().equals("")) {
                Toast.makeText(this, "최대 수용 인원을 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 1;
                mPager.setCurrentItem(curPage);
            }else if (tmp2.startDate.getText().toString().equals("")) {
                Toast.makeText(this, "시작 날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 1;
                mPager.setCurrentItem(curPage);
            }else if (tmp2.endDate.getText().toString().equals("")) {
                Toast.makeText(this, "종료 날짜를 입력해주세요", Toast.LENGTH_SHORT).show();
                curPage = 1;
                mPager.setCurrentItem(curPage);
            }else if (tmp3.address.equals("") ){
                Toast.makeText(this, "원하는 지역을 선택해주세요", Toast.LENGTH_SHORT).show();
            }else {
                Data_parse();
            }


            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void next_btn(View view) {
        progressDialog = ProgressDialog.show(Write.this, "로딩중입니다","잠시만 기다려주세요",true);
        if(curPage!=2) {
            curPage = curPage + 1;
            mPager.setCurrentItem(curPage);
        }
        progressDialog.dismiss();
    }

    public void pre_btn(View view) {
        progressDialog = ProgressDialog.show(Write.this, "로딩중입니다","잠시만 기다려주세요",true);
        if(curPage!=0) {
            curPage = curPage - 1;
            mPager.setCurrentItem(curPage);
        }
        progressDialog.dismiss();
    }

    public void StartDateSelect(View view){
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Second_step tmp = (Second_step) mItems.get(SECOND_STEP);
                try {
                    startD = ""+year+(month+1)+day;
                    tmp.startDate.setText(year+"년"+(month+1)+"월"+day+"일");
                }catch(Exception e){
                    StartDateSelect(view);
                }
            }
        };
        DatePickerDialog alert = new DatePickerDialog(this,  mDateSetListener,
                cyear, cmonth, cday);
        alert.show();
    }

    public void EndDateSelect(View view){
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Second_step tmp = (Second_step) mItems.get(SECOND_STEP);
                try {
                    endD = ""+year+(month+1)+day;
                    tmp.endDate.setText(year + "년" + (month + 1) + "월" + day + "일");
                }catch(Exception e){
                    EndDateSelect(view);
                }
            }
        };
        DatePickerDialog alert = new DatePickerDialog(this,  mDateSetListener,
                cyear, cmonth, cday);
        alert.show();
    }

    public class StepPagerAdapter extends FragmentPagerAdapter {

        public StepPagerAdapter(FragmentManager fm) {
            super(fm);
            mItems=new ArrayList<Fragment>();
            init();
        }
        public void init(){
            mItems.add(new First_step());
            mItems.add(new Second_step());
            mItems.add(new Third_step());
            maxPage=mItems.size();
        }

        public void addFragment(Fragment f){
            mItems.add(f);
            mAdapter.notifyDataSetChanged();
            maxPage=mItems.size();
        }
        @Override
        public Fragment getItem(int position) {
            if(position==0) {
                nextBtn.setVisibility(View.VISIBLE);
            }
            return mItems.get(position);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }
        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }
    }
}