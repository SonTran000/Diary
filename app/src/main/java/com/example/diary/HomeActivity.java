package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary.Adapter.UltimateRecycleV;
import com.example.diary.Model.Item;
import com.example.diary.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    RecyclerView ultimateRecyclerView;
    ArrayList<Item> items=new ArrayList<>();
    ArrayList<User> users=new ArrayList<>();
    UltimateRecycleV adapter;
    BoomMenuButton boomMenuButton;
    private DatabaseReference mDatabase;
    String DiaryID;
    ImageView ava,ava1;
    EditText username,email;
    FrameLayout container;
    Button cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initItem();
        boomMenuButton=findViewById(R.id.userButton);

        ava=findViewById(R.id.mainProfile);
        ava1=findViewById(R.id.profile);
        username=findViewById(R.id.currentUsername);
        email=findViewById(R.id.currentEmail);
        getUserData();
        container= findViewById(R.id.HomeContainer);
        container.setBackgroundColor(Color.parseColor("#65DD4E"));
        cancle=findViewById(R.id.closeB);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.setVisibility(View.INVISIBLE);
            }
        });


            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            container.setVisibility(View.VISIBLE);
                            Toast.makeText(HomeActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                        }
                    }).normalText("Profile").normalImageRes(R.drawable.ic_profile);
            boomMenuButton.addBuilder(builder);
            builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    // When the boom-button corresponding this builder is clicked.
                    Toast.makeText(HomeActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                }
            }).normalText("Write").normalImageRes(R.drawable.ic_brush_black_24dp);
            boomMenuButton.addBuilder(builder);
        builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                // When the boom-button corresponding this builder is clicked.
                Toast.makeText(HomeActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
            }
        }).normalText("Logout").normalImageRes(R.drawable.ic_out);
        boomMenuButton.addBuilder(builder);
        //}

        ultimateRecyclerView=findViewById(R.id.Recycle_view);
        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UltimateRecycleV(this,items,users);
        ultimateRecyclerView.setAdapter(adapter);



    }

    private void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String mail = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
           //boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
           // String uid = user.getUid();

            //for (User u:users) {
              //  if(u.getId().equals(uid))
                //    name=u.getName();
            //}
            username.setText(name);
            username.setText("Uranus");
            email.setText(mail);
        }
    }

    private void initItem() {

       mDatabase.child("Diary").addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               if(dataSnapshot.exists())
                   if(dataSnapshot.child("title").getValue()!=null
                   && dataSnapshot.child("content").getValue()!=null
                   && dataSnapshot.child("color").getValue()!=null
                   && dataSnapshot.child("time").getValue()!=null
                   && dataSnapshot.child("uID").getValue()!=null)
                   items.add(new Item(dataSnapshot.getKey()
                           ,dataSnapshot.child("title").getValue().toString(),
                           dataSnapshot.child("content").getValue().toString(),
                           dataSnapshot.child("color").getValue().toString(),
                           dataSnapshot.child("time").getValue().toString(),
                           dataSnapshot.child("uID").getValue().toString()));
                   adapter.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

        mDatabase.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                    if(dataSnapshot.child("UserName").getValue()!=null
                            && dataSnapshot.child("email").getValue()!=null
                            )
                        users.add(new User(dataSnapshot.getKey()
                                ,dataSnapshot.child("UserName").getValue().toString(),
                                dataSnapshot.child("email").getValue().toString()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
