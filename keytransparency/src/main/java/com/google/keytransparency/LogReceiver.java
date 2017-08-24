package com.google.keytransparency;

import com.google.keytransparency.gobind.gobindclient.LogWriter;

// LogReceiver is a rebranding of the LogWriter interface on the go side.
// Used to hide the gobind classes from users of this library.
public interface LogReceiver extends LogWriter {}
