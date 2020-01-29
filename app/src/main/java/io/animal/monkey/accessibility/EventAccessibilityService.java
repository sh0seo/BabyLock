package io.animal.monkey.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

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
                case KeyEvent.KEYCODE_APP_SWITCH:
                case KeyEvent.KEYCODE_BACK:
                case KeyEvent.KEYCODE_VOLUME_UP:
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    // TODO show Lock icon.
                    Toast.makeText(getApplicationContext(), "show lock icon", Toast.LENGTH_SHORT).show();
                    return true;
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

//        PermissionHelper permissionHelper = new PermissionHelper(getApplication());

//        boolean isHas = permissionHelper.hasSystemAlertWindowsPermission();
//        if (isHas) {
//            windowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
//            if (ScreenHepler.isPortrait(getResources())) {
//                isPortrait = true;
//            } else {
//                isPortrait = false;
//            }
//
//            initTouchView();
//        } else {
//            Toast.makeText(this, "권한이 없음.", Toast.LENGTH_SHORT).show();
//        }

//        tileService = new SwitchTileService();
        try {
            sp = new SharedPreferencesHelper(getApplication());
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

        sp.setTileState(Tile.STATE_INACTIVE);
    }

}
