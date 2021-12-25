package com.example.bmianalayzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class lra extends RecyclerView.Adapter<lra.ViewHolder> {

    private List<Records> recordsList;
    private Context context;

    public lra(Context context,  List<Records> results) {
        this.context=context;
        this.recordsList = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_square,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Length.setText(recordsList.get(position).getLengthRecords());
        holder.Status.setText(recordsList.get(position).getStutesRecords());
        holder.Weight.setText(recordsList.get(position).getWeightRecords());
        holder.Date.setText(recordsList.get(position).getDateRecords());
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Length, Status, Weight, Date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Length = itemView.findViewById(R.id.Length);
            Status = itemView.findViewById(R.id.Status);
            Weight = itemView.findViewById(R.id.Weight);
            Date = itemView.findViewById(R.id.Date);
        }
    }


}
