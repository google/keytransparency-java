package com.google.keytransparency;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.keytransparency.gobind.gobindClient.BClientParams;
import com.google.keytransparency.gobind.gobindClient.GobindClient;
import com.google.keytransparency.gobind.gobindClient.BWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class KTClient {
    private static final String TAG_LOGS_FROM_GOBIND = "KTGo:";

    private final BClientParams clientParams;

    // TODO remove me
    TextView tv;
    public void setTextViewForLogs(TextView tv) {
        this.tv = tv;
    }


    public KTClient(String ktUrl, long mapID, byte[] ktServerTLSCert, byte[] vrfPubKey, byte[] ktSmrPubKey, byte[] trLogKey) {
        clientParams = new BClientParams(ktUrl, mapID, ktServerTLSCert, vrfPubKey, ktSmrPubKey, trLogKey);
        GobindClient.bSetCustomLogger(new WriterForGoLogs());
    }

    public KTClient(String ktUrl, long mapID, Context context, int ktServerTLSCertResID, int vrfPubKeyResID, int ktSmrPubKeyResID, int trLogKeyResID) throws IOException {
        this(ktUrl, mapID, resourceToByteArray(context, ktServerTLSCertResID),
                resourceToByteArray(context, vrfPubKeyResID),
                resourceToByteArray(context, ktSmrPubKeyResID),
                resourceToByteArray(context, trLogKeyResID));
    }

    public byte[] getEntry(String userName, String appName, int timeoutInMilliseconds) throws KeyTransparencyException {
        try {
            // TODO(amarcedone): do we want to store the latest smr so that it can be used for consistency of new requests?
            return GobindClient.bGetEntry(timeoutInMilliseconds, clientParams, userName, appName);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

//    byte[] verifyGetEntryResponse(){
//        return null;
//    }

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

    private static byte[] resourceToByteArray(Context context, int resourceId) throws IOException {
        InputStream is = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        // TODO(amarcedone): Buffer size was chosen arbitrarily. Figure out what is appropriate.
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

}
