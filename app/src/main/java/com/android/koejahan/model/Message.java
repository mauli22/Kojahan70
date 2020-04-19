package com.android.koejahan.model;


import javax.crypto.SecretKey;

public class Message{
    public String idSender;
    public String idReceiver;
    public String text;
    public byte[] tmp_text;
    public byte[] tmp_key;
    public long timestamp;
    public String enc_key;
    public SecretKey skey;
    public boolean isseen;
}