package com.example.shoppinglistapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateListActivity extends AppCompatActivity {
    private EditText etListHeading, etListContent;
    private MaterialButton btUpdate,btDelete;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uid;
    private int listID;
    private ImageButton btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        etListContent = findViewById(R.id.edt_list_content);
        etListHeading = findViewById(R.id.edt_list_heading);

        btUpdate = findViewById(R.id.btn_update);
        btBack = findViewById(R.id.btn_back);
        btDelete = findViewById(R.id.btn_delete);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateListActivity.this,FragmentActivity.class));
                finish();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDocumentID(0);
            }
        });

        Intent intent = getIntent();
        String heading = intent.getStringExtra("heading");
        String content = intent.getStringExtra("content");
        listID = intent.getIntExtra("listID",0);

        if (!heading.isEmpty() && !content.isEmpty()){
            etListContent.setText(content);
            etListHeading.setText(heading);
        }

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate(){
        getDocumentID(1);
    }

    private void getDocumentID(int choice){
        db.collection("LISTS").whereEqualTo("ID",listID).whereEqualTo("UID",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    String id = null;
                    for (QueryDocumentSnapshot document:task.getResult()){
                        id = document.getId();
                    }
                    if (choice == 1) {
                        updateDatabase(id);
                    }else if (choice == 0){
                        deleteDocument(id);
                    }

                }else {
                    Toast.makeText(UpdateListActivity.this,"Error getting document!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateListActivity.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDatabase(String id){
        String content,heading,date,time;
        date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        content = etListContent.getText().toString();
        heading = etListHeading.getText().toString();

        db.collection("LISTS").document(id).update("CONTENT",content);
        db.collection("LISTS").document(id).update("HEADING",heading);
        db.collection("LISTS").document(id).update("CREATEDATE",date);
        db.collection("LISTS").document(id).update("CREATETIME",time);
        db.collection("LISTS").document(id).update("TIMESTAMP", FieldValue.serverTimestamp());
        Toast.makeText(UpdateListActivity.this,"List Updated!",Toast.LENGTH_SHORT).show();
    }

    private void deleteDocument(String id){
        db.collection("LISTS").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateListActivity.this,"List Deleted!",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateListActivity.this,"Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}