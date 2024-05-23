package com.samy.groceryapp.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samy.groceryapp.adapters.NavCategoryAdapter;
import com.samy.groceryapp.adapters.PopularAdapter;
import com.samy.groceryapp.databinding.FragmentCategoryBinding;
import com.samy.groceryapp.model.NavCategoryModel;
import com.samy.groceryapp.model.PopularModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    FirebaseFirestore db;

    private FragmentCategoryBinding binding;
    NavCategoryAdapter categoryAdapter;
    List<NavCategoryModel> categoryModelList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setup();
        return root;
    }

    private void setup() {
progressbar();
        //popular item
        db  = FirebaseFirestore.getInstance();
        binding.catRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new NavCategoryAdapter(getActivity(), categoryModelList);
        binding.catRec.setAdapter(categoryAdapter);
        db.collection("NavCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                binding.progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot decument : task.getResult()) {
                        categoryModelList.add(decument.toObject(NavCategoryModel.class));
                        categoryAdapter.notifyDataSetChanged();

                        binding.progressbar.setVisibility(View.GONE);
                        binding.catRec.setVisibility(View.VISIBLE);

                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void progressbar() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.catRec.setVisibility(View.GONE);
    }
}