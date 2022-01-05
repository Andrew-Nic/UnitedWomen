package com.example.bravewoman;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Favoritos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);


        bottomNavigationView.setSelectedItemId(R.id.favoritos);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.favoritos:
                    return true;
                case R.id.tiendas:
                    startActivity(new Intent(getApplicationContext(),Tiendas.class));
                    overridePendingTransition(0,0);
                    return true;

            }
            return false;
        });
    }
}