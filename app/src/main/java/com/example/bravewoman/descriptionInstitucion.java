package com.example.bravewoman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import java.util.Objects;

public class descriptionInstitucion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_institucion);

        Toolbar mToolDetalleInstitucion = findViewById(R.id.toolbar_DetallesInstitucion);
        setSupportActionBar(mToolDetalleInstitucion);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}