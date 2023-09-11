package com.example.bottomnavigationview.adapter;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.bottomnavigationview.profileData.profile;


import java.util.ArrayList;
import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>  {
    private List<profile> profileDataArrayList;
    private Activity context;
    private onItemClickListener myListener;


    public interface onItemClickListener {
        void onItemClicking(int position);
    }


    public void setOnItemClickListener(onItemClickListener listener) {
        myListener = listener;
    }

    public recyclerAdapter(List<profile> dataArrayList, Activity context) {
        this.profileDataArrayList = dataArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(com.example.bottomnavigationview.R.layout.eachrow, parent, false);
        return new MyViewHolder(itemView, myListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        profile currentItem = profileDataArrayList.get(position);
        holder.hTitle.setText(currentItem.getName());

    }

    @Override
    public int getItemCount() {
        return profileDataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView hTitle;
        TextView hid;
        TextView hinfo;

        public MyViewHolder(View itemView, final onItemClickListener listener) {
            super(itemView);
            hTitle = itemView.findViewById(com.example.bottomnavigationview.R.id.headingTitle);
            hid = itemView.findViewById(com.example.bottomnavigationview.R.id.hid);
            hinfo = itemView.findViewById(com.example.bottomnavigationview.R.id.hinfo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClicking(position);
                        }
                    }
                }
            });
        }
    }
}
