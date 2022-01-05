package com.example.bravewoman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesProducto extends AppCompatActivity {

    FirebaseFirestore mfirestone;
    TextView mNomProd,mDescription,mPrecio,mPrecioInferior;
    ImageView mIVproducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        Toolbar mToolDetallesproducto = findViewById(R.id.toolbar_DetallesProducto);
        setSupportActionBar(mToolDetallesproducto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mNomProd=findViewById(R.id.nombProd);
        mDescription=findViewById(R.id.detallesDescription);
        mPrecio=findViewById(R.id.precio);
        mPrecioInferior=findViewById(R.id.PreProducto);
        mIVproducto=findViewById(R.id.IVProducto);


        String pathReferencenceFirestone = getIntent().getStringExtra("pathReferenceProducto");
        mfirestone = FirebaseFirestore.getInstance();
        DocumentReference InfoProducto = mfirestone.document(pathReferencenceFirestone);
         InfoProducto.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
             @Override
             public void onSuccess(DocumentSnapshot documentSnapshot) {
                 if (documentSnapshot.exists()){
                     String nombProducto="";
                     String Descripcion="";
                     String precio="";
                     String urlfotoProducto="";

                     if (documentSnapshot.contains("nombreProducto")){
                         nombProducto = documentSnapshot.getString("nombreProducto");
                     }
                     if (documentSnapshot.contains("precioProducto")){
                         precio = documentSnapshot.getString("precioProducto");
                     }
                     if (documentSnapshot.contains("descripcionProducto")){
                         Descripcion = documentSnapshot.getString("descripcionProducto");
                     }
                     if (documentSnapshot.contains("fotoProducto")){
                         urlfotoProducto = documentSnapshot.getString("fotoProducto");
                     }
                     String PrecioFinal = "$"+precio;
                     mNomProd.setText(nombProducto);
                     mDescription.setText(Descripcion);
                     mPrecio.setText(PrecioFinal);
                     mPrecioInferior.setText(PrecioFinal);
                     Glide.with(getApplicationContext()).load(urlfotoProducto).into(mIVproducto);
                 }
             }
         });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.menu_detalles_producto,menu);
        return true;
    }
}