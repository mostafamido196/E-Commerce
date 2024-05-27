package com.samy.groceryapp.ui.mycart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samy.groceryapp.R;
import com.samy.groceryapp.activities.PlacedOrderActivity;
import com.samy.groceryapp.adapters.MyCartAdapter;
import com.samy.groceryapp.databinding.FragmentMyCartsBinding;
import com.samy.groceryapp.model.MyCartModel;
import com.samy.groceryapp.model.UserModel;
import com.samy.groceryapp.model.ViewAllModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {

    List<MyCartModel> list;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    MyCartAdapter adapter;
    private FragmentMyCartsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyCartsBinding.inflate(inflater, container, false);
        setup();
        return binding.getRoot();
    }

    private void setup() {
        progressbar();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerview();
        broadcastReceiver();
        getData();
        //
        payment();

    }

    private void payment() {
        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) list);
                startActivity(intent);
            }
        });
    }

    private void recyclerview() {
        list = new ArrayList<>();
        adapter = new MyCartAdapter(getActivity(), list);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerview.setAdapter(adapter);

    }

    private void getData() {
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        binding.progressbar.setVisibility(View.GONE);
                        for (QueryDocumentSnapshot decument : task.getResult()) {
                            String documentId = decument.getId();
                            MyCartModel myCartModel = decument.toObject(MyCartModel.class);
                            myCartModel.setDocumentId(documentId);
                            list.add(myCartModel);
                            adapter.notifyDataSetChanged();
                            binding.progressbar.setVisibility(View.GONE);
                            binding.recyclerview.setVisibility(View.VISIBLE);
                        }
                    }


                });
    }

    private void broadcastReceiver() {

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int totalBill = intent.getIntExtra("totalAmount", 0);
                binding.txtPrice.setText("Total Bill :" + totalBill + "$");
            }
        };
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));

    }

    private void progressbar() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.GONE);
    }
}