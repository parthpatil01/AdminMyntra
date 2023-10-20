package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MiddleActivity extends AppCompatActivity {

    private TextView picksingle,pickbulk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);


        init();

        picksingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MiddleActivity.this,MainAdmin.class);
                startActivity(intent);
            }
        });

        pickbulk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                try {
                    JSONArray jArray = new JSONArray(LoadJsonFromAsset());


                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject o = jArray.getJSONObject(i);
                        HashMap<String,Object> result = new HashMap<>();


                        result.put("PID",o.get("PID"));
                        result.put("category",o.get("category"));
                        result.put("date",o.get("date"));
                        result.put("description",o.get("description"));
                        result.put("image",o.get("image"));
                        result.put("name",o.get("name"));
                        result.put("price",o.get("price"));
                        result.put("time",o.get("time"));


                        firestore.collection("PRODUCTS").document().set(result).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("success");
                            }
                        });

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }






            }
        });

    }

    void init(){
        picksingle=findViewById(R.id.picksingle);
        pickbulk=findViewById(R.id.pickbulk);
    }


    public String LoadJsonFromAsset(){
        String json="";
        try{
            InputStream in=this.getAssets().open("upload.json");
            int size=in.available();
            byte[] bbuffer=new byte[size];
            in.read(bbuffer);
            in.close();
            json=new String(bbuffer,"UTF-8");

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }

}