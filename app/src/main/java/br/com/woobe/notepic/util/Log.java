package br.com.woobe.notepic.util;

/**
 * Created by willian alfeu on 23/11/2016.
 */

public class Log {
    private static final String TAG = "E_NOTEPIC";

    public static void e(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void d(String msg) {
        android.util.Log.d(TAG, msg);
    }

    public static void i(String msg) {
        android.util.Log.i(TAG, msg);
    }

    public static void v(String msg) {
        android.util.Log.v(TAG, msg);
    }

    public static void w(String msg) {
        android.util.Log.w(TAG, msg);
    }
}
