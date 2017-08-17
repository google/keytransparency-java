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
            tv.append("\n\n --- GetEntry test --- \n");

            String ktUrl = "35.184.134.53:8080";
            KTClient client = new KTClient(ktUrl, 1, getApplicationContext(), R.raw.server, R.raw.vrf_pubkey, R.raw.p256_pubkey, R.raw.trillian_log);
            client.setTextViewForLogs(tv);
            try {
                String username = "gary.belvin@gmail.com";

                tv.append("\nTrying to get public key for " + username + " from server " + ktUrl +"\n");
                byte[] entry = client.getEntry(username,"app1",1000);
                if (entry==null){
                    tv.append("Received key is null: entry does not exists");
                } else {
                    tv.append("Received key: " + new String(entry, "UTF-8"));
                }

                username = "NOT_A_USER@gmail.com";
                tv.append("\n\nTrying to get public key for " + username + " from server " + ktUrl +"\n");
                entry = client.getEntry(username,"app1",1000);
                if (entry==null){
                    tv.append("Received key is null: entry does not exists");
                } else {
                    tv.append("Received key: " + new String(entry, "UTF-8"));
                }


            } catch (KeyTransparencyException e) {
                tv.append("Exception was raised: " + e);
            }

        } catch (IOException e) {
            e.printStackTrace();
            tv.append("\nError reading one of the public keys: "+e.getMessage());
            throw new RuntimeException("Error reading one of the public keys: "+e.getMessage(),e );
        }
    }

}
