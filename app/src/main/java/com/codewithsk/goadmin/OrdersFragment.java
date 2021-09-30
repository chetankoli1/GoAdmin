package com.codewithsk.goadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codewithsk.goadmin.databinding.FragmentOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {
    FragmentOrdersBinding binding;
    ArrayList<Order> prList;
    AdapterOrders adapterProducts;
    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders");
        prList = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Order products = ds.getValue(Order.class);
                    prList.add(products);
                }
                adapterProducts = new AdapterOrders(getContext(),prList);
                binding.orderRv.setAdapter(adapterProducts);
                adapterProducts.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}