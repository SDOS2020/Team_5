package org.emberon.winscan.util;

import android.util.Log;

public class DebugUtil {

    private final static String DEFAULT_DEBUG_TAG = "bruh";

    public static void log(Object... objects) {
        StringBuilder logMessage = new StringBuilder();
        for (Object o : objects) {
            if (o != null) {
                logMessage.append(o.toString()).append(" | ");
            } else {
                logMessage.append("null | ");
            }
        }
        Log.d(DEFAULT_DEBUG_TAG, logMessage.toString());
    }
}
