package com.samy.groceryapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.samy.groceryapp.R;
import com.samy.groceryapp.databinding.ActivityDetailsBinding;
import com.samy.groceryapp.model.ViewAllModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    private int totalQuantity = 1;
    private int totalPrice = 0;
    private ViewAllModel viewAllModel;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setup();
    }

    private void setup() {

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        actionBar();

        final Object detail = getIntent().getSerializableExtra("detail");
        if (detail instanceof ViewAllModel) {
            viewAllModel = (ViewAllModel) detail;
        }
        if (viewAllModel != null) {
            downloadImd(viewAllModel.getImg_url());
            binding.rating.setText(viewAllModel.getRating());
            binding.description.setText(viewAllModel.getDescription());
            binding.detailedPrice.setText("Price :$" + viewAllModel.getPrice() + "/kg");
            if (viewAllModel.getType().equals("egg")) {
                binding.detailedPrice.setText("Price :$" + viewAllModel.getPrice() + "/dosen");
            }
            if (viewAllModel.getType().equals("milk")) {
                binding.detailedPrice.setText("Price :$" + viewAllModel.getPrice() + "/kg");
            }
        }
        totalPrice = viewAllModel.getPrice()*totalQuantity;
        binding.addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCard();
            }
        });
        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalQuantity++;
                totalPrice = viewAllModel.getPrice()*totalQuantity;
                binding.quantity.setText(totalQuantity+"");
                binding.detailedPrice.setText("Price :$" + totalPrice + "/kg");
                if (viewAllModel.getType().equals("egg")) {
                    binding.detailedPrice.setText("Price :$" + totalPrice + "/dosen");
                }
                if (viewAllModel.getType().equals("milk")) {
                    binding.detailedPrice.setText("Price :$" + totalPrice + "/kg");
                }
            }
        });
        binding.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 0) {
                    totalQuantity--;
                    binding.quantity.setText(totalQuantity+"");
                    totalPrice = viewAllModel.getPrice()*totalQuantity;
                    binding.detailedPrice.setText("Price :$" + totalPrice + "/kg");
                    if (viewAllModel.getType().equals("egg")) {
                        binding.detailedPrice.setText("Price :$" + totalPrice + "/dosen");
                    }
                    if (viewAllModel.getType().equals("milk")) {
                        binding.detailedPrice.setText("Price :$" + totalPrice + "/kg");
                    }  }
            }
        });
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

    private void addToCard() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        //date
        SimpleDateFormat currentDate = new SimpleDateFormat("mm dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        //time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productPrice",viewAllModel.getPrice()+"");
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",totalQuantity+"");
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }

    private void downloadImd(String img_url) {
        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                Log.d("mos samy", "name: " + viewAllModel.getName() + "\n GlideException: " + e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        };

        Glide.with(getApplicationContext()).load(img_url).listener(requestListener).error(R.drawable.baseline_image_24).into(binding.detailedImg);
    }
}