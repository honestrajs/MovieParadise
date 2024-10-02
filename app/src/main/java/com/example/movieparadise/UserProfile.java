package com.example.movieparadise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity {
    TextView Name;
    TextView Phone,Age;
    ImageView profilePic;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Name=findViewById(R.id.username);
        Phone=findViewById(R.id.phoneNumber);
        Age=findViewById(R.id.age);
        profilePic=findViewById(R.id.profile_photo);

       firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users");


        Query query=databaseReference.orderByChild("phone").equalTo(user.getPhoneNumber());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String name = "" + ds.child("name").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String prof_url= (String) ds.child("profile_url").getValue();
                    String AGE=""+ds.child("age").getValue();
                    Phone.setText(phone);
                    Name.setText(name);
                    Age.setText(AGE);
                    try {
                        Picasso.get().load(prof_url).into(profilePic);
                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.profile).into(profilePic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Intent intent=new Intent(UserProfile.this,homepage.class);
                startActivity(intent);
                finish();
            }
        });

    }
}