package sgt.hansung.com.suh_gongteo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Search extends ActionBarActivity {
    private SpaceEnroll spaceEnroll = new SpaceEnroll();
    EditText searchWord;
    Button search;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchWord = (EditText) findViewById(R.id.searchWord);
        search = (Button) findViewById(R.id.search);
        test = (TextView) findViewById(R.id.test);
    }

    public void search(View view){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("post");
        //query.whereEqualTo("spaceTitle", searchWord.getText().toString()); //searchWord = 검색창에 입력한 단어
        //query.whereStartsWith("address", searchWord.getText().toString());
        query.whereMatches("address", searchWord.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {

//                        String spaceTitle = (parseObjects.get(i).getString("spaceTitle"));
//                        String address = (parseObjects.get(i).getString("address"));
//                        JSONObject obj = parseObjects.get(i).getJSONObject("option_all");
//                        String str = null;
//                        try {
//                            str = obj.get("option1").toString();
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
//                        test.setText(spaceTitle + address + str);

                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
