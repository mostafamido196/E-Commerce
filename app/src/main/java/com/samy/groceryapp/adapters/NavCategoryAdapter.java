package com.samy.groceryapp.adapters;


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
import com.samy.groceryapp.activities.NavCatigoryActivity;
import com.samy.groceryapp.activities.ViewAllActivity;
import com.samy.groceryapp.model.HomeCategory;
import com.samy.groceryapp.model.NavCategoryModel;

import java.util.List;

public class NavCategoryAdapter extends RecyclerView.Adapter<NavCategoryAdapter.ViewHolder> {

    Context context;

    List<NavCategoryModel> list;

    public NavCategoryAdapter(Context context, List<NavCategoryModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public NavCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_cat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.description.setText(isSmall(list.get(position).getDescribtion()));
        holder.discount.setText(list.get(position).getDiscount());
        Glide.with(context).load(list.get(position).getImg_url()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NavCatigoryActivity.class);
                intent.putExtra("type",list.get(position).getType());
                Log.d("mos samy","popularAdapter: type:"+list.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    public String isSmall(String str) {
        if (str != null && str.length() > 30) {
            return str.substring(0, 18) + "...";
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, description, discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cat_nav_img);
            name = itemView.findViewById(R.id.nav_cat_name);
            description = itemView.findViewById(R.id.nav_cat_describtion);
            discount = itemView.findViewById(R.id.nav_cat_discount);
        }
    }
}
