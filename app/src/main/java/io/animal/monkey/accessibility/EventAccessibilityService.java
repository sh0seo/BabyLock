package io.animal.monkey.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import io.animal.monkey.bus.events.KidModeEvent;
import io.animal.monkey.touch.TouchEventView;
import io.animal.monkey.util.SharedPreferencesHelper;

public class EventAccessibilityService extends AccessibilityService {

    private final static String TAG = EventAccessibilityService.class.getSimpleName();

    private TouchEventView touchEventView;

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "onKeyEvent(" + event.toString() + ")");

        if (getSharedPref().getTileState() == Tile.STATE_ACTIVE) {
            int code = event.getKeyCode();
            switch (code) {
                case KeyEvent.KEYCODE_HOME:
                case KeyEvent.KEYCODE_APP_SWITCH:
                case KeyEvent.KEYCODE_BACK:
                    // TODO volume up, down key is ready.
//                case KeyEvent.KEYCODE_VOLUME_UP:
//                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    // TODO show Lock icon.
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        Toast.makeText(getApplicationContext(), "show lock icon", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(KidModeEvent.class);
                    }
                    return true;
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        // 사용자가 서비스에서 Accessibility를 설정하면 동작.
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
        getSharedPref().setTileState(Tile.STATE_INACTIVE);

        touchEventView = new TouchEventView(getApplication());
        touchEventView.updateParamsForLocation(getWindowManager(), true);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent(" + event.toString() + ")");
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (touchEventView != null) {
            touchEventView.onDestroy();
            getWindowManager().removeView(touchEventView.getTouchView());
        }
    }

    private WindowManager windowManager;

    private WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        }

        return windowManager;
    }

    private SharedPreferencesHelper sp;

    private SharedPreferencesHelper getSharedPref() {
        if (sp == null) {
            sp = new SharedPreferencesHelper(getApplicationContext());
        }

        return sp;
    }

}
