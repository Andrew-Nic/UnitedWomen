package com.example.bravewoman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificacionDeCuenta extends AppCompatActivity {
    private Button mbtnEnviarCorreo,mbtnAcualizar, mbtnContinuar;
    private TextView mTvMenActualizar,mTvTitVerificacion;
    private FirebaseAuth mAunth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_de_cuenta);

        //instancias del activity
        mbtnEnviarCorreo = findViewById(R.id.btnEnviarcorreo);
        mbtnAcualizar = findViewById(R.id.btnactualizar);
        mbtnContinuar = findViewById(R.id.btncontinuar);
        mTvTitVerificacion= findViewById(R.id.TvTituloVerificacion);
        mTvMenActualizar= findViewById(R.id.mensajeAtualizar);

        mTvMenActualizar.setVisibility(View.GONE);
        mbtnContinuar.setVisibility(View.GONE);
        mbtnAcualizar.setVisibility(View.GONE);

        //Instancias de firebase
        mAunth = FirebaseAuth.getInstance();
        user = mAunth.getCurrentUser();


        mbtnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerificacionDeCuenta.this, MainActivity.class));
                finish();
            }
        });


        mbtnAcualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });


        mbtnEnviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReenviarcorreoVerificacion();
            }
        });
    }

    private void ReenviarcorreoVerificacion(){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mTvMenActualizar.setVisibility(View.VISIBLE);
                    mbtnAcualizar.setVisibility(View.VISIBLE);
                    Toast.makeText(VerificacionDeCuenta.this, "Se ha enviado aun email a: " +user.getEmail(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(VerificacionDeCuenta.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void reload(){
       user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   updateUI();
               }else{
                   Toast.makeText(VerificacionDeCuenta.this, "No se pudo recargar", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    private void updateUI(){
        if(user != null) {
            if(user.isEmailVerified()){
                mTvTitVerificacion.setText("Tu cuenta ha sido verificada, presiona en Continuar para ver nuestro contenido");
                mbtnContinuar.setVisibility(View.VISIBLE);
                mbtnEnviarCorreo.setEnabled(false);
                mbtnAcualizar.setEnabled(false);
            }else{
                Toast.makeText(this, "No se ha verificado al usuario", Toast.LENGTH_SHORT).show();
            }
        }else{
            startActivity(new Intent(VerificacionDeCuenta.this, Login.class));
            finish();
        }
    }

}