package com.example.movieparadise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class login extends AppCompatActivity {
Button signupbtn,loginbtn;
EditText PhoneNum,Pass;
TextView forget_pass;
CountryCodePicker countryCodePicker;
ProgressDialog progressDialog1;
DatabaseReference reference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupbtn=findViewById(R.id.buttonsignup);
        loginbtn=findViewById(R.id.Login_Button);
        PhoneNum=findViewById(R.id.phone);
        Pass=findViewById(R.id.password);
        forget_pass=findViewById(R.id.forget_password);
        progressDialog1=new ProgressDialog(login.this);

        countryCodePicker=findViewById(R.id.codePick_Login);

        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),forgetpass_phone.class);
                startActivity(intent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String phoneN=PhoneNum.getEditableText().toString();
               String pass=Pass.getEditableText().toString();
               String PhoneNumber="+"+countryCodePicker.getFullNumber()+phoneN;
               if (!phoneN.isEmpty() && !pass.isEmpty()) {
                   progressDialog1.show();
                   progressDialog1.setContentView(R.layout.progress_dialog_login);
                   progressDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                   reference=FirebaseDatabase.getInstance().getReference("Users");

                   Query checkuser=reference.orderByChild("phone").equalTo(PhoneNumber);

                   checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          if (dataSnapshot.exists()){

                              PhoneNum.setError(null);
                              String passFromDb=dataSnapshot.child(PhoneNumber).child("password").getValue().toString();
                              if (passFromDb.equals(pass)){
                                  progressDialog1.dismiss();
                                  Toast.makeText(getApplicationContext(),"Logged IN Successfully",Toast.LENGTH_SHORT).show();
                                  Intent intent=new Intent(login.this,homepage.class);
                                  startActivity(intent);
                                  finish();
                              }else{
                                  progressDialog1.dismiss();
                                  Pass.setError("Wrong Password");
                                  Pass.requestFocus();
                              }
                          }
                          else {
                              progressDialog1.dismiss();
                              PhoneNum.setError("No such User");
                              PhoneNum.requestFocus();
                          }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       progressDialog1.dismiss();
                       Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                       }
                   });

               }else {

                   Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
               }
           }
       });
        signupbtn.setOnClickListener(view -> {
            Intent in=new Intent(login.this,signup.class);
            startActivity(in);
            finish();
        });
    }
}