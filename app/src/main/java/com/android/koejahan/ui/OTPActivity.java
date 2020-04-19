package com.android.koejahan.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.koejahan.MainActivity;
import com.android.koejahan.R;
import com.android.koejahan.data.SharedPreferenceHelper;
import com.android.koejahan.model.User;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
    private String kodeAsli = "";
    private String otpAsli = "";
    private String phone="";
    private EditText otp;
    private TextView timer;
    private String tagPhone = "Mauli-PHONE-Success";
    private String tagOtp = "Mauli-OTP-Success";
    private FirebaseAuth mAuth;
    private String valRandom="";
    private String dummyCode = "123456";
    private PublicKey pubkey;
    private PrivateKey privkey;
    private LovelyProgressDialog waitingDialog;
    private ConstraintLayout cly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp = findViewById(R.id.editTextOTP);
        timer = findViewById(R.id.tv_timer);
        cly = findViewById(R.id.verifyOTPLayout);
        mAuth = FirebaseAuth.getInstance();
        waitingDialog = new LovelyProgressDialog(this).setCancelable(false);
        phone = getIntent().getStringExtra(tagPhone);
        StringBuilder j = new StringBuilder();
        waitingDialog.setIcon(R.drawable.ic_person_low)
                .setTitle("Mengirim OTP ke nomor teleponmu....")
                .setTopColorRes(R.color.colorPrimaryDark)
                .show();
        sendVerificationCode(phone); //send otp automaticly
        kirimulangTampilan();
    }

    //method countime
    private void kirimulangTampilan(){
        new CountDownTimer(60000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                View child = cly.getViewById(R.id.buttonVerify2);
                child.setVisibility(View.GONE);
                timer.setText("Kirim Ulang SMS : "+millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                timer.setText("Kirim Ulang SMS : 0");
                View child = cly.getViewById(R.id.buttonVerify2);
                View child2 = cly.getViewById(R.id.tv_timer);
                child2.setVisibility(View.GONE);
                child.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    //method to send sms verification
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        waitingDialog.dismiss();

    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String kodeAsli2 = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (kodeAsli2 != null) {
                otp.setText(kodeAsli2);
                waitingDialog.setIcon(R.drawable.ic_person_low)
                        .setTitle("Menyimpan Info User....")
                        .setTopColorRes(R.color.colorPrimaryDark)
                        .show();
                //verifying the code
                verifyVerificationCode(kodeAsli2);
            }else{
                otp.setText("gagal");
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("EXCEPTION FIREBASE",e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            valRandom = s;
            Log.d("codesent", "onCodeSent:" + s);
            Toast.makeText(OTPActivity.this,"Kode terkirim",Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(valRandom, code);
        Log.d("RANDOMNUMBER","nilai random = "+valRandom);
        try {
            final User userInfo = new User();
            FirebaseDatabase.getInstance().getReference().child("user/" ).orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                    userInfo.email = (String) mapMessage.get("email");
                    userInfo.name = (String) mapMessage.get("name");
                    userInfo.avata = (String) mapMessage.get("avata");
                    userInfo.id = dataSnapshot.getKey();
                    SharedPreferenceHelper.getInstance(OTPActivity.this).saveUserInfo(userInfo);
                    SharedPreferenceHelper.getInstance(OTPActivity.this).saveSPBoolean(SharedPreferenceHelper.SP_SUDAH_LOGIN,true);
                    Toast.makeText(OTPActivity.this,"Kode otp yang diterima = "+kodeAsli,Toast.LENGTH_SHORT).show();
                    waitingDialog.dismiss();
                    Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        //signing the user
        //signInWithPhoneAuthCredential(credential);
    }

    public void cekOtp(View view) {
        String getotp = otp.getText().toString();
        try {
            final User userInfo = new User();
            waitingDialog.setIcon(R.drawable.ic_person_low)
                    .setTitle("Menyimpan informasi User....")
                    .setTopColorRes(R.color.colorPrimaryDark)
                    .setCancelable(true)
                    .show();
            verifyVerificationCode(getotp);
//            if (getotp.equals(dummyCode)){
//                FirebaseDatabase.getInstance().getReference().child("user/" ).orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
//                        userInfo.email = (String) mapMessage.get("email");
//                        userInfo.name = (String) mapMessage.get("name");
//                        userInfo.avata = (String) mapMessage.get("avata");
//                        userInfo.id = dataSnapshot.getKey();
//                        userInfo.bio = (String) mapMessage.get("bio");
//                        SharedPreferenceHelper.getInstance(OTPActivity.this).saveUserInfo(userInfo);
//                        SharedPreferenceHelper.getInstance(OTPActivity.this).saveSPBoolean(SharedPreferenceHelper.SP_SUDAH_LOGIN,true);
//                        waitingDialog.dismiss();
//                        Toast.makeText(OTPActivity.this,"Kode otp yang diterima = "+kodeAsli,Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(OTPActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }else{
//                Toast.makeText(this,"Kode otp dummy 1-6 cuk !",Toast.LENGTH_SHORT).show();
//                waitingDialog.dismiss();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendGain(View view) {
        sendVerificationCode(phone);
        View child2 = cly.getViewById(R.id.tv_timer);
        child2.setVisibility(View.VISIBLE);
        kirimulangTampilan();
        Toast.makeText(this,"trying...",Toast.LENGTH_SHORT).show();
    }
}



