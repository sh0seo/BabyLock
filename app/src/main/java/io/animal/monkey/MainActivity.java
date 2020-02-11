package io.animal.monkey;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.drm.DrmStore;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;

import io.animal.monkey.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                // app icon
                actionBar.setIcon(R.drawable.ic_child_care_black_24dp);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);

                // Rubik font
                SpannableString s = new SpannableString(" BABYLOCK");
                s.setSpan(new TypefaceSpan("rubik_black.ttf"), 0, s.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                actionBar.setTitle(s);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
