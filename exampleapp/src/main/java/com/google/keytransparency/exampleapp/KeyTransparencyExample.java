package com.google.keytransparency.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.keytransparency.client.KeyTransparencyClient;
import com.google.keytransparency.client.KeyTransparencyException;
import com.google.keytransparency.client.LogReceiver;

import java.io.IOException;

public class KeyTransparencyExample extends AppCompatActivity {

    private static final String TAG_LOGS_FROM_GOBIND = "GoKtClient:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_transparency_example);
        final TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText("");
        tv.setMovementMethod(new ScrollingMovementMethod());

        try {
            tv.append("\n\n --- GetEntry test --- \n");

            KeyTransparencyClient.addVerboseLogsDestination(new LogReceiver() {
                @Override
                public long write(byte[] bytes) throws Exception {
                    tv.append(TAG_LOGS_FROM_GOBIND + new String(bytes, "UTF-8"));
                    return bytes.length;
                }
            });

            String ktUrl = "35.184.134.53:8080";
            KeyTransparencyClient.addKtServer(ktUrl, true, null, null);

            try {
                String username = "gary.belvin@gmail.com";

                tv.append("\nTrying to get public key for " + username + " from server " + ktUrl + "\n");
                byte[] entry = KeyTransparencyClient.getEntry(ktUrl, username, "app1");
                if (entry == null) {
                    tv.append("Received key is null: entry does not exists");
                } else {
                    tv.append("Received key: " + new String(entry, "UTF-8"));
                }

                username = "NOT_A_USER@gmail.com";
                tv.append("\n\nTrying to get public key for " + username + " from server " + ktUrl + "\n");
                entry = KeyTransparencyClient.getEntry(ktUrl, username, "app1");
                if (entry == null) {
                    tv.append("Received key is null: entry does not exists");
                } else {
                    tv.append("Received key: " + new String(entry, "UTF-8"));
                }


            } catch (KeyTransparencyException e) {
                tv.append("Exception was raised: " + e);
            }

        } catch (IOException e) {
            e.printStackTrace();
            tv.append("\nError reading one of the public keys: " + e.getMessage());
            throw new RuntimeException("Error reading one of the public keys: " + e.getMessage(), e);
        } catch (KeyTransparencyException e) {
            e.printStackTrace();
            tv.append("\nError creating the client: " + e.getMessage());
            throw new RuntimeException("Error creating the client: " + e.getMessage(), e);
        }
    }

}
