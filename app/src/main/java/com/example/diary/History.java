package com.example.diary;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.Adapter.HistoryRecycleView;
import com.example.diary.Adapter.UltimateRecycleV;
import com.example.diary.Model.HistoryItem;
import com.example.diary.Model.Item;
import com.example.diary.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    RecyclerView ultimateRecyclerView;
    ArrayList<HistoryItem> items=new ArrayList<>();
    ArrayList<User> users=new ArrayList<>();
    HistoryRecycleView adapter;
    BoomMenuButton boomMenuButton;
    ImageView ava,ava1;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);mDatabase = FirebaseDatabase.getInstance().getReference();
        initItem();
        boomMenuButton=findViewById(R.id.userButton);


        //Set BoomActionButton

        for (int i = 0; i < boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            finish();

                        }
                    }).normalImageRes(R.drawable.ic_assignment_return_black_24dp).normalText("Return").textSize(15);
            boomMenuButton.addBuilder(builder);
        }

        //Set RecycleView
        ultimateRecyclerView=findViewById(R.id.Recycle_view);
        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new HistoryRecycleView(this,items,users);
        ultimateRecyclerView.setAdapter(adapter);

        ava=findViewById(R.id.mainProfile);
        ava1=findViewById(R.id.profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            for (UserInfo profile : user.getProviderData()) {
                //Set Avatar

                Uri photoUrl = profile.getPhotoUrl();
                if(photoUrl!=null) {
                    Picasso.get().load(photoUrl).into(ava);
                    Picasso.get().load(photoUrl).into(ava1);
                }
            }


        }



    }

    private void initItem() {

        mDatabase.child("History").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                    if(dataSnapshot.child("changed").getValue()!=null
                            && dataSnapshot.child("time").getValue()!=null
                            && dataSnapshot.child("uID").getValue()!=null)
                        items.add(new HistoryItem(dataSnapshot.getKey()
                                ,dataSnapshot.child("uID").getValue().toString(),
                                dataSnapshot.child("changed").getValue().toString(),
                                dataSnapshot.child("time").getValue().toString()));
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
