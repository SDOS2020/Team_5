package org.emberon.winscan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.emberon.winscan.WinCashApp;

public class Utils {

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static void showToast(String message) {
        try {
            Toast.makeText(WinCashApp.getContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {

        }
    }
}
