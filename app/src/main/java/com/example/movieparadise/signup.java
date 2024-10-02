package com.example.movieparadise;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class signup extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Button selectProfilePic,registerbtn,verifybtn;
    ImageView profile_pic;
    EditText Verification,Phone,Pass,RePass,Name,Age;
    FirebaseAuth mauth;
    String codeSent;
    CountryCodePicker countryCodePicker;
    ProgressDialog progressDialog;
    DatabaseReference reference;
    StorageReference Reference;
    Uri ImageUri;
    String ProfileURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        registerbtn=findViewById(R.id.buttonregister);
        verifybtn=findViewById(R.id.buttonverify);
       Name=findViewById(R.id.name);
       countryCodePicker=findViewById(R.id.codePick_SignUp);
        Phone=findViewById(R.id.phone);
        Pass=findViewById(R.id.password);
        RePass=findViewById(R.id.repassword);
       Verification=findViewById(R.id.VerificationCode);
       selectProfilePic=findViewById(R.id.select_profile);
       profile_pic=findViewById(R.id.profile_photo);
       Age=findViewById(R.id.age);
       //profile Pic storage location
       Reference = FirebaseStorage.getInstance().getReference("UserProfilePics");

        mauth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(signup.this);


        selectProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                sendVerificationCode();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                verifyRegisterCode();

            }
        });

    }
    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            data.getData();
            ImageUri = data.getData();
            profile_pic.setImageURI(ImageUri);
        }
    }

    public void verifyRegisterCode(){

        String VerificationCode=Verification.getEditableText().toString();
        if (!VerificationCode.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, VerificationCode);
            signInWithPhoneAuthCredential(credential);
        }else{
            progressDialog.dismiss();
            Verification.setError("Enter Verification Code");
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Storing User Data
                            storeUserData();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(signup.this,register_success.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(),"Invalid Verification Code",Toast.LENGTH_SHORT).show();
                            }
                            }
                        }
                });
    }

    public void storeUserData() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");


        String UPhone=Phone.getText().toString();
        String UName=Name.getText().toString();
        String UAge=Age.getText().toString();
        String UPass=Pass.getText().toString();
        String PhoneNumber="+"+countryCodePicker.getFullNumber()+UPhone;
        String ImageUrl = UName + "." + getExt(ImageUri);
        final StorageReference Reference1 = Reference.child(ImageUrl);

Reference1.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

Reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
        ProfileURL=uri.toString();
        helper helper=new helper(UName,PhoneNumber,UPass,ProfileURL,UAge);
        reference.child(PhoneNumber).setValue(helper);
    }
});
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(),"Check Your Connection",Toast.LENGTH_SHORT).show();
    }
});



    }
    //get ProfilePic extension
    public String getExt(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Sending Verification Code
    public void sendVerificationCode(){

        String phoneNumber=Phone.getText().toString();
        String name=Name.getText().toString();
        String pass=Pass.getText().toString();
        String repass=RePass.getText().toString();
        String PhoneNum="+"+countryCodePicker.getFullNumber()+phoneNumber;

        reference=FirebaseDatabase.getInstance().getReference("Users");
        Query checkuser=reference.orderByChild("phone").equalTo(PhoneNum);

        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
            progressDialog.dismiss();
            Phone.setError("User Already Exists");
            Phone.requestFocus();
            }
            else {
             Phone.setError(null);
             if (phoneNumber.isEmpty() || name.isEmpty() || pass.isEmpty() || repass.isEmpty()){
              progressDialog.dismiss();
               Toast.makeText(getApplicationContext(),"All Fields are Required",Toast.LENGTH_SHORT).show();
                }else
                 if (phoneNumber.length() < 10 && phoneNumber.length() >= 1){
                  progressDialog.dismiss();
                   Phone.setError("Enter a Valid Phone Number");
                    Phone.requestFocus();
                       return;
                        }else
                        if (!pass.equals(repass)){
                          progressDialog.dismiss();
                            RePass.setError("Password Not Matching");
                            }
                            else{
                             PhoneAuthProvider.getInstance().verifyPhoneNumber(
                              PhoneNum,        // Phone number to verify
                              60,                 // Timeout duration
                               TimeUnit.SECONDS,   // Unit of timeout
                               signup.this,               // Activity (for callback binding)
                              mCallbacks);        // OnVerificationStateChangedCallbacks
                               }
                                }
                               }

                                @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

              
                                  }});

    }
PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                  progressDialog.dismiss();
                  Toast.makeText(getApplicationContext(),"Verification Code sent",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed to get Verification code",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        super.onCodeSent(s, forceResendingToken);
        codeSent=s;
        Toast.makeText(getApplicationContext(),"Verification Code sent",Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }
};
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),login.class);
        startActivity(intent);
        finish();
    }
}