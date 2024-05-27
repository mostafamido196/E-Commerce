package com.samy.groceryapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samy.groceryapp.R;
import com.samy.groceryapp.activities.ViewAllActivity;
import com.samy.groceryapp.adapters.HomeAdapter;
import com.samy.groceryapp.adapters.PopularAdapter;
import com.samy.groceryapp.adapters.RecommendedAdapter;
import com.samy.groceryapp.adapters.ViewAllAdapter;
import com.samy.groceryapp.databinding.FragmentHomeBinding;
import com.samy.groceryapp.model.HomeCategory;
import com.samy.groceryapp.model.PopularModel;
import com.samy.groceryapp.model.RecommendedModel;
import com.samy.groceryapp.model.ViewAllModel;
import com.samy.groceryapp.viewmodel.main.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseFirestore db;
    //search
    List<ViewAllModel> searchList;
    ViewAllAdapter searchAdapter;

    //
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;
    //
    List<PopularModel> popularModelList;
    PopularAdapter popularAdapter;
    //
    List<RecommendedModel> recommendedList;
    RecommendedAdapter recommendedAdapter;

    //
    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.setViewModel(searchViewModel);
        binding.setLifecycleOwner(this);
        View root = binding.getRoot();

        setup();
        return root;
    }

    private void setup() {
        //initial fragment state
        showProgress(true);
        db = FirebaseFirestore.getInstance();
        getData();
        viewAll();


        observe();
    }

    private void observe() {
        searchList = new ArrayList<>();
        searchAdapter = new ViewAllAdapter(getActivity(), searchList);
        binding.searchRec.setAdapter(searchAdapter);
        binding.searchRec.setHasFixedSize(true);
        searchViewModel.getSearchListLivedata().observe(this, response -> {
            switch (response.status) {
                case LOADING:
                    // Show loading indicator
                    Log.d("mos samy", "LOADING ");
                    break;
                case SUCCESS:
                    // Update UI with response.data
                    Log.d("mos samy", "SUCCESS: " + ((List<ViewAllModel>) response.data));
                    searchList.clear();
                    searchAdapter.notifyDataSetChanged();
                    for (ViewAllModel model : (List<ViewAllModel>) response.data) {
                        searchList.add(model);
                        searchAdapter.notifyDataSetChanged();
                    }
                    break;
                case ERROR:
                    // Show error message
                    Toast.makeText(getActivity(), "error: " + response.error, Toast.LENGTH_LONG).show();
                    Log.d("mos samy", "ERROR: " + response.error);
                    break;
                case IDEL:
                    // Handle idle state if needed
                    Log.d("mos samy", "IDEL");
                    break;
            }
        });
        searchViewModel.getSearchKeyET().observe(this, s -> {
            searchList.clear();
            searchAdapter.notifyDataSetChanged();
            solveIssues();
            searchViewModel.search(s);
        });
    }

    private void solveIssues() {
        // when long press on delete on edittext
        binding.searchBox.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (binding.searchBox.getText().toString().isEmpty())
                    searchViewModel.onDeleteKeyPressed();
            }
            return false;
        });
    }


    private void viewAll() {
        View.OnClickListener goToViewAll = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                startActivity(intent);
            }
        };
        binding.viewAllExplore.setOnClickListener(goToViewAll);
        binding.viewAllRecommended.setOnClickListener(goToViewAll);
        binding.viewAllPopular.setOnClickListener(goToViewAll);

    }

    private void getData() {
        //popular item
        binding.popRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        popularModelList = new ArrayList<>();
        popularAdapter = new PopularAdapter(getActivity(), popularModelList);
        binding.popRec.setAdapter(popularAdapter);
        db.collection("PopularProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot decument : task.getResult()) {
                        popularModelList.add(decument.toObject(PopularModel.class));
                        popularAdapter.notifyDataSetChanged();
                        showProgress(false);
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //category item
        binding.explorreRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(), categoryList);
        binding.explorreRec.setAdapter(homeAdapter);
        db.collection("HomeCategory").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot decument : task.getResult()) {
                        categoryList.add(decument.toObject(HomeCategory.class));
                        homeAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //recommended item
        binding.recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        recommendedList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(getActivity(), recommendedList);
        binding.recommendedRec.setAdapter(recommendedAdapter);
        db.collection("Recommended").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                binding.progressbar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot decument : task.getResult()) {
                        recommendedList.add(decument.toObject(RecommendedModel.class));
                        recommendedAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void showProgress(boolean b) {
        if (b) {
            binding.progressbar.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.GONE);
        } else {
            binding.progressbar.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.VISIBLE);
        }
    }


}