package sgt.hansung.com.suh_gongteo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;


/**
 * Created by user on 2015-10-21.
 */

//이미지, 제목, 상세설명
public class Second_step extends Fragment {



    CheckBox[] checkBox = new CheckBox[12];;
    TextView startDate, endDate;
    public RadioButton office;
    RadioGroup spaceType;
    String space_Type;
    MaterialEditText cash, phone, max_people;

    Calendar c;
    int cyear, cmonth, cday;

    public Second_step() {
        c = Calendar.getInstance();
        cyear = c.get(Calendar.YEAR);
        cmonth = c.get(Calendar.MONTH);
        cday = c.get(Calendar.DAY_OF_MONTH);
        space_Type = "office";

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_second_step, container, false);
        startDate = (TextView) linearLayout.findViewById(R.id.startDate);
        endDate = (TextView) linearLayout.findViewById(R.id.endDate);
        cash = (MaterialEditText) linearLayout.findViewById(R.id.cash);
        max_people = (MaterialEditText) linearLayout.findViewById(R.id.max_people);
        office = (RadioButton) linearLayout.findViewById(R.id.office);
        office.setChecked(true);


        phone = (MaterialEditText)linearLayout.findViewById(R.id.phone);
        checkBox[0] = (CheckBox) linearLayout.findViewById(R.id.checkBox0);
        checkBox[1] = (CheckBox) linearLayout.findViewById(R.id.checkBox1);
        checkBox[2] = (CheckBox) linearLayout.findViewById(R.id.checkBox2);
        checkBox[3] = (CheckBox) linearLayout.findViewById(R.id.checkBox3);
        checkBox[4] = (CheckBox) linearLayout.findViewById(R.id.checkBox4);
        checkBox[5] = (CheckBox) linearLayout.findViewById(R.id.checkBox5);
        checkBox[6] = (CheckBox) linearLayout.findViewById(R.id.checkBox6);
        checkBox[7] = (CheckBox) linearLayout.findViewById(R.id.checkBox7);
        checkBox[8] = (CheckBox) linearLayout.findViewById(R.id.checkBox8);
        checkBox[9] = (CheckBox) linearLayout.findViewById(R.id.checkBox9);
        checkBox[10] = (CheckBox) linearLayout.findViewById(R.id.checkBox10);
        checkBox[11] = (CheckBox) linearLayout.findViewById(R.id.checkBox11);



        spaceType = (RadioGroup) linearLayout.findViewById(R.id.spaceType);
        spaceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(group == spaceType ){
                    if (checkedId == R.id.office ) {
                        space_Type = "사무실";
                    }else if(checkedId == R.id.etc) {
                        space_Type = "기타";
                    }else if (checkedId == R.id.cafe ){
                        space_Type = "카페";
                    }else if (checkedId == R.id.concert ){
                        space_Type = "공연장";
                    }else if (checkedId == R.id.warehouse ){
                        space_Type = "창고";
                    }else if (checkedId == R.id.sleeping ){
                        space_Type = "숙박시설";
                    }else if (checkedId == R.id.ground ){
                        space_Type = "공터";
                    }else if (checkedId == R.id.public_office ){
                        space_Type = "공공시설";
                    }
                }
            }
        });

//        office = (RadioButton)linearLayout.findViewById(R.id.office);
//        cafe = (RadioButton) linearLayout.findViewById(R.id.cafe);
//        concert = (RadioButton) linearLayout.findViewById(R.id.concert);
//        warehouse = (RadioButton) linearLayout.findViewById(R.id.warehouse);
//        sleeping = (RadioButton) linearLayout.findViewById(R.id.sleeping);
//        ground = (RadioButton) linearLayout.findViewById(R.id.ground);
//        public_office = (RadioButton) linearLayout.findViewById(R.id.public_office);
//        etc = (RadioButton) linearLayout.findViewById(R.id.etc);

        //type1.setChecked(true);


        return linearLayout;
    }
}