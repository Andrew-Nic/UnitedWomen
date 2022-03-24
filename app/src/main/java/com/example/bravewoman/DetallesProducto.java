package com.example.bravewoman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesProducto extends AppCompatActivity {

    FirebaseFirestore mfirestone;
    TextView mNomProd,mDescription,mPrecio,mPrecioInferior;
    ImageView mIVproducto;

    String PrecioFinal, nomProd;
    Button mComprar;
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
        mComprar=findViewById(R.id.btnComprar);

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

                     nomProd = nombProducto;
                     PrecioFinal = "$"+precio;
                     mNomProd.setText(nombProducto);
                     mDescription.setText(Descripcion);
                     mPrecio.setText(PrecioFinal);
                     mPrecioInferior.setText(PrecioFinal);
                     Glide.with(getApplicationContext()).load(urlfotoProducto).into(mIVproducto);
                 }
             }
         });


         mComprar.setOnClickListener(new View.OnClickListener() {
             final String mPhoneNumber ="9971242299";
             @Override
             public void onClick(View view) {
                 boolean installed = isAppInstalled("com.whatsapp");

                 if (installed)
                 {
                     Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+521"+mPhoneNumber+"&text="+ messageWhatsapp(nomProd,PrecioFinal)));
                     startActivity(intent);
                 }
                 else
                 {
                     Toast.makeText(DetallesProducto.this, "Whatsapp is not installed!", Toast.LENGTH_SHORT).show();
                 }
             }
         });
    }

    public static String messageWhatsapp(String nombreProducto, String Precio){

        final String message = "Deseo comprar el siguiente producto: \n - "+nombreProducto+"\n\nTiene un precio de: "+Precio;
        return message;
    }

    private boolean isAppInstalled(String s) {
        PackageManager packageManager = getPackageManager();
        boolean is_installed;

        try {
            packageManager.getPackageInfo(s, PackageManager.GET_ACTIVITIES);
            is_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed = false;
            e.printStackTrace();
        }
        return is_installed;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
       getMenuInflater().inflate(R.menu.menu_detalles_producto,menu);
        return true;
    }
}