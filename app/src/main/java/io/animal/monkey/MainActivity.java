package io.animal.monkey;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.monkey.bus.events.AlertBoxStatusEvent;
import io.animal.monkey.setting.SettingFragment;
import io.animal.monkey.ui.main.MainFragment;
import io.animal.monkey.util.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settingFragment = new SettingFragment();

//        ImageView logo = findViewById(R.id.logo);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            logo.setBackground(new ShapeDrawable(new OvalShape()));
//        } else {
//            logo.setBackgroundDrawable(new ShapeDrawable(new OvalShape()));
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            logo.setClipToOutline(true);
//        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_main, settingFragment)
                    .commitNow();
        }

        try {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                // app icon
//                actionBar.setIcon(R.drawable.ic_child_care_black_24dp);
//                actionBar.setDisplayUseLogoEnabled(true);
//                actionBar.setDisplayShowHomeEnabled(true);

                // Rubik font
//                SpannableString s = new SpannableString(" BABYLOCK");
//                s.setSpan(new TypefaceSpan("rubik_black.ttf"), 0, s.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                actionBar.setTitle(s);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PermissionHelper permissionHelper = new PermissionHelper(this);
        if (!permissionHelper.hasSystemAlertWindowsPermission()
                || !permissionHelper.isAccessibilitySettingsOn()) {
           showPermissionAlertBox();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AlertBox(AlertBoxStatusEvent e) {
        Log.d(TAG, "AlertBox");
        if (!settingFragment.isHidden()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(settingFragment)
                    .commitNow();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(settingFragment)
                    .commitNow();
        }
    }

    private void showPermissionAlertBox() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("권한확인");
        alertDialog.setMessage("앱 구동에 필요한 권한 확인이 필요합니다.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(this, SettingFragment.class);
//                startActivity(intent);
                Snackbar.make(getRootView(), "Show Full Dialog Box", Snackbar.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private View getRootView() {
        return getWindow().getDecorView().getRootView();
    }
}
