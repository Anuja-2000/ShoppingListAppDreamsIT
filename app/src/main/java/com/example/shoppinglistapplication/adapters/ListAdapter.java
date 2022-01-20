package com.example.shoppinglistapplication.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglistapplication.R;
import com.example.shoppinglistapplication.models.ListModel;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    ArrayList<ListModel> lists;
    private ViewHolder.RecyclerViewClickListener clickListener;

    public ListAdapter(ArrayList<ListModel> lists, ViewHolder.RecyclerViewClickListener clickListener) {
        this.lists = lists;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_template, parent, false);
        return new ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String listName, lastModified,time;
        listName = lists.get(position).getListHeading();
        lastModified = lists.get(position).getCreateDate();
        time = lists.get(position).getCreateTime();

        holder.tvListName.setText(listName);
        holder.tvLastModified.setText("Last Updated:- "+lastModified+"@"+time);
    }


    @Override
    public int getItemCount() {
        return lists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvListName;
        private final TextView tvLastModified;
        RecyclerViewClickListener clickListener;

        public ViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.tv_list_name);
            tvLastModified = itemView.findViewById(R.id.tv_last_update);
            this.clickListener = clickListener;
            tvListName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClickListener(getAdapterPosition());
        }

        public interface RecyclerViewClickListener {
            void onClickListener(int position);
        }
    }
}
