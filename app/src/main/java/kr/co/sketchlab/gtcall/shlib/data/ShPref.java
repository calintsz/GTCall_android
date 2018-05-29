package kr.co.sketchlab.gtcall.shlib.data;

import android.content.Context;
import android.content.SharedPreferences;

public class ShPref {
    // GET Ref
    public static int getPreference(Context context, String prefName, String name, int defaultValue) {
        if(context == null)
            return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return prefs.getInt(name, defaultValue);
    }
    public static long getPreference(Context context, String prefName, String name, long defaultValue) {
        if(context == null)
            return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return prefs.getLong(name, defaultValue);
    }
    public static String getPreference(Context context, String prefName, String name, String defaultValue) {
        if(context == null)
            return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return prefs.getString(name, defaultValue);
    }
    public static boolean getPreference(Context context, String prefName, String name, boolean defaultValue) {
        if(context == null)
            return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return prefs.getBoolean(name, defaultValue);
    }
    public static float getPreference(Context context, String prefName, String name, float defaultValue) {
        if(context == null)
            return defaultValue;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return prefs.getFloat(name, defaultValue);
    }
    public static void setPreference(Context context, String prefName, String name, int data) {
        if(context == null)
            return;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(name, data);
        editor.commit();
    }
    public static void setPreference(Context context, String prefName, String name, long data) {
        if(context == null)
            return;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(name, data);
        editor.commit();
    }
    public static void setPreference(Context context, String prefName, String name, String data) {
        if(context == null)
            return;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, data);
        editor.commit();
    }
    public static void setPreference(Context context, String prefName, String name, boolean data) {
        if(context == null)
            return;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, data);
        editor.commit();
    }
    public static void setPreference(Context context, String prefName, String name, float data) {
        if(context == null)
            return;
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(name, data);
        editor.commit();
    }

    public static void removePreference(Context context, String prefName , String key) {
        SharedPreferences prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }
}
