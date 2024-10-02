package com.example.movieparadise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class contactus extends AppCompatActivity {
    ImageView insta;
    ImageView facebook;
    ImageView twitter;
    ImageView whatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        insta=findViewById(R.id.insta);
        facebook=findViewById(R.id.facebook);
        twitter=findViewById(R.id.twitter);
        whatsapp=findViewById(R.id.whatsapp);

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://www.instagram.com/thalapathiyin_thambi/");
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://www.facebook.com/appu.papu.562");
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://twitter.com/Honest_offl");
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("smsto:"+"8667561605");
                Intent whatsappintent=new Intent(Intent.ACTION_SENDTO,uri);
                whatsappintent.setPackage("com.whatsapp");
                startActivity(whatsappintent);
            }
        });
    }

    private void gotoUrl(String s){
        Uri uri=Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}