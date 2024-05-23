package com.samy.groceryapp.ui.newproduct;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samy.groceryapp.R;
import com.samy.groceryapp.databinding.FragmentMyCartsBinding;
import com.samy.groceryapp.databinding.FragmentNewProductBinding;


public class NewProductFragment extends Fragment {


    private FragmentNewProductBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewProductBinding.inflate(inflater, container, false);
        return binding.getRoot();    }
}