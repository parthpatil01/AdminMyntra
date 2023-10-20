package com.example.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class categoryAdd extends AppCompatActivity {

    private String category,saveCurrentDate,saveCurrentTime,uniqueRandomkey;
    private ImageView camera;
    private EditText name,description,price;
    private Button addProduct;

    private static final int galleryPick=1;
    private Uri imageUri;

    private String productName,productPrice,productDescription,dowloadImageUrl;

    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private FirebaseFirestore firestore;

    private ProgressDialog loadingbar;

    ImageView plus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);

        productImageRef= FirebaseStorage.getInstance().getReference().child("Product image");
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        firestore=FirebaseFirestore.getInstance();

        loadingbar= new ProgressDialog(this);

        category= getIntent().getExtras().get("category").toString();
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();

        camera= findViewById(R.id.camera);
        name=findViewById(R.id.pro_name);
        description=findViewById(R.id.description);
        price=findViewById(R.id.price);
        addProduct=findViewById(R.id.addproduct);
        plus=findViewById(R.id.plus);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProduct();
            }
        });

    }

    private void validateProduct() {

        productName=name.getText().toString();
        productDescription=description.getText().toString();
        productPrice=price.getText().toString();

        if(imageUri==null){
            Toast.makeText(this, "Product image not found", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(productName)){
            Toast.makeText(this, "Product name is empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(productDescription)){
            Toast.makeText(this, "Product description is empty", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(productPrice)){
            Toast.makeText(this, "Product price is empty", Toast.LENGTH_SHORT).show();
        }else{
            storeProductInformation();
        }

    }

    private void storeProductInformation() {

        loadingbar.setTitle("Adding product");
        loadingbar.setMessage("please wait..");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat current=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=current.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currenttime.format(calendar.getTime());

        uniqueRandomkey=saveCurrentDate+saveCurrentTime;

        StorageReference filepath=productImageRef.child(imageUri.getLastPathSegment()+uniqueRandomkey+".jpg");
        final UploadTask uploadtask=filepath.putFile(imageUri);

        uploadtask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String msg=e.toString();
                Toast.makeText(categoryAdd.this, "Error: "+msg, Toast.LENGTH_SHORT).show();

                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(categoryAdd.this, "Image uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask=uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        dowloadImageUrl=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()) {
                            dowloadImageUrl=task.getResult().toString();

                            Toast.makeText(categoryAdd.this, "Product Image url", Toast.LENGTH_SHORT).show();
                            saveProductInfotoDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfotoDatabase() {

        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("PID",uniqueRandomkey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("name",productName);
        productMap.put("description",productDescription);
        productMap.put("image",dowloadImageUrl);
        productMap.put("category",category);
        productMap.put("price",productPrice);

        firestore.collection("PRODUCTS").document(uniqueRandomkey).set(productMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(categoryAdd.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(categoryAdd.this, "Product is added to database Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(categoryAdd.this,MainAdmin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else {
                    String message=task.getException().toString();
                    Toast.makeText(categoryAdd.this, message, Toast.LENGTH_SHORT).show();
                }
                loadingbar.dismiss();
            }
        });

//        productRef.child(uniqueRandomkey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    loadingbar.dismiss();
//                    Toast.makeText(categoryAdd.this, "Product is added to database Successfully", Toast.LENGTH_SHORT).show();
//
//                    Intent intent=new Intent(categoryAdd.this,MainAdmin.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//
//                }else {
//                    loadingbar.dismiss();
//                    String message=task.getException().toString();
//                    Toast.makeText(categoryAdd.this, message, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }


    private void openGallery() {

        Intent galleryintent= new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,galleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            plus.setVisibility(View.GONE);
            imageUri= data.getData();
            camera.setImageURI(imageUri);
        }
    }
}
