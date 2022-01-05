package com.example.bravewoman;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MiNegocio extends AppCompatActivity {
    private CircleImageView mfotoPerfil;
    private ImageView mfotobaner;
    TextView mTVNombreNegocio,mTVDescripcionNegocio;
    Button mCrearNeg;
    ImageView mBTNagregarProducto;

    FirebaseFirestore mfirestore;
    String mUserID;
    LinearLayout mLLMinngocio;
    RelativeLayout mRLSinNegocio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_negocio);

        mfotobaner = findViewById(R.id.baner);
        mfotoPerfil = findViewById(R.id.fotoPerfilNegocio);
        mTVNombreNegocio = findViewById(R.id.NomNegocio);
        mTVDescripcionNegocio = findViewById(R.id.DescMiNegocio);
        mRLSinNegocio =findViewById(R.id.SinNegocio);
        mCrearNeg= findViewById(R.id.btnRegistarMiNegocio);
        mLLMinngocio = findViewById(R.id.ContMiNegocio);
        mBTNagregarProducto = findViewById(R.id.btnAgregarProducto);
        mfirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAut = FirebaseAuth.getInstance();
        FirebaseUser user = mAut.getCurrentUser();

        Toolbar mToolDetallesproducto = findViewById(R.id.toolbar_DetallesNegocio);
        setSupportActionBar(mToolDetallesproducto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assert user != null;
        mUserID = user.getUid();

        Negocio();

        mCrearNeg.setOnClickListener(v -> startActivity(new Intent(MiNegocio.this,RegisroNegocio.class)));
        mBTNagregarProducto.setOnClickListener(v -> {
            startActivity(new Intent(MiNegocio.this,RegistroProductos.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.modificar_mi_megocio,menu);
        return true;
    }


    public void Negocio(){
        mfirestore.collection("Negocios").document( mUserID+"").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    mRLSinNegocio.setVisibility(View.GONE);
                    mLLMinngocio.setVisibility(View.VISIBLE);
                    String nombNegocio="";
                    String Description="";
                    String urlPerfil="";
                    String urlBanner="";

                    if (documentSnapshot.contains("nombreNegocio")){
                        nombNegocio = documentSnapshot.getString("nombreNegocio");
                    }
                    if (documentSnapshot.contains("descNegocio")){
                        Description = documentSnapshot.getString("descNegocio");
                    }
                    if (documentSnapshot.contains("fotoPerfil")) {
                        urlPerfil = documentSnapshot.getString("fotoPerfil");
                    }
                    if (documentSnapshot.contains("fotoBaner")) {
                        urlBanner = documentSnapshot.getString("fotoBaner");
                    }

                    mTVNombreNegocio.setText(nombNegocio);
                    mTVDescripcionNegocio.setText(Description);
                    Glide.with(MiNegocio.this).load(urlPerfil).into(mfotoPerfil);
                    Glide.with(MiNegocio.this).load(urlBanner).centerCrop().into(mfotobaner);

                }else{
                    mRLSinNegocio.setVisibility(View.VISIBLE);
                    mLLMinngocio.setVisibility(View.GONE);
                }
            }
        });
    }
}