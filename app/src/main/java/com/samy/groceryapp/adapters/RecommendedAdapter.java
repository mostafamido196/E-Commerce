package com.samy.groceryapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samy.groceryapp.R;
import com.samy.groceryapp.activities.ViewAllActivity;
import com.samy.groceryapp.model.RecommendedModel;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {

    Context context;
    List<RecommendedModel> recommendedList;

    public RecommendedAdapter(Context context, List<RecommendedModel> recommendedList) {
        this.context = context;
        this.recommendedList = recommendedList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(recommendedList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(recommendedList.get(position).getName());
        holder.rating.setText(recommendedList.get(position).getRating());
        holder.description.setText(recommendedList.get(position).getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("type",recommendedList.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,description,rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rec_img);
            name = itemView.findViewById(R.id.rec_name);
            description = itemView.findViewById(R.id.rec_dec);
            rating = itemView.findViewById(R.id.rec_rating);
        }
    }
}
