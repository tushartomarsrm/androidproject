package com.example.project_1;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Fifteen extends AppCompatActivity {
    EditText e1;
    Button b1;
    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifteen);
        phonenumber=getIntent().getStringExtra("mobile").toString();
        e1=(EditText) findViewById(R.id.editText11);
        b1=(Button) findViewById(R.id.button30);
        mAuth=FirebaseAuth.getInstance();
        initiateotp(phonenumber);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.getText().toString().isEmpty()){
                    Toast.makeText(Fifteen.this, "Blank Field!", Toast.LENGTH_SHORT).show();
                }
                else if(e1.getText().toString().length()!=6){
                    Toast.makeText(Fifteen.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,e1.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }
    private void initiateotp(String phonenumber)
    {
        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phonenumber)
                                        .setTimeout(60L,TimeUnit.SECONDS)
                                                .setActivity(this)
                                                        .setCallbacks(mCallBack)
                                                                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    otpid=s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(Fifteen.this, "ERROR!", Toast.LENGTH_SHORT).show();
                }
            };
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Fifteen.this,Third.class));
                            finish();

                        } else {
                            Toast.makeText(Fifteen.this, "ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}