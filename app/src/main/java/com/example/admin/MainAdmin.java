package com.example.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainAdmin extends AppCompatActivity {


    ImageView shirt,trouser,sweater,jackets,dress;
    ImageView western,sandals,shoes,watches,luggages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        shirt=findViewById(R.id.shirt);
        trouser=findViewById(R.id.trouser);
        sweater=findViewById(R.id.sweater);
        jackets=findViewById(R.id.jacket);
        dress=findViewById(R.id.dress);
        western=findViewById(R.id.western);
        sandals=findViewById(R.id.sandals);
        shoes=findViewById(R.id.shoes);
        watches=findViewById(R.id.watches);
        luggages=findViewById(R.id.luggages);

        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("shirts");

            }
        });

        trouser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("trousers");

            }
        });

        sweater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCategory("sweaters");

            }
        });

        jackets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("jackets");
            }
        });

        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("dresses");
            }
        });

        western.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("western");
            }
        });

        sandals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("sandals");
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("shoes");
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("watches");
            }
        });

        luggages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategory("luggages");
            }
        });
    }

    void openCategory(String category){
        Intent intent=new Intent(MainAdmin.this,categoryAdd.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }
}