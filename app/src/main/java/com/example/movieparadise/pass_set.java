package com.example.movieparadise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyStore;

public class pass_set extends AppCompatActivity {
    long backPressedTime;
    Toast backToast;
    EditText newpass,repass;
    Button change_pass;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_set);

        newpass=findViewById(R.id.password);
        repass=findViewById(R.id.repassword);
        change_pass=findViewById(R.id.buttonchange_pass);

        progressDialog=new ProgressDialog(pass_set.this);

        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String re_pass=repass.getEditableText().toString().trim();
                String new_pass=newpass.getEditableText().toString().trim();
                String phone_num=getIntent().getStringExtra("phoneNumber");

                if (new_pass.equals(re_pass)){
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);
                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                reference.child(phone_num).child("password").setValue(new_pass);
                    Intent intent=new Intent(pass_set.this,success.class);
                    progressDialog.dismiss();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                    finish();
            }
            else {
                repass.setError("Password Mismatch");
                repass.requestFocus();
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            Intent intent=new Intent(getApplicationContext(),login.class);
           startActivity(intent);
           finish();
            return;
        }else {
            backToast=Toast.makeText(getBaseContext(),"Press back again to cancel",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime=System.currentTimeMillis();
    }
}