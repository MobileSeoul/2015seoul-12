package sgt.hansung.com.suh_gongteo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class SearchFilter extends ActionBarActivity {
    private final int BUTTON_COUNT = 3;
    private View[] mButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        ToggleButton button1 = (ToggleButton) findViewById(R.id.button1);
        ToggleButton button2 = (ToggleButton) findViewById(R.id.button2);
        ToggleButton button3 = (ToggleButton) findViewById(R.id.button3);
/*
        mButtons = new ToggleButton[BUTTON_COUNT];

        final String format = "btn_%d";
        for (int i = 0; i < BUTTON_COUNT; i++) {
            int id = getResources().getIdentifier(String.format(format, i), "id", getPackageName());
            mButtons[i] = findViewById(id);
            mButtons[i].setOnClickListener(this);
        }
*/
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
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
