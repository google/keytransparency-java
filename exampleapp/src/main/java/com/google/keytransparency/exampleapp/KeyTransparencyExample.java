package com.google.keytransparency.exampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.keytransparency.client.KeyTransparencyClient;
import com.google.keytransparency.client.KeyTransparencyException;
import com.google.keytransparency.client.LogReceiver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static android.R.attr.button;

public class KeyTransparencyExample extends AppCompatActivity {

    private static final String TAG_LOGS_FROM_GOBIND = "GoKtClient:";

    private static final String DEFAULT_AUTHORIZED_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"+
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEBUzgqmfMNYETU67U5kklSx/wfqcd\n"+
            "Zn+mxLDouFyti/hdshzOlZYfb51YG+zhgQQ7PpTzoj3Lz/EdfeZauwDKPA==\n"+
            "-----END PUBLIC KEY-----";

    private static final String DEFAULT_AUTHORIZED_PRIVATE_KEY = "-----BEGIN EC PRIVATE KEY-----\n" +
            "MHcCAQEEIKrzmO7QnfhTXOSP7hPk6j5fO2b36z97w35Fdr6d0qUkoAoGCCqGSM49\n" +
            "AwEHoUQDQgAEBUzgqmfMNYETU67U5kklSx/wfqcdZn+mxLDouFyti/hdshzOlZYf\n" +
            "b51YG+zhgQQ7PpTzoj3Lz/EdfeZauwDKPA==\n" +
            "-----END EC PRIVATE KEY-----";

    private static final int DEFAULT_RETRY_COUNT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_transparency_example);
        final TextView tv = (TextView) findViewById(R.id.textView);

        tv.setText("");
        tv.setMovementMethod(new ScrollingMovementMethod());

        KeyTransparencyClient.addVerboseLogsDestination(new LogReceiver() {
            @Override
            public long write(byte[] bytes) throws Exception {
                tv.append(TAG_LOGS_FROM_GOBIND + new String(bytes, "UTF-8"));
                return bytes.length;
            }
        });

        final EditText urlEditText = (EditText) findViewById(R.id.ktURL);
        urlEditText.setText("35.184.134.53:8080");

        final EditText emailEditText = (EditText) findViewById(R.id.email);
        emailEditText.setText("non_existing_user@gmail.com");
        final EditText appIdEditText = (EditText) findViewById(R.id.appId);
        appIdEditText.setText("app1");

        final EditText profileDataEditText = (EditText) findViewById(R.id.profileData);
        profileDataEditText.setText("myProfileData");

        final Button addServerButton = (Button) findViewById(R.id.addServerButton);
        addServerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String ktUrl = urlEditText.getText().toString();
                tv.append("\n\n --- AddServer test --- \n");
                tv.append("\nAdding " + ktUrl + "as a KeyTransparency server\n");
                try {
                    KeyTransparencyClient.addKtServerIfNotExists(ktUrl, true, null, null);
                } catch (KeyTransparencyException e) {
                    tv.append("\nError connecting to the server: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        final Button getEntryButton = (Button) findViewById(R.id.getEntryButton);
        getEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String appId = appIdEditText.getText().toString();
                String ktUrl = urlEditText.getText().toString();

                tv.append("\n\n --- GetEntry test --- \n");
                tv.append("\nTrying to get public key for (" + email + "," + appId + ") from server " + ktUrl + "\n");

                try {
                    byte[] entry = KeyTransparencyClient.getEntry(ktUrl, email, appId);
                    if (entry == null) {
                        tv.append("Received key is null: entry does not exists");
                    } else {
                        tv.append("Received key: " + bytesToHex(entry));
                    }
                } catch (KeyTransparencyException e) {
                    tv.append("\nError getting the key: " + e.getMessage());
                }
            }
        });

        final Button updateEntryButton = (Button) findViewById(R.id.updateEntryButton);
        updateEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String appId = appIdEditText.getText().toString();
                String ktUrl = urlEditText.getText().toString();

                String profileData = profileDataEditText.getText().toString();


                tv.append("\n\n --- UpdateEntry test --- \n");
                try {
                    tv.append("\nTrying to update public key for (" + email + "," + appId + "," + bytesToHex(profileData.getBytes("UTF-8")) + ") from server " + ktUrl + "\n");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    KeyTransparencyClient.updateEntry(ktUrl, email, appId, profileData.getBytes("UTF-8"), DEFAULT_AUTHORIZED_PRIVATE_KEY, DEFAULT_AUTHORIZED_PUBLIC_KEY, DEFAULT_RETRY_COUNT);
                    tv.append("Update succeeded");
                } catch (KeyTransparencyException e) {
                    tv.append("\nError updating the key: " + e.getMessage());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
