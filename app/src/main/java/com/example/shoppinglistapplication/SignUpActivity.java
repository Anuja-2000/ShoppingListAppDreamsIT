package com.example.shoppinglistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etAddress, etPassword, etConfirmPassword;
    private TextView tvSignIn;
    private ConstraintLayout btSignUp;
    String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; // name@xyz.com
    String PasswordPattern = "[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{5,24}";

    private FirebaseAuth mAuth;
    private String userId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etFullName = findViewById(R.id.edt_full_name);
        etEmail = findViewById(R.id.edt_email);
        etAddress = findViewById(R.id.edt_address);
        etPassword = findViewById(R.id.edt_password);
        etConfirmPassword = findViewById(R.id.edt_confirm_password);

        btSignUp = findViewById(R.id.constraint1);

        tvSignIn = findViewById(R.id.tv_sign_in);
        String signInText = "<font color = #104E05>Already have an account?</font><font color = #0400C2><b>SignIn</b></font>";
        tvSignIn.setText(Html.fromHtml(signInText));
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                finish();
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(SignUpActivity.this,R.anim.fadein);
                btSignUp.startAnimation(anim);
                validateFields();
            }
        });
    }

    private void validateFields() {
        String fullName, email, address, password, confirmPassword;
        fullName = etFullName.getText().toString();
        email = etEmail.getText().toString();
        address = etAddress.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        if (!(fullName.isEmpty()) && !(email.isEmpty()) && !(address.isEmpty()) && !(password.isEmpty()) && !(confirmPassword.isEmpty())) {
            if (email.matches(EmailPattern)) {
                if (password.matches(PasswordPattern)) {
                    if (password.equals(confirmPassword)) {
                        saveUser(fullName, email, address, password);
                    }else{
                        Toast.makeText(SignUpActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    etPassword.setError("Password should be 5 to 24 characters");
                }
            } else {
                etEmail.setError("Email format is wrong!");
            }
        } else {
            Toast.makeText(SignUpActivity.this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
        }

    }

    private void saveUser(String name, String email, String address, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                userId = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("user").document(userId);
                Map<String,Object> user = new HashMap<>();
                user.put("FullName",name);
                user.put("Email",email);
                user.put("Address",address);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Data not Saved!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Login not success!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}