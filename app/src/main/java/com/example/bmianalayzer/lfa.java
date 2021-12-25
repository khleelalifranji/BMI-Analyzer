package com.example.bmianalayzer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class lfa extends RecyclerView.Adapter<lfa.ViewHolder> {
    private Context context;
    private List<nfood> foodsList;
    FirebaseFirestore firebaseFirestore;
    private static ClickListener listener;

    public lfa(Context context, List<nfood> results) {
        this.context = context;
        this.foodsList = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfood,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        holder.tvCategory.setText(foodsList.get(position).getCategoryFoodsName());
        holder.tvCalory.setText(foodsList.get(position).getClaryFoods());
        holder.tvNameFood.setText(foodsList.get(position).getNameFoods());
        Glide.with(context).load(foodsList.get(position).getFBUri()).into(holder.imgFood);
        int positions =  position;
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("food").document(foodsList.get(positions).getdIDFoods()).delete().
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "deleted ", Toast.LENGTH_SHORT).show();
                                    foodsList.remove(positions);
                                    context.startActivity(new Intent(context , listfood.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvCalory, tvNameFood;
        ImageView imgFood, imgDelete;
        Button btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCalory = itemView.findViewById(R.id.tvCalory);
            tvNameFood = itemView.findViewById(R.id.tvNameFood);
            imgFood = itemView.findViewById(R.id.photoFood);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(foodsList.get(getAdapterPosition()));
                }
            });

        }

    }

    public void OnItemCliclkLisener(ClickListener listener) {
        lfa.listener = listener;
    }

    public interface ClickListener {
        void onClick(nfood result);
    }

}
