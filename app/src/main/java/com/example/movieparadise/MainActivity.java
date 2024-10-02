package com.example.movieparadise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    ImageView splash;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

}
    @Override
    protected void onStart() {
        super.onStart();

        splash=findViewById(R.id.splash);
        splash.animate().alpha(4000).setDuration(0);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
               if(firebaseUser != null){
                    Intent dsp=new Intent(MainActivity.this,homepage.class);
                    startActivity(dsp);
                    finish();
                } else{
                    Intent ds=new Intent(MainActivity.this,login.class);
                    startActivity(ds);
                    finish();
               }
            }
        },4000);

    }

}