package com.android.koejahan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.koejahan.CryptoLib.RSA;
import com.android.koejahan.R;
import com.android.koejahan.data.ProsesLogin;
import com.android.koejahan.data.StaticConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
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
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OtpFirebaseGagal {
}
//public class OTPActivity extends AppCompatActivity {
//    private String kodeAsli = "";
//    private String otpAsli = "";
//    private String phone="";
//    private EditText otp;
//    private String tagPhone = "Mauli-PHONE-Success";
//    private String tagOtp = "Mauli-OTP-Success";
//    private FirebaseAuth mAuth;
//    private String valRandom="";
//    private String dummyCode = "123456";
//    private PublicKey pubkey;
//    private PrivateKey privkey;
//    private LovelyProgressDialog waitingDialog;
//    private Handler handler ;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otp);
//        otp = findViewById(R.id.editTextOTP);
//        mAuth = FirebaseAuth.getInstance();
//        waitingDialog = new LovelyProgressDialog(this).setCancelable(false);
//        handler = new Handler();
//        phone = getIntent().getStringExtra(tagPhone);
//        StringBuilder j = new StringBuilder();
//        kodeAsli = doGenerate(j);
//        waitingDialog.setIcon(R.drawable.ic_person_low)
//                .setTitle("Mengirim OTP ke nomor teleponmu....")
//                .setTopColorRes(R.color.colorPrimaryDark)
//                .show();
//        try{
//            pubkey = RSA.readPublicKey(OTPActivity.this);
//            privkey = RSA.readPrivatekey(OTPActivity.this);
//        }catch(Exception e){
//            waitingDialog.dismiss();
//            Log.d("EROR_OTP","READ PUB DAN PRIV GAGAL");
//        }
//        sendVerificationCode(phone); //send otp automaticly
//        //waitingDialog.dismiss();
//    }
//
//    //method to send sms verification
//    private void sendVerificationCode(String mobile) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                mobile,
//                60,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallbacks);
//        waitingDialog.dismiss();
//
//    }
//
//    //the callback to detect the verification status
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//            //Getting the code sent by SMS
//            String kodeAsli2 = phoneAuthCredential.getSmsCode();
//
//            //sometime the code is not detected automatically
//            //in this case the code will be null
//            //so user has to manually enter the code
//            if (kodeAsli2 != null) {
//                otp.setText(kodeAsli2);
//                waitingDialog.setIcon(R.drawable.ic_person_low)
//                        .setTitle("Mengirim QR Code ke Email....")
//                        .setTopColorRes(R.color.colorPrimaryDark)
//                        .show();
//                //verifying the code
//                verifyVerificationCode(kodeAsli);
//            }else{
//                otp.setText("kokgagal");
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//
//            //storing the verification id that is sent to the user
//            valRandom = s;
//            Log.d("codesent", "onCodeSent:" + s);
//        }
//    };
//
//    private void verifyVerificationCode(String code) {
//        //creating the credential
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(valRandom, code);
//        Log.d("RANDOMNUMBER","nilai random = "+valRandom);
//        try {
//            final ProsesLogin login = new ProsesLogin();
//            final String Sign_secval =  RSA.sign(kodeAsli,privkey);
//            final String publik_key = pubkey.toString();
//
//            FirebaseDatabase.getInstance().getReference().child("user/" ).orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    String userid = dataSnapshot.getKey();
//                    HashMap mapMessage = (HashMap) dataSnapshot.getValue();
//                    String email = (String) mapMessage.get("email");
//                    String nama = (String) mapMessage.get("name");
//                    String avatar = (String) mapMessage.get("avata");
//                    login.initUpdateUser(kodeAsli,userid,email,phone,nama,avatar,publik_key);
//
//                    Toast.makeText(OTPActivity.this,"Kode otp yang diterima = "+kodeAsli,Toast.LENGTH_SHORT).show();
//                    //sendEmailQR(phone,email,Sign_secval);
//                    waitingDialog.dismiss();
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //signing the user
//        //signInWithPhoneAuthCredential(credential);
//    }
//
//    public Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Intent intent = new Intent(OTPActivity.this, ScanActivity.class);
//            intent.putExtra(tagOtp,kodeAsli);
//            intent.putExtra(tagPhone,phone);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
//    };
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            //verification successful we will start the profile activity
//                            try {
//                                final ProsesLogin login = new ProsesLogin();
//                                final String Sign_secval =  RSA.sign(kodeAsli,privkey);
//                                final String publik_key = pubkey.toString();
//
//                                FirebaseDatabase.getInstance().getReference().child("user/" ).orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
//                                    @Override
//                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                        String userid = dataSnapshot.getKey();
//                                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
//                                        String email = (String) mapMessage.get("email");
//                                        String nama = (String) mapMessage.get("name");
//                                        String avatar = (String) mapMessage.get("avata");
//                                        login.initUpdateUser(kodeAsli,userid,email,phone,nama,avatar,publik_key);
//
//                                        Toast.makeText(OTPActivity.this,"Kode otp yang diterima = "+kodeAsli,Toast.LENGTH_SHORT).show();
//                                        sendEmailQR(phone,email,Sign_secval);
//                                        waitingDialog.dismiss();
//                                    }
//
//                                    @Override
//                                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                                    }
//
//                                    @Override
//                                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } else {
//
//                            //verification unsuccessful.. display an error message
//                            waitingDialog.dismiss();
//                            String message = "Somthing is wrong, we will fix it soon...";
//                            Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                message = "Invalid code entered...";
//                                Toast.makeText(OTPActivity.this, message, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
//
//
//    public void cekOtp(View view) {
//        String getotp = otp.getText().toString();
//        try {
//            waitingDialog.setIcon(R.drawable.ic_person_low)
//                    .setTitle("Memeriksa Nomor Telpon....")
//                    .setTopColorRes(R.color.colorPrimaryDark)
//                    .show();
////            final ProsesLogin login = new ProsesLogin();
////            final String Sign_secval =  RSA.sign(kodeAsli,privkey);
////            final String publik_key = pubkey.toString();
//            verifyVerificationCode(getotp);
////            //if (getotp.equals(dummyCode)){
////                FirebaseDatabase.getInstance().getReference().child("user/" ).orderByChild("phone").equalTo(phone).addChildEventListener(new ChildEventListener() {
////                    @Override
////                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////                        String userid = dataSnapshot.getKey();
////                        HashMap mapMessage = (HashMap) dataSnapshot.getValue();
////                        String email = (String) mapMessage.get("email");
////                        String nama = (String) mapMessage.get("name");
////                        String avatar = (String) mapMessage.get("avata");
////                        login.initUpdateUser(kodeAsli,userid,email,phone,nama,avatar,publik_key);
////                        waitingDialog.dismiss();
////                        Toast.makeText(OTPActivity.this,"Kode otp yang diterima = "+kodeAsli,Toast.LENGTH_SHORT).show();
////                        sendEmailQR(phone,email,Sign_secval);
////                    }
////
////                    @Override
////                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                    }
////
////                    @Override
////                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
////
////                    }
////
////                    @Override
////                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                    }
////                });
////            }else{
////                Toast.makeText(this,"Kode otp dummy 1-6 cuk !",Toast.LENGTH_SHORT).show();
////                waitingDialog.dismiss();
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    public boolean sendEmailQR(final String phone, final String email, final String signcrypt){
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, StaticConfig.SendEmail_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //If we are getting success from server
//                        if (response.contains(StaticConfig.responsePublickeySend)) {
//                            //loading = ProgressDialog.show(OTPActivity.this,"Mengirim...","Mohon Tunggu...",false,false);
//                            //handler.postDelayed(runnable,2000);
//                            Intent intent = new Intent(OTPActivity.this, ScanActivity.class);
//                            intent.putExtra(tagOtp,kodeAsli);
//                            intent.putExtra(tagPhone,phone);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(OTPActivity.this,"Cek koneksi atau server diluar jangkauan !",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(OTPActivity.this,"Error volley",Toast.LENGTH_SHORT).show();
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put(StaticConfig.KEY_PHONE,phone);
//                params.put(StaticConfig.KEY_EMAIL,email);
//                params.put(StaticConfig.KEY_Enkripsi_SecretVal,signcrypt);
//
//                return params;
//            }
//
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//        return true;
//    }
//
//    private String doGenerate(StringBuilder sb){
//        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
//        sb = new StringBuilder(10);
//        Random random = new Random();
//        for (int i = 0; i < 8; i++) {
//            char c = chars[random.nextInt(chars.length)];
//            sb.append(c);
//        }
//        return sb.toString();
//    }
//
//    public void sendGain(View view) {
//        sendVerificationCode(phone);
//        Toast.makeText(this,"trying...",Toast.LENGTH_SHORT).show();
//    }
//}



