package com.example.bravewoman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputLayout mlogCorreo, mlogContraseña;
    private Button mbtnOlvideContraseña,mbtnIniciar,mbtnNoTengoCuenta;

    private FirebaseAuth mAuth;

    private String email = "";
    private String password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mlogCorreo = findViewById(R.id.correo_LogIn);
        mlogContraseña = findViewById(R.id.contraseña_LogIn);
        mbtnOlvideContraseña = findViewById(R.id.btnRecuperarContraseña_LogIn);
        mbtnIniciar = findViewById(R.id.btnIniciar_LogIn);
        mbtnNoTengoCuenta = findViewById(R.id.btnNuevoUsuario_LogIn);

        mAuth = FirebaseAuth.getInstance();

        mbtnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mlogCorreo.getEditText().getText().toString();
                password = mlogContraseña.getEditText().getText().toString();
                if (!email.isEmpty() && !password.isEmpty()){
                    login();
                } else{
                    Toast.makeText(Login.this, "Depe llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mbtnNoTengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });
    }

    private void login() {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}