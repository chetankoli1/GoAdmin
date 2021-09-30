package com.codewithsk.goadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codewithsk.goadmin.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    ArrayList<Products> prList;
    AdapterProducts adapterProducts;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        prList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Products products = ds.getValue(Products.class);
                    prList.add(products);
                }
                adapterProducts = new AdapterProducts(getContext(),prList);
                binding.productsRv.setAdapter(adapterProducts);
                adapterProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}