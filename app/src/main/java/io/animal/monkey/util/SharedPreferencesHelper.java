package io.animal.monkey.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class SharedPreferencesHelper extends ContextWrapper {

//    public final static String KEY_TILE_STATE = "tile_state";
    public final static String KEY_BABY_MODE = "baby_mode";

    private final static String PREF_NAME = "pref";

    private SharedPreferences pref;

    /**
     * Construction.
     *
     * @param context
     */
    public SharedPreferencesHelper(Context context) {
        super(context);
    }

//    public void setTileState(int state) {
//        getPreferences().edit().putInt(KEY_TILE_STATE, state).apply();
//    }
//
//    public int getTileState() {
//        return getPreferences().getInt(KEY_TILE_STATE, -1);
//    }

    public void setBabyMode(boolean enable) {
        getPreferences().edit().putBoolean(KEY_BABY_MODE, enable).apply();
    }

    public boolean isBabyMode() {
        return getPreferences().getBoolean(KEY_BABY_MODE, false);
    }

    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    private SharedPreferences getPreferences() {
        if (pref == null) {
            pref = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        }
        return pref;
    }

}
