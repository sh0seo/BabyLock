package io.animal.monkey.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class SharedPref extends ContextWrapper {

    private final static String PREF_NAME = "pref";

    private final static String KEY_TILE_STATE = "tile_state";

    private SharedPreferences pref;

    public SharedPref(Context base) {
        super(base);
    }

    public void setTileState(int state) {
        getPreferences().edit().putInt(KEY_TILE_STATE, state).apply();
    }

    public int getTileState() {
        return getPreferences().getInt(KEY_TILE_STATE, -1);
    }

    private SharedPreferences getPreferences() {
        if (pref == null) {
            pref = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        }
        return pref;
    }

}
