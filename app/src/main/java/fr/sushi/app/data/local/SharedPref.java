package fr.sushi.app.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private final static String PREFERENCE_NAME = "sushi_app";
    private static SharedPreferences preferences;
    private static SharedPref sharedPref;

    private SharedPref() {
    }

    public static SharedPref on(Context context) {
        preferences = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        if (sharedPref == null) {
            sharedPref = new SharedPref();
        }
        return sharedPref;
    }

    public static boolean write(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);

        return editor.commit();
    }

    public static boolean write(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);

        return editor.commit();
    }

    public static boolean write(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean write(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static String read(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static long readLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public static int readInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public static boolean readBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }



    public static boolean contains(String key) {
        return preferences.contains(key);
    }

    /**
     * Remove all saved shared Preference data of app.
     */
    public static void removeAllPreferenceData() {
        preferences.edit().clear().apply();
    }

    public static void removeSpecificItem(String key) {
        preferences.edit().remove(key).apply();
    }

}
