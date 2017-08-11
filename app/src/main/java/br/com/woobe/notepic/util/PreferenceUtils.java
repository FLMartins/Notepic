package br.com.woobe.notepic.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import br.com.woobe.notepic.R;

/**
 * Created by willian alfeu on 17/01/2017.
 */

public class PreferenceUtils {

    public static void putString(Activity context, String key, String value) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putInteger(Activity context, String key, Integer value) {
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(Activity context, String key){
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static Integer getInteger(Activity context, String key){
        SharedPreferences sharedPref = context.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

}
