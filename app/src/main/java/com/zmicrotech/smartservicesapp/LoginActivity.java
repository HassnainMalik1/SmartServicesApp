package com.zmicrotech.smartservicesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zmicrotech.smartservicesapp.Utils.Common;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText userEmailEt, userPasswordEt;
    Button loginButton;
    FirebaseAuth mAuth;
    CheckBox rememberCheckBox;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initialized paper db
        Paper.init(this);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        userEmailEt = (MaterialEditText) findViewById(R.id.loginUserName);
        userPasswordEt = (MaterialEditText) findViewById(R.id.loginUserPassword);
        rememberCheckBox = (CheckBox) findViewById(R.id.rememberCheckBox);

        loginButton = (Button) findViewById(R.id.loginButton);


        autoLogin();


        progressDialog.setMessage("Please wait...");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(userEmailEt.getText().toString().trim())){
                    userEmailEt.setError("User email is required!");
                    return;
                }
                if(TextUtils.isEmpty(userPasswordEt.getText().toString().trim())){
                    userPasswordEt.setError("User password is required!");
                    return;
                }
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(userEmailEt.getText().toString(), userPasswordEt.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            if(rememberCheckBox.isChecked()){
                                Paper.book().write(Common.currentUserEmail, userEmailEt.getText().toString().trim());
                                Paper.book().write(Common.currentUserPassword, userPasswordEt.getText().toString().trim());
                            }

                            Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }
                });
            }
        });


    }

    private void autoLogin() {
        progressDialog.show();
        String userEmail = Paper.book().read(Common.currentUserEmail, "");
        String userPassword = Paper.book().read(Common.currentUserPassword, "");
        if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Intent homeIntent = new Intent(getApplicationContext(), Home.class);
                        startActivity(homeIntent);
                        finish();
                    }
                }
            });
        }
    }
}
