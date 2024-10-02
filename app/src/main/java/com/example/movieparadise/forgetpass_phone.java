package com.example.movieparadise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class forgetpass_phone extends AppCompatActivity {

    Button get_otp,verify_otp;
    EditText Verification,Phone;
    CountryCodePicker countryCodePicker;
    String codeSent;
    ProgressDialog progressDialog;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass_phone);

        get_otp=findViewById(R.id.button_get_otp);
        verify_otp=findViewById(R.id.button_verify);
        countryCodePicker=findViewById(R.id.codePick_pass);
        Verification=findViewById(R.id.VerificationCode);
        Phone=findViewById(R.id.phone);

        mauth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(forgetpass_phone.this);

        get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                sendVerificationCode();
            }
        });
        verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                verifyRegisterCode();
            }
        });

    }

    private void verifyRegisterCode() {
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
                            String phoneNumber=Phone.getText().toString();
                            String PhoneNum="+"+countryCodePicker.getFullNumber()+phoneNumber;
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Verified Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(forgetpass_phone.this,pass_set.class);
                            intent.putExtra("phoneNumber",PhoneNum);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Invalid Verification Code",Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(),"Invalid Verification Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    public void sendVerificationCode() {
        String phoneNumber=Phone.getText().toString();
        String PhoneNum="+"+countryCodePicker.getFullNumber()+phoneNumber;

        if (phoneNumber.isEmpty()){
            progressDialog.dismiss();
            Phone.setError("Enter PhoneNumber");
            Phone.requestFocus();
        }else
        if (phoneNumber.length() < 10 && phoneNumber.length() >= 1){
            progressDialog.dismiss();
            Phone.setError("Enter a Valid Phone Number");
            Phone.requestFocus();
            return;
        }
        else{
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    PhoneNum,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    forgetpass_phone.this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }

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