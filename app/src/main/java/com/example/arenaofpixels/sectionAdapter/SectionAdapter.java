package com.example.arenaofpixels.sectionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;
import com.example.arenaofpixels.R;
import com.example.arenaofpixels.Resources;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    String[] sections;
    Context context;
    boolean[] checked;

    public SectionAdapter(Context context, String[] sectionArrayList){
        this.context = context;
        sections = sectionArrayList;
        checked = new boolean[sectionArrayList.length];
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.section_item, parent , false);

        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, final int position) {
        //System.out.println(position);
        holder.checkBox.setText(sections[position]);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked[position] = !checked[position];
            }
        });
    }

    @Override
    public int getItemCount() {
        return sections.length;
    }

    public boolean getChecked(int position){
        return checked[position];
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public SectionViewHolder(@NonNull final View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

}
