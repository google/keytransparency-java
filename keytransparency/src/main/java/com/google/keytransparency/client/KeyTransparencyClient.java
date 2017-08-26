package com.google.keytransparency.client;

import com.google.keytransparency.gobind.gobindclient.Gobindclient;

public final class KeyTransparencyClient {

    // Prevents instantiating the class.
    private KeyTransparencyClient(){}

    // addVerboseLogsDestination instructs the client to also send the verbose log statements
    // to the provided destination. Useful for demonstrating what the library is doing.
    public static void addVerboseLogsDestination(LogReceiver receiver) {
        Gobindclient.addVerboseLogsDestination(receiver);
    }

    // setTimeout sets the timeout (in milliseconds) for all network requests made by the
    // KeyTransparencyClient.
    public static void setTimeout(int ms){
        Gobindclient.setTimeout(ms);
    }

    // addKtServer configures a connection to a new KeyTransparency Server. If insecureTLS is true,
    // the TLS certificate verification is skipped (useful for debug). If false, a self signed certificate
    // can be passed through ktTlsCertPem. Finally, domainInfoHash is the hash of all public key material
    // that will be used to verify the proofs provided by this kt server and should be hardcoded.
    // If a conncection to the ktUrl was already created, this method throws an exception.
    public static void addKtServer(String ktUrl, boolean insecureTLS, byte[] ktTlsCertPem, byte[] domainInfoHash) throws KeyTransparencyException {
        try {
            Gobindclient.addKtServer(ktUrl, insecureTLS, ktTlsCertPem, domainInfoHash);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

    // addKtServerIfNotExists is similar to addKtServer, but the command is ignored without any exception
    // if a connection to ktUrl already exists.
    public static void addKtServerIfNotExists(String ktUrl, boolean insecureTLS, byte[] ktTlsCertPem, byte[] domainInfoHash) throws KeyTransparencyException {
        try {
            Gobindclient.addKtServerIfNotExists(ktUrl, insecureTLS, ktTlsCertPem, domainInfoHash);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }


    // getEntry retrieves an entry from a KeyTransparency server. Before calling this method, a connection
    // needs to be established through addKtServer.
    public static byte[] getEntry(String ktUrl, String userName, String appName) throws KeyTransparencyException {
        try {
            return Gobindclient.getEntry(ktUrl, userName, appName);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

    public static void updateEntry(String ktUrl, String userName, String appName, byte[] profileData, String authorizedPrivateKeyPem, String authorizedPublicKeyPem, int retryCount) throws KeyTransparencyException {
        try {
            Gobindclient.updateEntryWithFakeAuth(ktUrl, userName, appName, profileData, authorizedPrivateKeyPem, authorizedPublicKeyPem, retryCount);
        } catch (Exception e) {
            throw new KeyTransparencyException(e);
        }
    }

}
