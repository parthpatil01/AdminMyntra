package com.example.admin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.admin.Models.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private EditText usernameTV,passwordTV;
    private Button loginButton;
    private ProgressBar progressCircle;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataFields();
            }
        });

    }

    private void init() {

        usernameTV = findViewById(R.id.username);
        passwordTV = findViewById(R.id.password);
        loginButton = findViewById(R.id.signin);
        progressCircle = findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();

    }



    private void checkDataFields() {
        String username = usernameTV.getText().toString();
        String password = passwordTV.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Credentials can't be empty", Toast.LENGTH_SHORT).show();

        }else if(password.length()<10){
            Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        }else {
            loginAuth(username,password);
        }
    }


//    private void allowLogin(String username, String password) {
//
//        final DatabaseReference rootRef;
//        rootRef= FirebaseDatabase.getInstance().getReference();
//
//        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if(snapshot.child(parentDbname).child(username).exists()){
//
//                    Admin user = snapshot.child(parentDbname).child(username).getValue(Admin.class);
//
//
//                        if(user.getPassword().equals(password)){
//
//                            loadingbar.dismiss();
//                            Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
//
//                            Intent intent=new Intent(MainActivity.this,MainAdmin.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
//                            startActivity(intent);
//
//                        }else{
//                            loadingbar.dismiss();
//                            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
//                        }
//
//
//
//
//
//                }else{
//                    loadingbar.dismiss();
//                    Toast.makeText(MainActivity.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void loginAuth(String username, String password) {

        progressCircle.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        loginButton.setAlpha(0.5f);


        mAuth.signInWithEmailAndPassword(username, password).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressCircle.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                loginButton.setAlpha(1f);
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(MainActivity.this,MiddleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);


                } else {
                    progressCircle.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1f);
                }

            }
        });


    }


}