package com.example.bravewoman;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    static final float END_SCALE = 0.7f;
    private RelativeLayout contentView;
    private RecyclerView mReVPublicaciones;
    private ImageView mbtnMenu;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private ProductosAdapter adapter;
    private CardView mCarTal, mCarAso;

    //drawer menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //activity main
        contentView= findViewById(R.id.contenidoMainActivity);
        mReVPublicaciones = findViewById(R.id.ReVPublicaciones);
        mCarTal = findViewById(R.id.cardTalleres);
        mCarAso=findViewById(R.id.CardAsoIns);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);
        //menu hooks
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        mbtnMenu=findViewById(R.id.btnmenu);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        navigationDrawer();
        configurarRecliclerViewProductos();
        SelecCardT_A();

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    return true;
                case R.id.favoritos:
                    startActivity(new Intent(getApplicationContext(),Favoritos.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.tiendas:
                    startActivity(new Intent(getApplicationContext(),Tiendas.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    //card Views de presentacion de los talleres e Intituciones y asosiaciones
    private void SelecCardT_A(){
        mCarTal.setOnClickListener(v -> anuncio());

        mCarAso.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,descriptionInstitucion.class)));
    }
    private void anuncio(){
        Toast.makeText(MainActivity.this, "Proximante...", Toast.LENGTH_SHORT).show();
    }

//    barra de navegacion lateral
    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        mbtnMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            }else{
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {


                final float diffScaleOffset = slideOffset * (1 - END_SCALE);
                final float offSetScale = 1 - diffScaleOffset;
                contentView.setScaleX(offSetScale);
                contentView.setScaleY(offSetScale);

                final float xOffset= drawerView.getWidth() * slideOffset;
                final float xOffsetDiff= contentView.getWidth() * diffScaleOffset / 2;
                final float xTranslation= xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOut:
                mAuth.signOut();
                startActivity(new Intent(this, Login.class));
                finish();
                break;
            case R.id.mi_negocio:
                startActivity(new Intent(this,MiNegocio.class));
                break;
            case R.id.mi_perfil:
                startActivity(new Intent(this,MiPerfil.class));
                break;
            case R.id.Talleres:
            case R.id.Aso_Ins:
                anuncio();
                break;
            case R.id.AcercaDe:
                startActivity(new Intent(this,AboutUs.class));
                break;
        }
        return false;
    }



    //reycler view para mostar los productos en la pantalla
    private void configurarRecliclerViewProductos() {
        Query query = mFirestore.collection("publicaciones_productos");

        FirestoreRecyclerOptions<ListProductos> options = new FirestoreRecyclerOptions.Builder<ListProductos>()
                .setQuery(query, ListProductos.class)
                .build();

        adapter = new ProductosAdapter(options);

        mReVPublicaciones.setHasFixedSize(true);
        mReVPublicaciones.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mReVPublicaciones.setAdapter(adapter);

        adapter.setOnItemClickListener(new ProductosAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String path = documentSnapshot.getReference().getPath();
               Intent intent = new Intent(MainActivity.this,DetallesProducto.class);
               intent.putExtra("pathReferenceProducto",path);
                startActivity(intent);
            }
        });
    }



    //ciclo de vida del activity
    @Override
    protected void onStart() {
        super.onStart();
        if (user.isEmailVerified()){
            adapter.startListening();
        }else{
            startActivity(new Intent(MainActivity.this,VerificacionDeCuenta.class));
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}