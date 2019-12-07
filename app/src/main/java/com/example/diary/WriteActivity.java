package com.example.diary;

import android.os.Bundle;


import com.flask.colorpicker.ColorPickerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;



public class WriteActivity extends AppCompatActivity {

    FrameLayout container;
    Button close,back,ok;
    DatePicker date;
    TimePicker time;
    ColorPickerView color;
    EditText title,content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        title=findViewById(R.id.in_title);
        content=findViewById(R.id.in_content);

        back=findViewById(R.id.goBack);
        ok=findViewById(R.id.post);
        container=findViewById(R.id.frag_container);
        close=findViewById(R.id.closeAll);
        date=findViewById(R.id.getDate);
        time=findViewById(R.id.getTime);
        color=findViewById(R.id.getColor);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.setVisibility(View.INVISIBLE);
                date.setVisibility(View.INVISIBLE);
                time.setVisibility(View.INVISIBLE);
                color.setVisibility(View.INVISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post();addHistory();finish();
            }
        });



        BottomNavigationView bnv= findViewById(R.id.nav_view);
        bnv.setOnNavigationItemSelectedListener(navL);
    }

    private void post() {

        String GTime=date.getDayOfMonth()+"-"+date.getMonth()+"-"+date.getYear()+" "+time.getHour()+":"+time.getMinute();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        Date d = null;
        try {
            d = (Date)formatter.parse(GTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String g=d.getTime();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uID=user.getUid();

       if(title.getText()!=null
       && content.getText()!=null
       && d!=null && Integer.toHexString(color.getSelectedColor())!=null
               && uID!=null){
           Map DiaryMap = new HashMap<>();
           DiaryMap.put("color","#"+Integer.toHexString(color.getSelectedColor()));
           DiaryMap.put("content",content.getText().toString());
           DiaryMap.put("time",d.getTime());
           DiaryMap.put("title",title.getText().toString());
           DiaryMap.put("uID",uID);

           FirebaseDatabase.getInstance().getReference().child("Diary").push().updateChildren(DiaryMap);
       }
       else Toast.makeText(this,"Thieu noi dung",Toast.LENGTH_SHORT);


    }

    private void addHistory() {


        String GTime=date.getDayOfMonth()+"-"+date.getMonth()+"-"+date.getYear()+" "+time.getHour()+":"+time.getMinute();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        Date d = null;
        try {
            d = (Date)formatter.parse(GTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //String g=d.getTime();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uID=user.getUid();

        if(title.getText()!=null
                && content.getText()!=null
                && d!=null
                && uID!=null){
            Map DiaryMap = new HashMap<>();
            DiaryMap.put("time",d.getTime());
            DiaryMap.put("changed",title.getText().toString());
            DiaryMap.put("uID",uID);

            FirebaseDatabase.getInstance().getReference().child("History").push().updateChildren(DiaryMap);
        }
        else Toast.makeText(this,"Thieu noi dung",Toast.LENGTH_SHORT);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navL = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            container.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
            time.setVisibility(View.INVISIBLE);
            color.setVisibility(View.INVISIBLE);
            String fragName=null;
            switch (menuItem.getItemId()){
                case R.id.nav_date:
                    //selectedFrag=new DateFragment();
                    container.setVisibility(View.VISIBLE);
                    date.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_timer:
                    //selectedFrag=new TimeFragment();
                    container.setVisibility(View.VISIBLE);
                    time.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_BColor:
                    //selectedFrag=new ColorFragment();
                    container.setVisibility(View.VISIBLE);
                    color.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_trash:
                    //selectedFrag=new TrashFragment();
                    break;
            }
            //getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,selectedFrag).commit();
            return true;
        }
    };

}
