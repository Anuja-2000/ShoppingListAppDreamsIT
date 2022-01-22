package com.example.shoppinglistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.pm.SigningInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
private TextView tvSignIn,tvForgotPassword;
private EditText etEmail,etPassword;
private ConstraintLayout btSigniIn;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // name@xyz.com
    String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{5,24}";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignInActivity.this,FragmentActivity.class));
            finish();
        }


        btSigniIn = findViewById(R.id.constraint1);

        etEmail = findViewById(R.id.edt_email);
        etPassword = findViewById(R.id.edt_password);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,ForgotPasswordActivity.class));
                finish();
            }
        });
        tvSignIn = findViewById(R.id.tv_sign_in);
        String text = "<font color = #104E05>Don't have an account?</font><font color = #0400C2><b>SignUp</b></font>";
        tvSignIn.setText(Html.fromHtml(text));
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),SignUpActivity.class));
            }
        });

        btSigniIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(SignInActivity.this,R.anim.fadein);
                btSigniIn.startAnimation(anim);
                validateFields();
            }
        });
    }

    private void validateFields(){
        String email,password;
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (!(email.isEmpty()) && !(password.isEmpty())){
            if (email.matches(EmailPattern)){
                if (password.matches(PasswordPattern)){
                    logUser(email,password);
                }else {
                    etPassword.setError("Password should 5-24 characters");
                }
            }else {
                etEmail.setError("Email is incorrect!!!");
            }
        }else {
            Toast.makeText(SignInActivity.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void logUser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SignInActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this,FragmentActivity.class));
                    finish();
                }else{
                    Toast.makeText(SignInActivity.this, "Task unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, "Task Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}