package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    Button btn_login, btn_logout,b_loginBox,b_registerBox,cancle,cancle1,inLogin,inRegister;
    TextView text;
    ImageView image;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;
    FrameLayout loginContainer;
    EditText inEMail,inPass,createEmail,createPass,createRePass,create_name;
    String nickNAme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
            updateUI(currentUser);

        inEMail = findViewById(R.id.in_email);
        inPass = findViewById(R.id.in_password);
        createEmail = findViewById(R.id.create_email);
        createPass = findViewById(R.id.create_password);
        createRePass = findViewById(R.id.create_REpassword);
        create_name = findViewById(R.id.create_username);

        btn_login = findViewById(R.id.login_button);
        btn_logout = findViewById(R.id.logout_button);

        loginContainer=findViewById(R.id.login_container);

        b_loginBox=findViewById(R.id.open_loginBox);
        b_registerBox=findViewById(R.id.open_registerBox);


        b_loginBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout box=findViewById(R.id.login_layout);
                loginContainer.setVisibility(View.VISIBLE);
                box.setVisibility(View.VISIBLE);
            }
        });
        b_registerBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout box=findViewById(R.id.create_layout);
                loginContainer.setVisibility(View.VISIBLE);
                box.setVisibility(View.VISIBLE);
            }
        });
        cancle=findViewById(R.id.in_cancel);
        cancle1=findViewById(R.id.create_cancel);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout box=findViewById(R.id.create_layout);

                loginContainer.setVisibility(View.INVISIBLE);
                box.setVisibility(View.INVISIBLE);
                box=findViewById(R.id.login_layout);
                box.setVisibility(View.INVISIBLE);
            }
        });
        cancle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout box=findViewById(R.id.create_layout);
                loginContainer.setVisibility(View.INVISIBLE);
                box.setVisibility(View.INVISIBLE);
                box=findViewById(R.id.login_layout);
                box.setVisibility(View.INVISIBLE);
            }
        });

        inLogin=findViewById(R.id.in_login);
        inRegister=findViewById(R.id.create_login);

        inLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        inRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        text = findViewById(R.id.fb_text);
        image = findViewById(R.id.fb_img);
        progressBar = findViewById(R.id.progress_circular);





    //}}



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.SignInGoogle();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.Logout();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
        Intent intent = new Intent(MainActivity.this, History.class);
        startActivity(intent);
    }

    private void loginUser() {
        String email=inEMail.getText().toString();
        String password=inPass.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void createUser() {
        nickNAme=create_name.getText().toString();
        String email=createEmail.getText().toString();
        String password=createPass.getText().toString();
        String REpassword=createRePass.getText().toString();
        if(password.equals(REpassword))
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            addToDatabase(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }
                });
    }

    private void addToDatabase(FirebaseUser u) {
        final DatabaseReference addUser=FirebaseDatabase.getInstance().getReference().child("User").child(u.getUid());
        final FirebaseUser user=u;
        addUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    Map<String,Object> userMap=new HashMap<>();
                    userMap.put("email",user.getEmail());
                    userMap.put("UserName",nickNAme);
                    userMap.put("avatar",user.getPhotoUrl());
                    addUser.updateChildren(userMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);

                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            MainActivity.this.updateUI(user);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);

                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            MainActivity.this.updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        /*if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());

            text.append("Info : \n");
            text.append(name + "\n");
            text.append(email);
            //Picasso.get(MainActivity.this).load(photo).into(image);
            Picasso.get().load(photo).into(image);
            btn_logout.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.INVISIBLE);
        } else {
            text.setText("Firebase Login \n");
            //Picasso.with(LoginActivity.this).load(R.drawable.ic_firebase_logo).into(image);
            Picasso.get().load(R.drawable.firebase_logo).into(image);
            btn_logout.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.VISIBLE);
        }*/
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MainActivity.this.updateUI(null);
                    }
                });
    }
}