package com.example.arenaofpixels.homeAdapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arenaofpixels.HomeFragment;
import com.example.arenaofpixels.ImageObj;
import com.example.arenaofpixels.R;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    List<ImageObj> records;
    Context context;
    MyClick listener;

    public MyAdapter(Context context , ArrayList<ImageObj> recordArrayList, MyClick listener){
        this.context = context;
        records = recordArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.card_item, parent , false);
        final MyViewHolder vh = new MyViewHolder(view);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(vh.getAdapterPosition());
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        //System.out.println(position);
        Glide.with(context).load(Uri.parse(records.get(position).uri)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface MyClick{
        public void onClick(int pos);
    }

}

