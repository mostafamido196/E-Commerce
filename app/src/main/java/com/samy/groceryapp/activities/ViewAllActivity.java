package com.samy.groceryapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samy.groceryapp.adapters.ViewAllAdapter;
import com.samy.groceryapp.databinding.ActivityViewAllBinding;
import com.samy.groceryapp.model.ViewAllModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllActivity extends AppCompatActivity {

    private ActivityViewAllBinding binding;

    //
    FirebaseFirestore db;
    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
    }

    private void setup() {
        progressbar();
        actionBar();

        String type = getIntent().getStringExtra("type");
        Log.d("mos samy", "ViewAllActivity: type:" + type);

        db = FirebaseFirestore.getInstance();
        binding.viewAllRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(this, viewAllModelList);
        binding.viewAllRec.setAdapter(viewAllAdapter);

        if (type == null)
            getAllProduct();
        else
            getProduct(type);// types -> fruit, vegetables, fish, egg, milk


    }

    private void getAllProduct() {
        db.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot decument : task.getResult()) {
                    ViewAllModel itemModel = decument.toObject(ViewAllModel.class);
                    Log.d("mos samy", "ViewAllActivity: name" + itemModel.getName() + "," + itemModel.getType());
                    viewAllModelList.add(itemModel);
                    viewAllAdapter.notifyDataSetChanged();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.viewAllRec.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void progressbar() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.viewAllRec.setVisibility(View.GONE);
    }

    private void actionBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
    }

    private void getProduct(String type) {
        if (type != null && type.equalsIgnoreCase(type)) {
            db.collection("AllProducts").whereEqualTo("type", type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    binding.progressbar.setVisibility(View.GONE);
                    for (QueryDocumentSnapshot decument : task.getResult()) {
                        ViewAllModel itemModel = decument.toObject(ViewAllModel.class);
                        Log.d("mos samy", "ViewAllActivity: name" + itemModel.getName() + "," + itemModel.getType());
                        viewAllModelList.add(itemModel);
                        viewAllAdapter.notifyDataSetChanged();

                        binding.progressbar.setVisibility(View.GONE);
                        binding.viewAllRec.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }


}