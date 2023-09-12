package com.example.bottomnavigationview.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnavigationview.R;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.MyViewHolder> {
    private List<Timestamp> timeStampList;
    private Activity context;
    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    public DialogAdapter(List<Timestamp> timeStampList, Activity context) {
        this.timeStampList = timeStampList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.datecard, parent, false);
        return new MyViewHolder(itemView, myListener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Timestamp currentItem = timeStampList.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        holder.textDate.setText( dateFormat.format(currentItem.toDate()) );
    }

    @Override
    public int getItemCount() {
        return timeStampList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textDate;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
