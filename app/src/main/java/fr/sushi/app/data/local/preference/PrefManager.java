/*
package fr.sushi.app.data.local.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.sushi.app.SushiApp;


public class PrefManager {

    private SharedPreferences publicPref;
    private SharedPreferences privatePref;

    private int getMode() {
        return Context.MODE_PRIVATE;
    }

    private String getPrefName() {
        return SushiApp.getInstance().getPackageName();
    }

    private static volatile PrefManager instance;

    private PrefManager() {
        publicPref = PreferenceManager.getDefaultSharedPreferences(SushiApp.getInstance());
        privatePref = SushiApp.getInstance().getSharedPreferences(getPrefName(), getMode());
    }

    public static PrefManager getPreference() {
        if (instance == null) {
            synchronized (PrefManager.class) {
                if (instance == null) {
                    instance = new PrefManager();
                }
            }
        }
        return instance;
    }

    private void setPublicValue(String key, String value) {
        SharedPreferences.Editor editor = publicPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, String value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, int value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, long value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void setPrivateValue(String key, boolean value) {
        SharedPreferences.Editor editor = privatePref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private String getPublicString(String key, String defaultValue) {
        return publicPref.getString(key, defaultValue);
    }

    private boolean getPublicBoolean(String key, boolean defaultValue) {
        return publicPref.getBoolean(key, defaultValue);
    }

    public String getPrivateString(String key) {
        return privatePref.getString(key, null);
    }

    public String getPrivateString(String key, String defValue) {
        return privatePref.getString(key, defValue);
    }

    public int getPrivateInt(String key) {
        return privatePref.getInt(key, -1);
    }

    public long getPrivateLong(String key) {
        return privatePref.getLong(key, -1);
    }

    public boolean getPrivateBoolean(String key, boolean defaultValue) {
        return privatePref.getBoolean(key, defaultValue);
    }

    @SuppressLint("ApplySharedPref")
    public void clearAllPrefData() {
        publicPref.edit().clear().commit();
        privatePref.edit().clear().commit();
    }
}
*/
