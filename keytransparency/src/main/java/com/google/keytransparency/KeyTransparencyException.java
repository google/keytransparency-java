package com.google.keytransparency;

/**
 * A KeyTransparencyException is thrown whenever the go native code throws an Exception.
 */
public class KeyTransparencyException extends Throwable {
    public KeyTransparencyException(Exception e) {
        super(e);
    }
}
