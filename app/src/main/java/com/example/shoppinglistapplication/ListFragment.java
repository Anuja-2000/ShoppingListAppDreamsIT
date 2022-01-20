package com.example.shoppinglistapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglistapplication.adapters.ListAdapter;
import com.example.shoppinglistapplication.models.ListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements com.example.shoppinglistapplication.adapters.ListAdapter.ViewHolder.RecyclerViewClickListener {
private TextView tvSalutation;
private FloatingActionButton floatingActionButton;
private RecyclerView recyclerView;
private LinearLayoutManager linearLayoutManager;
private ArrayList <ListModel> lists;
private ListAdapter listAdapter;
private FirebaseFirestore db;
private FirebaseAuth mAuth;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tvSalutation = view.findViewById(R.id.tv_salutaion);
        recyclerView = view.findViewById(R.id.recyclerVew);

        floatingActionButton = view.findViewById(R.id.fab);

        lists = new ArrayList<>();
        initData();




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddListActivity.class));
            }
        });

        return view;
    }

    public void setAdapter(){
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new ListAdapter(lists,this::onClickListener);

        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void initData(){
        db.collection("LISTS").whereEqualTo("UID",mAuth.getCurrentUser().getUid()).orderBy("TIMESTAMP", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        String sListID = documentSnapshot.getData().get("ID").toString();
                        int listID = Integer.valueOf(sListID);
                        String userID = documentSnapshot.getData().get("UID").toString();
                        String heading = documentSnapshot.getData().get("HEADING").toString();
                        String content = documentSnapshot.getData().get("CONTENT").toString();
                        String date = documentSnapshot.getData().get("CREATEDATE").toString();
                        String time = documentSnapshot.getData().get("CREATETIME").toString();
                        lists.add(new ListModel(listID,userID,heading,content,time,date));
                        Log.e("task",String.valueOf(lists.size()));
                        setAdapter();
                    }
                }else {
                    Toast.makeText(getContext(),"No Data!",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error Occurred!!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickListener(int position) {
        //what will happen when clicked
        Log.e("count",String.valueOf(position));
        Intent intent = new Intent(getContext(),AddListActivity.class);
        intent.putExtra("heading",lists.get(position).getListHeading());
        intent.putExtra("content",lists.get(position).getListContent());
        startActivity(intent);
    }
}