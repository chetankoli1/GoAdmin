package com.codewithsk.goadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.codewithsk.goadmin.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddProductActivity.class));
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.ic_container,new HomeFragment()).commit();

        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_orders:
                        fragment = new OrdersFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.ic_container,fragment).commit();
                return true;
            }

        });


    }
}