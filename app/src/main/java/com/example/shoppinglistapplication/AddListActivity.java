package com.example.shoppinglistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddListActivity extends AppCompatActivity {
    private EditText etListHeading, etListContent;
    private MaterialButton btSave;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        etListContent = findViewById(R.id.edt_list_content);
        etListHeading = findViewById(R.id.edt_list_heading);
        btSave = findViewById(R.id.btn_save);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String heading = intent.getStringExtra("heading");
        String content = intent.getStringExtra("content");

        if (!heading.isEmpty() && !content.isEmpty()){
            etListContent.setText(content);
            etListHeading.setText(heading);
        }


        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate(){
        //validation
        String heading,content;
        heading = etListHeading.getText().toString();
        content = etListContent.getText().toString();
        getListID();

    }

    private void saveData(int id){
        String heading,content;
        heading = etListHeading.getText().toString();
        content = etListContent.getText().toString();
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


        Map<String,Object> list = new HashMap<>();
        list.put("CONTENT",content);
        list.put("HEADING",heading);
        list.put("CREATEDATE",currentDate);
        list.put("CREATETIME",currentTime);
        list.put("ID",id);
        list.put("UID",uid);
        list.put("TIMESTAMP", FieldValue.serverTimestamp());
        db.collection("LISTS").add(list).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddListActivity.this,"Data Saved!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddListActivity.this,"Task Unsuccess!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddListActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getListID(){

        db.collection("LISTS").whereEqualTo("UID",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int max = 1;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        int id = Integer.parseInt(document.getData().get("ID").toString());
                        if (max < id){
                            max = id;
                        }
                    }
                    saveData(max+1);
                } else {
                    Toast.makeText(AddListActivity.this,"Task Unsuccess!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddListActivity.this,"Error Occurred getting ID!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}