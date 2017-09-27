package sgt.hansung.com.suh_gongteo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Management extends AppCompatActivity {

    ListView managementListView;
    ArrayList<manageListData> manDataArr;
    manageListAdapter manListAdapter;

    ParseFile imageFile;
    byte[] parseBitmapByte;
    Bitmap parseBitmap;

    ParseUser user;
    ParseQuery<ParseObject> query;

    String parseTitle;
    String parseStartTime;
    String parseEndTime;
    String availableTime;
    String parsePrice;
    String parseObjectId;
    String parseAddress;
    SimpleDateFormat format;

    ProgressDialog progressDialog; // ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        setTitle("공간관리");
        user = ParseUser.getCurrentUser();
        query = ParseQuery.getQuery("post");
        format = new SimpleDateFormat("MM월 dd일 HH:mm");

        managementListView = (ListView) findViewById(R.id.manageListView);
        manDataArr = new ArrayList<manageListData>();

        manListAdapter = new manageListAdapter(this, R.layout.management_list_item, manDataArr);
        managementListView.setAdapter(manListAdapter);


        parseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_cancel);


        progressDialog = ProgressDialog.show(Management.this, "로딩중입니다", "잠시만 기다려주세요", true);
        try{
            viewRegistrationtList(user.getUsername());
        }catch (NullPointerException e){
            progressDialog.dismiss();
            ImageView empty = (ImageView)findViewById(R.id.empty_space);
            empty.setVisibility(View.VISIBLE);
        }

        manListAdapter.notifyDataSetChanged();

        managementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Management.this, Detail.class);
                intent.putExtra("address",manDataArr.get(position).manAddress);
                startActivity(intent);
            }
        });

    }

    public void viewRegistrationtList(String userId) {

        query.whereEqualTo("userId", userId);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> registrationList, ParseException e) {
                if(registrationList.size()==0){
                    ImageView empty = (ImageView)findViewById(R.id.empty_space);
                    empty.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < registrationList.size(); i++) {
                    imageFile = (ParseFile) registrationList.get(i).get("image1");
                    try {
                        parseBitmapByte = imageFile.getData();
                        parseBitmap = BitmapFactory.decodeByteArray(parseBitmapByte, 0, parseBitmapByte.length);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        parseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_media_play);
                    } catch (NullPointerException e2) {
                        parseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_parent_rounded_corner);
                    }
                    parseAddress = registrationList.get(i).getString("address");
                    parseTitle = registrationList.get(i).getString("spaceTitle");
                    parseStartTime = format.format(registrationList.get(i).getDate("StartTime"));
                    parseEndTime = format.format(registrationList.get(i).getDate("EndTime"));
                    availableTime = parseStartTime + " ~ " + parseEndTime;
                    parsePrice = registrationList.get(i).getString("price");
                    parseObjectId = registrationList.get(i).getObjectId();
                    if (parsePrice == null || parsePrice.equals("")) {
                        parsePrice = "무료";
                    } else {
                        parsePrice += parsePrice + "원";
                    }

                    manDataArr.add(new manageListData(parseBitmap, parseTitle, availableTime, parsePrice, parseObjectId,parseAddress));
                }

                progressDialog.dismiss();
                manListAdapter.notifyDataSetChanged();

            }
        });
    }

    public void manageDelete(View view) {
        progressDialog = ProgressDialog.show(Management.this, "로딩중입니다", "잠시만 기다려주세요", true);
        final int position = managementListView.getPositionForView((View) view.getParent());

        query.whereEqualTo("objectId", manDataArr.get(position).manObjectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if(manDataArr.size()<=1){
                    ImageView empty = (ImageView)findViewById(R.id.empty_space);
                    empty.setVisibility(View.VISIBLE);
                }
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Favorite");
                        query2.whereEqualTo("address", object.getString("address"));
                        query2.whereEqualTo("username", ParseUser.getCurrentUser());
                        query2.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                for (int i = 0; i < list.size(); i++) {

                                    list.get(i).deleteInBackground();
                                }
                                manDataArr.remove(position);
                                manListAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }
}

