package com.example.shoppinglistapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistapplication.R;
import com.example.shoppinglistapplication.models.ListModel;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter <ListAdapter.ViewHolder>{
    List<ListModel> lists;
    private ViewHolder.RecyclerViewClickListener clickListener;
    public ListAdapter(List<ListModel> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_template,parent,false);
        return new ViewHolder(view,clickListener)
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    }
}
