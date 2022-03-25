package com.example.bravewoman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

/*para publicar un producto:
* 1. Se rellenan los datos requeridos en el activity
* 2. al precionar el Boton de Publicar se ejecuta el metodo publicarProducto()
*    Primero sube la imagen al Storage de Firebase.
*       si la imagen se sube, se le pasa la referencia de donde se guardo la imagen al metodo SaveToFirestore() y nombre del procucto, posteriormente se ejecuta este mismo
*       si no se pudo subir se muestra un toast que indica que no se pudo subir
* 3. Al ejecutar SaveToFirestore()
*    primero consulta el url de la imagen del producto en base a la referencia del storage que se le paso al metodo
*    Segunda cosa por hacer es crean un HashMap que contendra los la informacion del producto; el hash map se llama miNegocio; esta mal nombrado, deberia ser producto pero por
*       cuestiones de tiempo no le he cambiado el nombre
*    Tercera cosa por hacer para simplificar las cosas para poner la informacion al hashMap, es poner la informacion del activity en las variables correspondientes
*    Cuarta cosa es poner las variables al hashmap con la funcion .put
*    Quinta cosa por subir la informacion del hasmap a firestore, si se sube se regresa al activity de mi negocio y si no se muestra un toast */
public class RegistroProductos extends AppCompatActivity {

    private ImageView mIVfotoProducto;
    private EditText mNombreProducto, mDescripcionProducto, mPrecioProducto;
    private TextView mNombreTienda;
    private Button mPublicar;
    private Uri mImageUri;
    private FirebaseFirestore mFirestone;
    private StorageReference mStorageReference;
    private String mUserID;
    private Uri descargaUriFoto;
    private String nombreProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_productos);

        Toolbar mToolDetallesproducto = findViewById(R.id.toolbar_AgregarProducto);
        setSupportActionBar(mToolDetallesproducto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registrar Producto");

        mNombreTienda = findViewById(R.id.TVnombreTienda);
        mNombreProducto = findViewById(R.id.ProductName);
        mDescripcionProducto= findViewById(R.id.ProductDescription);
        mPrecioProducto = findViewById(R.id.productPrice);
        mIVfotoProducto = findViewById(R.id.IVfotoproducto);
        mPublicar = findViewById(R.id.BtnPublicar);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mFirestone = FirebaseFirestore.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();


        assert mUser != null;
        mUserID = mUser.getUid();



        mIVfotoProducto.setOnClickListener(v -> {
            //solicita los permisos para acceder a la memoria y seleccionar uan foto.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(RegistroProductos.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegistroProductos.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //Recorta la imagen para que se acomode al imageview de tipo circular.
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(RegistroProductos.this);
                }
            }
        });

        //solo consulta el nombre de la tienda y se lo pone al activity
        mFirestone.collection("Negocios").document(mUserID+"").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            private String nombreTienda;
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    if (documentSnapshot.contains("nombreNegocio")){
                        nombreTienda = documentSnapshot.getString("nombreNegocio");
                        mNombreTienda.setText(nombreTienda);
                    }
                }else{
                    Toast.makeText(RegistroProductos.this, "NoExiste el documento", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // boton para publicar el producto
        mPublicar.setOnClickListener(v -> publicarProducto());
    }

// recorta la imagen para ser subida
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.general_sin_opciones,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                mImageUri= result.getUri();
                Glide.with(this).load(mImageUri).into(mIVfotoProducto);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void publicarProducto(){
        nombreProducto = mNombreProducto.getText().toString().trim();

        StorageReference filepath = mStorageReference.child("FotoProductosXnegocio").child(mUserID).child(nombreProducto);
        StorageReference FotoProducto = filepath.child(nombreProducto+".jpg");
        FotoProducto.putFile(mImageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                //FotoProducto es una referencia de la imagen subuda al storage, es decir, donde se encuentra ubicada la imagen
                saveToFirestone(FotoProducto, nombreProducto);
            }else{
                Toast.makeText(RegistroProductos.this, "NO SE SUBIO A FIRESTORE", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void saveToFirestone(StorageReference fotoProducto, String nombreProducto) {
        fotoProducto.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            Map<String,Object> miNegocio= new HashMap<>();
            @Override
            public void onSuccess(Uri uri) {
                descargaUriFoto = uri;
                String descripcionProducto = mDescripcionProducto.getText().toString();
                String precioProducto = mPrecioProducto.getText().toString();
                String nomTienda = mNombreTienda.getText().toString();
                String idTienda = mUserID+"";
                miNegocio.put("nombreProducto", nombreProducto);
                miNegocio.put("descripcionProducto", descripcionProducto);
                miNegocio.put("precioProducto", precioProducto);
                miNegocio.put("nombreTienda", nomTienda);
                miNegocio.put("fotoProducto",descargaUriFoto.toString());
                miNegocio.put("idNegocio",idTienda);



                mFirestone.collection("publicaciones_productos").document().set(miNegocio).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegistroProductos.this, "Se ha Registrado su producto", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistroProductos.this,MiNegocio.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroProductos.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}