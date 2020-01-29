package io.animal.monkey.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import io.animal.monkey.util.SharedPreferencesHelper;

public class EventAccessibilityService extends AccessibilityService {

    private final static String TAG = EventAccessibilityService.class.getSimpleName();

//    private IBinder tileService;

    private SharedPreferencesHelper sp;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent(" + event.toString() + ")");
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt()");
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "onKeyEvent(" + event.toString() + ")");

        if (sp.getTileState() == Tile.STATE_ACTIVE) {
            int code = event.getKeyCode();
            switch (code) {
                case KeyEvent.KEYCODE_HOME:
                case KeyEvent.KEYCODE_MENU:
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    // TODO show Lock icon.
                    return true;
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        boolean canDrawOverlays = checkSystemAlertWindowPermission(this);
        if (canDrawOverlays) {
//            windowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
//            if (ScreenHepler.isPortrait(getResources())) {
//                isPortrait = true;
//            } else {
//                isPortrait = false;
//            }
//
//            initTouchView();
        } else {
//            Toast.makeText(this, getString(R.string.Toast_allow_system_alert_first), Toast.LENGTH_LONG).show();
        }

//        tileService = new SwitchTileService();
        try {
            sp = new SharedPreferencesHelper(getApplication());
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
            return;
        }

        if (sp.getTileState() == Tile.STATE_UNAVAILABLE) {
            // TODO request permission.
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);

    }

    private boolean checkSystemAlertWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (!Settings.canDrawOverlays(context)) {
            return false;
        }
        return true;
    }
}
