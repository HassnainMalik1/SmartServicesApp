package com.zmicrotech.smartservicesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zmicrotech.smartservicesapp.Model.UserInfo;

import org.w3c.dom.Text;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference userRef;
    MaterialEditText userName, userEmail, userPassword, userCity, userAddress;
    RadioGroup accountTypeRG;
    String accountType;

    Button registrationBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        userName = (MaterialEditText) findViewById(R.id.registrationUserName);
        userEmail = (MaterialEditText) findViewById(R.id.registrationUserEmail);
        userPassword = (MaterialEditText) findViewById(R.id.registrationUserPassword);
        userCity = (MaterialEditText) findViewById(R.id.registrationUserCity);
        userAddress = (MaterialEditText) findViewById(R.id.registrationUserAddress);

        accountTypeRG = (RadioGroup) findViewById(R.id.registrationACTypeRG);

        accountTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.buyerRB){
                        accountType = "buyer";
                    }else if(checkedId == R.id.sellerRB){
                        accountType = "seller";
                    }
            }
        });

        registrationBtn = (Button) findViewById(R.id.registrationBtn);

        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(userName.getText().toString().trim())) {
                    userName.setError("User name is requried!");
                    return;
                }

                if(TextUtils.isEmpty(userEmail.getText().toString().trim())){
                    userEmail.setError("User email is requried!");
                    return;
                }

                if(TextUtils.isEmpty(userPassword.getText().toString().trim())){
                    userPassword.setError("User password is requried!");
                    return;
                }

                if(TextUtils.isEmpty(userAddress.getText().toString().trim())){
                    userAddress.setError("User address is requried!");
                    return;
                }

                if(TextUtils.isEmpty(userCity.getText().toString().trim())){
                    userCity.setError("User city is requried!");
                    return;
                }
                if(TextUtils.isEmpty(accountType)){
                    Toast.makeText(RegistrationActivity.this, "Kindly select account type!", Toast.LENGTH_SHORT).show();
                   return;
                }

                final UserInfo currentUser = new UserInfo(userName.getText().toString(), userEmail.getText().toString(),
                        userPassword.getText().toString(), userCity.getText().toString(), userAddress.getText().toString(), accountType );

                mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isComplete()){
                                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentUser);
                                    Toast.makeText(RegistrationActivity.this, "User Registered successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Failed to register user!", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }
}
