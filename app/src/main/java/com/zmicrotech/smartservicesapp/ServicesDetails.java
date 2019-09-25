package com.zmicrotech.smartservicesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

public class ServicesDetails extends AppCompatActivity {

    ElegantNumberButton elegantNumberButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_details);


        elegantNumberButton = (ElegantNumberButton) findViewById(R.id.elagentNumber);


    }
}
