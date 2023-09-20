package com.example.bottomnavigationview.adapter;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationview.R;
import com.example.bottomnavigationview.profileData.profile;
import com.google.firebase.Timestamp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

        holder.OPD_ID.setText("OPD_ID: "+ currentItem.getOPD_ID().toUpperCase());
        holder.Name.setText("Name: "+ (currentItem.getName().substring(0,1).toUpperCase()) + (currentItem.getName().substring(1,currentItem.getName().toString().length())));
        holder.Gender.setText("Gender: "+currentItem.getGender().toString().toUpperCase());

        holder.Mobile.setText("Mobile: "+currentItem.getPhone_number().toString().toUpperCase());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        holder.Age.setText("Age: "+ currentItem.getAge());
        holder.Address.setText("Address: "+ currentItem.getAddress() );
        int size = currentItem.getVisit_dates().size();
        Timestamp LastVisit = currentItem.getLastVisit();
        holder.Last_visit.setText("Last_visit: "+dateFormat.format(LastVisit.toDate())  );

    }

    @Override
    public int getItemCount() {
        return profileDataArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView OPD_ID;
        TextView Gender;
        TextView Age;
        TextView Mobile;
        TextView Address;
        TextView Last_visit;

        public MyViewHolder(View itemView, final onItemClickListener listener) {
            super(itemView);
            OPD_ID = itemView.findViewById(com.example.bottomnavigationview.R.id.OPD_ID);
            Name = itemView.findViewById(com.example.bottomnavigationview.R.id.Name);
            Gender = itemView.findViewById(com.example.bottomnavigationview.R.id.Gender);
            Age = itemView.findViewById(com.example.bottomnavigationview.R.id.Age);
            Mobile = itemView.findViewById(com.example.bottomnavigationview.R.id.Mobile);
            Address = itemView.findViewById(com.example.bottomnavigationview.R.id.Address);
            Last_visit = itemView.findViewById(com.example.bottomnavigationview.R.id.Last_visit);

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
