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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisroNegocio extends AppCompatActivity {

    private Toolbar mToolDetallesproducto;
    private CircleImageView mCIVperfilNegocio;
    private ImageView mIVBanner;
    private EditText mNomMiNegocio, mDesMiNegocio;
    private Button mBtnCrearNegocio;

    private FirebaseFirestore mFirestone;
    private StorageReference mStorageReference;
    private String mUserID;
    private Uri mImageUri;
    private Uri mImageUribaner;
    private Uri DesgargaUriFotoPerfil = null;
    private Uri DesgargaUriFotoBaner = null;

    private String ImagenSelecionada="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisro_negocio);


        Instancias();

        mToolDetallesproducto = findViewById(R.id.toolbar_RegisNegocio);
        setSupportActionBar(mToolDetallesproducto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mBtnCrearNegocio.setOnClickListener(v -> CrearMiNegocio());

//        permite seleccionar la foto de la galeria
        mCIVperfilNegocio.setOnClickListener(v -> {
            //solicita los permisos para acceder a la memoria y seleccionar uan foto.
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(RegisroNegocio.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(RegisroNegocio.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    //Recorta la imagen para que se acomode al imageview de tipo circular.
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1,1)
                            .start(RegisroNegocio.this);
                    ImagenSelecionada="fotoPerfil";
                }
            }
        });


        mIVBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //solicita los permisos para acceder a la memoria y seleccionar uan foto.
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(RegisroNegocio.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(RegisroNegocio.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else{
                        //Recorta la imagen para que se acomode al imageview de tipo circular.
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(16,6)
                                .start(RegisroNegocio.this);
                        ImagenSelecionada="fotoBaner";
                    }
                }

            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.general_sin_opciones,menu);
        return true;
    }

    private void Instancias(){
        mNomMiNegocio = findViewById(R.id.inputNombreNegocio);
        mDesMiNegocio = findViewById(R.id.imputDescripMiNegocio);
        mBtnCrearNegocio = findViewById(R.id.BtnCrearNegocio);
        mIVBanner = findViewById(R.id.IV_BannerNegocio);
        mCIVperfilNegocio = findViewById(R.id.IV_PerfilNegocio);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mFirestone = FirebaseFirestore.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();


        assert mUser != null;
        mUserID = mUser.getUid();
    }

    private void CrearMiNegocio(){

        StorageReference filepath = mStorageReference.child("FotoNegocios").child(mUserID);
        StorageReference fotoDeperfil = filepath.child("fotodePerfil.jpg");
        fotoDeperfil.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    StorageReference fotoBaner = filepath.child("fotoBaner.jpg");
                    fotoBaner.putFile(mImageUribaner).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                saveToFirestone(fotoDeperfil, fotoBaner);
                            }else{
                                Toast.makeText(RegisroNegocio.this, "NO SE SUBIO A FIRESTORE", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void saveToFirestone(StorageReference fotoDeperfil, StorageReference fotoBaner) {
        fotoDeperfil.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DesgargaUriFotoPerfil = uri;
                Toast.makeText(RegisroNegocio.this, "se descargo perfil foto", Toast.LENGTH_SHORT).show();
                fotoBaner.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DesgargaUriFotoBaner= uri;

                        Toast.makeText(RegisroNegocio.this, "se descargo baner foto", Toast.LENGTH_SHORT).show();

                        String NombreMiNegocio = mNomMiNegocio.getText().toString();
                        String DescripcionMinegocio = mDesMiNegocio.getText().toString();

                        Map<String,Object> miNegocio= new HashMap<>();
                        miNegocio.put("nombreNegocio", NombreMiNegocio);
                        miNegocio.put("descNegocio", DescripcionMinegocio);
                        miNegocio.put("fotoPerfil",DesgargaUriFotoPerfil.toString());
                        miNegocio.put("fotoBaner",DesgargaUriFotoBaner.toString());

                        mFirestone.collection("Negocios").document(mUserID+"").set(miNegocio).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisroNegocio.this, "Se ha creado su Negocio", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisroNegocio.this,MiNegocio.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisroNegocio.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });



    }


    @Override
    //para conocer la informacion de la imagen seleccionada en el recorte de imagen.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (ImagenSelecionada){
            case "fotoPerfil":
                if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(resultCode == RESULT_OK){
                        mImageUri= result.getUri();
//                        Glide.with(this).load(mImageUribaner).centerInside().into(mCIVperfilNegocio);
                        mCIVperfilNegocio.setImageURI(mImageUri);
                    }
                    else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                        Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                ImagenSelecionada = "";
                break;
            case "fotoBaner":

                if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(resultCode == RESULT_OK){
                        mImageUribaner= result.getUri();
                        Glide.with(this).load(mImageUribaner).centerInside().into(mIVBanner);
//                        mIVBanner.setImageURI(mImageUribaner);
                    }
                    else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                        Toast.makeText(this, result.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                ImagenSelecionada = "";
                break;

        }
    }
}