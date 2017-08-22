package com.google.keytransparency;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.keytransparency.gobind.gobindClient.GobindClient;
import com.google.keytransparency.gobind.gobindClient.BWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class KTClient {
    private static final String TAG_LOGS_FROM_GOBIND = "KTGo:";

    private static KTClient client;

    // TODO remove me
    TextView tv;
    public void setTextViewForLogs(TextView tv) {
        this.tv = tv;
    }


    private KTClient(int timeoutInMs){
        try {
            GobindClient.bInit(timeoutInMs);
            GobindClient.bSetCustomLogger(new WriterForGoLogs());
        }catch (Exception e) {
            // This should never happen actually. We are enforcing init can be called only once,
            // and so far the only error comes from calling init twice.
            throw new RuntimeException(e);
        }
    }

    public static KTClient getClient(int timeoutInMs){
        if (client == null) {
            client = new KTClient(timeoutInMs);
        }
        return client;
    }

    public void addKtServer(String ktUrl, boolean insecureTLS, byte[] ktTlsCertPem, byte[] domainInfoHash) throws KeyTransparencyException {
        try {
            GobindClient.bAddKtServer(ktUrl, insecureTLS, ktTlsCertPem, domainInfoHash);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

    public byte[] getEntry(String ktUrl, String userName, String appName) throws KeyTransparencyException {
        try {
            // TODO(amarcedone): do we want to store the latest smr so that it can be used for consistency of new requests?
            return GobindClient.bGetEntry(ktUrl, userName, appName);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

    private class WriterForGoLogs implements BWriter {
        @Override
        public long write(byte[] bytes) throws Exception {
            // TODO(amarcedone): confirm utf-8 is the correct encoding here, as well as loglevel (i).
            Log.i(TAG_LOGS_FROM_GOBIND, new String(bytes, "UTF-8"));

            // TODO REMOVE ME.
            if(tv != null){
                tv.append(TAG_LOGS_FROM_GOBIND + new String(bytes, "UTF-8"));
            }

            return bytes.length;
        }
    }

}
