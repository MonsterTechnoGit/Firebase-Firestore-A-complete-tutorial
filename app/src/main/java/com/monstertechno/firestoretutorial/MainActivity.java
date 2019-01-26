package com.monstertechno.firestoretutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.monstertechno.firestoretutorial.authentication.LoginActivity;
import com.monstertechno.firestoretutorial.authentication.StoreUserData;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String curent_user_id;
    private Button signout, storedata;

    FirebaseFirestore firebaseFirestore;

    ImageView userImage;
    TextView userName,userPhone,userAddress;
    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();

        if (curent_user_id == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        curent_user_id = mAuth.getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

        userImage = findViewById(R.id.user_image);
        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);
        userAddress = findViewById(R.id.user_address);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Wait, We are checking...");
        progressDialog.show();
        

        firebaseFirestore.collection("Users").document(curent_user_id).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            if(task.getResult().exists()){
                                String image = task.getResult().getString("userImage");
                                String name = task.getResult().getString("userName");
                                String phone = task.getResult().getString("userPhone");
                                String address = task.getResult().getString("userAddress");

                                userName.setText(name);
                                userPhone.setText(phone);
                                userAddress.setText(address);

                                Glide.with(MainActivity.this)
                                        .load(image)
                                        .into(userImage);
                            }
                        }else {
                            Toast.makeText(MainActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        signout = findViewById(R.id.signout);
        storedata = findViewById(R.id.Activityopen);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });


        storedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Users")
                        .document(curent_user_id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        Toast.makeText(MainActivity.this, "You already have your data Stored in database",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(MainActivity.this, StoreUserData.class));
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
