package com.samy.groceryapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samy.groceryapp.adapters.NavCategoryDetailedAdapter;
import com.samy.groceryapp.databinding.ActivityDetailsBinding;
import com.samy.groceryapp.databinding.ActivityNavCatigoryBinding;
import com.samy.groceryapp.model.HomeCategory;
import com.samy.groceryapp.model.NavCatDetailModel;
import com.samy.groceryapp.model.ViewAllModel;

import java.util.ArrayList;
import java.util.List;

public class NavCatigoryActivity extends AppCompatActivity {
    private ActivityNavCatigoryBinding binding;
    private List<NavCatDetailModel> list;
    private NavCategoryDetailedAdapter adapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavCatigoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
    }

    private void setup() {
        progressbar();
        actionBar();
        recyclerView();
        data();
    }

    private void progressbar() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.navCatDetRec.setVisibility(View.GONE);
    }

    private void data() {
        firestore = FirebaseFirestore.getInstance();
        String type = getIntent().getStringExtra("type");
        getProduct(type);
    }

    private void recyclerView() {
        list = new ArrayList<>();
        adapter = new NavCategoryDetailedAdapter(this, list);
        binding.navCatDetRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.navCatDetRec.setAdapter(adapter);


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
            firestore.collection("NavCategoryDetailed").whereEqualTo("type", type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot decument : task.getResult()) {
                        NavCatDetailModel itemModel = decument.toObject(NavCatDetailModel.class);
                        list.add(itemModel);
                        adapter.notifyDataSetChanged();

                        binding.progressbar.setVisibility(View.GONE);
                        binding.navCatDetRec.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

}