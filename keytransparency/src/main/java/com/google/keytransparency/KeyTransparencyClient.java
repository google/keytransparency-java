package com.google.keytransparency;

import com.google.keytransparency.gobind.gobindclient.Gobindclient;

public class KeyTransparencyClient {

    // addVerboseLogsDestination instructs the client to also send the verbose log statements
    // to the provided destination. Useful for demonstrating what the library is doing.
    public void addVerboseLogsDestination(LogReceiver receiver) {
        Gobindclient.addVerboseLogsDestination(receiver);
    }

    // setTimeout sets the timeout (in milliseconds) for all network requests made by the
    // KeyTransparencyClient.
    public void setTimeout(int ms){
        Gobindclient.setTimeout(ms);
    }

    // addKtServer configures a connection to a new KeyTransparency Server. If insecureTLS is true,
    // the TLS certificate verification is skipped (useful for debug). If false, a self signed certificate
    // can be passed through ktTlsCertPem. Finally, domainInfoHash is the hash of all public key material
    // that will be used to verify the proofs provided by this kt server and should be hardcoded.
    public void addKtServer(String ktUrl, boolean insecureTLS, byte[] ktTlsCertPem, byte[] domainInfoHash) throws KeyTransparencyException {
        try {
            Gobindclient.addKtServer(ktUrl, insecureTLS, ktTlsCertPem, domainInfoHash);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

    // getEntry retrieves an entry from a KeyTransparency server. Before calling this method, a connection
    // needs to be established through addKtServer.
    public byte[] getEntry(String ktUrl, String userName, String appName) throws KeyTransparencyException {
        try {
            return Gobindclient.getEntry(ktUrl, userName, appName);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }
}
