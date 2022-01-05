package com.example.bravewoman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tiendas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);


        bottomNavigationView.setSelectedItemId(R.id.tiendas);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.favoritos:
                    startActivity(new Intent(getApplicationContext(),Favoritos.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.tiendas:
                    return true;

            }
            return false;
        });
    }
}