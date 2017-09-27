package sgt.hansung.com.suh_gongteo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyInterest extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerInterestAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ParseObject> space;
    ProgressDialog progressDialog; // ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interest);
        setTitle("찜한 목록");

        space = new ArrayList<ParseObject>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_interest);
        mLayoutManager = new LinearLayoutManager(MyInterest.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = ProgressDialog.show(MyInterest.this, "로딩중입니다","잠시만 기다려주세요",true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorite");
        query.whereEqualTo("username", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objectList, ParseException e1) {
                if (e1 == null) {
                    if(objectList.size()==0){
                        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_interest);
                        mRecyclerView.setBackgroundResource(R.drawable.emptyinterest);
                    }
                    for (final ParseObject obj : objectList) {
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("post");
                        query2.whereEqualTo("address", obj.getString("address").toString());
                        query2.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    space.add(object);
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("###e", e.getMessage());
                                }
                            }
                        });
                    }
                    mAdapter = new RecyclerInterestAdapter(space, getLayoutInflater());
                    mRecyclerView.setAdapter(mAdapter);

                } else {
                    Log.d("###e1", e1.getMessage());
                    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_interest);
                    mRecyclerView.setBackgroundResource(R.drawable.emptyinterest);
                }

                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();

    }
}