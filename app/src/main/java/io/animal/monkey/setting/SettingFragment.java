package io.animal.monkey.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import io.animal.monkey.R;
import io.animal.monkey.util.SharedPreferencesHelper;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey);

        SwitchPreferenceCompat switchTap = getPreferenceScreen().findPreference(SharedPreferencesHelper.KEY_BABY_MODE_TAP);
        switchTap.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getSharedPref().setTapOnBabyMode((boolean)newValue);
                return true;
            }
        });

        SwitchPreferenceCompat switchHome = getPreferenceScreen().findPreference(SharedPreferencesHelper.KEY_BABY_MODE_HOME);
        switchHome.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getSharedPref().setHomeOnBabyMode((boolean)newValue);
                return true;
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private SharedPreferencesHelper _sp;

    private SharedPreferencesHelper getSharedPref() {
        if (_sp == null) {
            _sp = new SharedPreferencesHelper(getContext());
        }

        return _sp;
    }
}
