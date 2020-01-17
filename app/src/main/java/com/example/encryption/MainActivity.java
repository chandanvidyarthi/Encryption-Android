package com.example.encryption;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnEncrypt, btnDecrypt;
    TextView textViewEncrypted, textViewDecrypted;
    private static String publicKey = "";
    private static String privateKey = "";
    private EditText inputText;
    byte[] encodeData = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText = findViewById(R.id.et_text);
        btnEncrypt = findViewById(R.id.btn_encrypt_data);
        btnDecrypt = findViewById(R.id.btn_decrypt_data);
        textViewEncrypted = findViewById(R.id.text_encrypt_text);
        textViewDecrypted = findViewById(R.id.text_decrypt_text);
        initKeys();
        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                encryptData(inputText.getText().toString());
            }
        });
        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrptData();
            }
        });

    }

    public void initKeys() {
        try {
            Map<String, Object> keyMap = RSA.initKey();
            publicKey = RSA.getPublicKey(keyMap);
            privateKey = RSA.getPrivateKey(keyMap);
            Log.d(MainActivity.class.getSimpleName(), "Public Key :  " + publicKey);
            Log.d(MainActivity.class.getSimpleName(), "Private Key :  " + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void encryptData(String text) {
        if (publicKey == null || privateKey == null) {
            Toast.makeText(getApplicationContext(), "Please generate public and private key", Toast.LENGTH_LONG);
        } else {
            byte[] userData = text.getBytes();
            try {
                encodeData = RSA.encryptByPublicKey(userData, publicKey);
                String encodeStr = new BigInteger(1, encodeData).toString(16);
                Log.d(MainActivity.class.getSimpleName(), "Encrypted Data " + encodeStr);
                textViewEncrypted.setText(encodeStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void decrptData() {
        if (encodeData == null) {
            Toast.makeText(getApplicationContext(), "Please use public key to encrypt", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] decodeData = RSA.decryptByPrivateKey(encodeData, privateKey);
            String decodeStr = new String(decodeData);
            Log.d(MainActivity.class.getSimpleName(), "Decrpt Data " + decodeStr);
            textViewDecrypted.setText(decodeStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
