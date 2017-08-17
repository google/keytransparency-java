package com.google.keytransparency.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.keytransparency.exampleapp.R;
import com.google.keytransparency.KTClient;
import com.google.keytransparency.KeyTransparencyException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeyTransparencyExample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_transparency_example);
        TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText("");
        tv.setMovementMethod(new ScrollingMovementMethod());

        try{
            tv.append("\n\n --- GetEntry test using library --- \n");

            KTClient client = new KTClient("35.184.134.53:8080", 1, getApplicationContext(), R.raw.server, R.raw.vrf_pubkey, R.raw.p256_pubkey, R.raw.trillian_log);
            try {
                client.getEntry("user","app",1000);
            } catch (KeyTransparencyException e) {
                tv.append("Expected exception was raised: " + e);
            }

        } catch (IOException e) {
            e.printStackTrace();
            tv.append("\nError reading one of the public keys: "+e.getMessage());
            throw new RuntimeException("Error reading one of the public keys: "+e.getMessage(),e );
        }
    }

}
