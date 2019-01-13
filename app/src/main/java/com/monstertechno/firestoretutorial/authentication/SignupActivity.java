package com.monstertechno.firestoretutorial.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.monstertechno.firestoretutorial.MainActivity;
import com.monstertechno.firestoretutorial.R;

public class SignupActivity extends AppCompatActivity {


    private EditText emailFiled,passwordFiled,checkpasswordFiled;
    private Button register,signin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailFiled = findViewById(R.id.emailFiled);
        passwordFiled = findViewById(R.id.passwordFiled);
        checkpasswordFiled = findViewById(R.id.repasswordFiled);

        register = findViewById(R.id.register);
        signin = findViewById(R.id.signinbutton);

        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailFiled.getText().toString();
                String password = passwordFiled.getText().toString();
                String repasswprd = checkpasswordFiled.getText().toString();

                if(!TextUtils.isEmpty(email)&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(repasswprd)){

                    if(password.equals(repasswprd)){

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(SignupActivity.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }else {
                        Toast.makeText(SignupActivity.this,"Your password is not match",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SignupActivity.this,"All fileds are important",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
