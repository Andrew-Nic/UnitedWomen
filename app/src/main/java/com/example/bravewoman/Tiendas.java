package com.example.bravewoman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Tiendas extends AppCompatActivity {
    private RecyclerView rvStores;
    private FirebaseFirestore mFirestore;
    private tiendasAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);
        rvStores=findViewById(R.id.rvTiendas);
        mFirestore = FirebaseFirestore.getInstance();


        listStores();


        //bootom navigation
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

    private void listStores(){
        Query query = mFirestore.collection("Negocios");

        FirestoreRecyclerOptions<listTTiendas> options = new FirestoreRecyclerOptions.Builder<listTTiendas>()
                .setQuery(query, listTTiendas.class)
                .build();

        adapter = new tiendasAdapter(options);
        rvStores.setHasFixedSize(true);
        rvStores.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvStores.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}