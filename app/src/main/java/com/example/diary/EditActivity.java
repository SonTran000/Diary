package com.example.diary;

import android.content.Intent;
import android.os.Bundle;
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

import com.flask.colorpicker.ColorPickerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    FrameLayout container;
    Button close,back,ok;
    DatePicker date;
    TimePicker time;
    ColorPickerView color;
    EditText title,content;
    String key="",oldTitle="",oldContent="";


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

        Intent intent=this.getIntent();
        key=intent.getStringExtra("key");
        oldTitle=intent.getStringExtra("title");
        oldContent=intent.getStringExtra("content");

        title.setText(oldTitle);
        content.setText(oldContent);

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

    //Add History to database
    private void addHistory() {
        String changedStatus="";
        if(!oldTitle.equals(title.getText().toString()))
            changedStatus+=" change title from ["+oldTitle+"] to ["+title.getText().toString()+"]";
        if(!oldContent.equals(content.getText().toString()))
            changedStatus+=" change content from ["+oldContent+"] to ["+content.getText().toString()+"]";

        String GTime=date.getDayOfMonth()+"-"+date.getMonth()+"-"+date.getYear()+" "+time.getHour()+":"+time.getMinute();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        Date d = null;
        try {
            d = (Date)formatter.parse(GTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uID=user.getUid();

        if(title.getText()!=null
                && content.getText()!=null
                && d!=null
                && uID!=null){
            Map DiaryMap = new HashMap<>();
            DiaryMap.put("time",d.getTime());
            DiaryMap.put("changed",changedStatus);
            DiaryMap.put("uID",uID);

            FirebaseDatabase.getInstance().getReference().child("History").push().updateChildren(DiaryMap);
        }
        else Toast.makeText(this,"Thieu noi dung",Toast.LENGTH_SHORT);


    }

    //Update diary  to database
    private void post() {

        String GTime=date.getDayOfMonth()+"-"+date.getMonth()+"-"+date.getYear()+" "+time.getHour()+":"+time.getMinute();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        Date d = null;
        try {
            d = (Date)formatter.parse(GTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

            FirebaseDatabase.getInstance().getReference().child("Diary").child(key).updateChildren(DiaryMap);
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
