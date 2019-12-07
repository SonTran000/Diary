package com.example.diary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary.Adapter.UltimateRecycleV;
import com.example.diary.Model.Item;
import com.example.diary.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    EditText username,email,old,newP,Renew;
    FrameLayout container;
    Button cancle,cancleReset,ResetOK;
    RelativeLayout infoC,resetC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        checkLogin();



        boomMenuButton=findViewById(R.id.userButton);

        ResetOK=findViewById(R.id.reset_OK);
        old=findViewById(R.id.reset_oldpassword);
        newP=findViewById(R.id.reset_newpassword);
        Renew=findViewById(R.id.reset_Renewpassword);
        ava=findViewById(R.id.mainProfile);
        ava1=findViewById(R.id.profile);
        username=findViewById(R.id.currentUsername);
        email=findViewById(R.id.currentEmail);
        getUserData();
        container= findViewById(R.id.HomeContainer);
        container.setBackgroundColor(Color.parseColor("#65DD4E"));
        infoC=findViewById(R.id.userInfoContainer);
        resetC=findViewById(R.id.userResetContainer);
        cancle=findViewById(R.id.closeB);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.setVisibility(View.INVISIBLE);
                infoC.setVisibility(View.INVISIBLE);
                resetC.setVisibility(View.INVISIBLE);
            }
        });
        cancleReset=findViewById(R.id.reset_Cancel);
        cancleReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.setVisibility(View.INVISIBLE);
                resetC.setVisibility(View.INVISIBLE);
                infoC.setVisibility(View.INVISIBLE);
            }
        });
        ResetOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });



            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            // When the boom-button corresponding this builder is clicked.
                            container.setVisibility(View.VISIBLE);
                            infoC.setVisibility(View.VISIBLE);
                            resetC.setVisibility(View.INVISIBLE);

                        }
                    }).normalText("Profile").normalImageRes(R.drawable.ic_profile).textSize(15);
            boomMenuButton.addBuilder(builder);


            builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {
                    // When the boom-button corresponding this builder is clicked.
                    Intent intent = new Intent(HomeActivity.this, WriteActivity.class);
                    startActivity(intent);

                }
            }).normalText("Write").normalImageRes(R.drawable.ic_brush_black_24dp).textSize(15);
            boomMenuButton.addBuilder(builder);

        builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                // When the boom-button corresponding this builder is clicked.
                yourDiary();

            }
        }).normalText("Your Diary").normalImageRes(R.drawable.ic_blur_on_black_24dp).textSize(15);
        boomMenuButton.addBuilder(builder);

        builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                // When the boom-button corresponding this builder is clicked.
                initItem();

            }
        }).normalText("All Diary").normalImageRes(R.drawable.ic_all_out_black_24dp).textSize(15);
        boomMenuButton.addBuilder(builder);


        builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                // When the boom-button corresponding this builder is clicked.
                container.setVisibility(View.VISIBLE);
                resetC.setVisibility(View.VISIBLE);
                infoC.setVisibility(View.INVISIBLE);

            }
        }).normalText("Reset Password").normalImageRes(R.drawable.ic_fingerprint_black_24dp).textSize(15);
        boomMenuButton.addBuilder(builder);


        builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                // When the boom-button corresponding this builder is clicked.
                Intent intent = new Intent(HomeActivity.this, History.class);
                startActivity(intent);

            }
        }).normalText("History").normalImageRes(R.drawable.ic_history_black_24dp).textSize(15);
        boomMenuButton.addBuilder(builder);


        builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
            @Override
            public void onBoomButtonClick(int index) {
                // When the boom-button corresponding this builder is clicked.
                FirebaseAuth.getInstance().signOut();
                checkLogin();

            }
        }).normalText("Logout").normalImageRes(R.drawable.ic_out).textSize(15);
        boomMenuButton.addBuilder(builder);


        //}

        //initItem();
        ultimateRecyclerView=findViewById(R.id.Recycle_view);
        ultimateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UltimateRecycleV(this,items,users);
        ultimateRecyclerView.setAdapter(adapter);



        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                initItem();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loss", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

    }



    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
        initItem();
    }

    private void checkLogin()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null)
        {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private void yourDiary()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Item> delItem=new ArrayList<>();
        String uid=user.getUid();
        if (uid!=null) {
            for (Item i :
                    items) {
                if (!i.getuID().equals(uid))
                    delItem.add(i);
            }
        }
        for (Item i:
             delItem) {
            items.remove(i);
        }
        adapter.notifyDataSetChanged();
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
            String uid = user.getUid();

            String userName="";
            for (User u:users) {
                if(u.getId().equals(uid))
                    userName=u.getName();
            }




            FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();

                for (UserInfo profile : u.getProviderData()) {
                    // Id of the provider (ex: google.com)
                    String providerId = profile.getProviderId();



                    // Name, email address, and profile photo Url
                    String Name = profile.getDisplayName();
                    String Email = profile.getEmail();
                    Uri photoUrl = profile.getPhotoUrl();
                    username.setText(userName);
                    //username.setText("Uranus");
                    email.setText(mail);
                    if(photoUrl!=null) {
                        Picasso.get().load(photoUrl).into(ava);
                        Picasso.get().load(photoUrl).into(ava1);
                    }
                }


        }
    }

    private void updatePassword()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (Renew.getText()!=null && newP.getText()!=null && old.getText()!=null)
            if (newP.getText().toString().equals(Renew.getText().toString())) {
                final String newPassword = newP.getText().toString();
                String oldPassword = old.getText().toString();

                // Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), oldPassword);

// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    old.setText("");
                                                    newP.setText("");
                                                    Renew.setText("");
                                                    container.setVisibility(View.INVISIBLE);
                                                    resetC.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(HomeActivity.this, "ResetSuccess", Toast.LENGTH_SHORT).show();
                                                    Log.d("updated", "User password updated.");
                                                }
                                            }
                                        });
                                Log.d("Reset", "User re-authenticated.");
                            }
                        });
            }
        else {
                Toast.makeText(HomeActivity.this, "ResetFailed", Toast.LENGTH_SHORT).show();
            }


    }

    private void initItem() {
       items.clear();

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
