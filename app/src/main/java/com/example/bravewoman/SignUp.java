package com.example.bravewoman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private TextInputLayout mSignCorreo, mSingContraseña;
    private Button mbtnRegistarme,mbtnTengoCuenta;

    private FirebaseAuth mAuth;
    private String email = "";
    private String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSignCorreo = findViewById(R.id.correo_SignUp);
        mSingContraseña = findViewById(R.id.contraseña_SignUp);
        mbtnRegistarme = findViewById(R.id.btnRegistarse_SignUp);
        mbtnTengoCuenta = findViewById(R.id.btntengoCuenta_SignUp);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mbtnRegistarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mSignCorreo.getEditText().getText().toString();
                password = mSingContraseña.getEditText().getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    if(password.length()>=6){

                        registrarUsuario();

                    }else {
                        mSingContraseña.setError("la contraseña debe tener almenos 6 caracteres");
                    }
                }else{
                    Toast.makeText(SignUp.this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mbtnTengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });

    }

    private void registrarUsuario(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUp.this, "Se ha creado el usuario", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}