package com.example.bravewoman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.webkit.WebView;

import java.util.Objects;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar mToolsitioweb = findViewById(R.id.toolbar_sitioweb);
        setSupportActionBar(mToolsitioweb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        WebView myWebView = findViewById(R.id.sitioWeb);
        myWebView.loadUrl("https://www.google.com");


    }
}