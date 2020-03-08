package io.animal.monkey.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import io.animal.monkey.AdMobActivity;
import io.animal.monkey.MainActivity;
import io.animal.monkey.bus.events.KidModeEvent;
import io.animal.monkey.bus.events.TapServiceEvent;
import io.animal.monkey.touch.TouchEventView;
import io.animal.monkey.util.PermissionHelper;
import io.animal.monkey.util.SharedPreferencesHelper;

public class EventAccessibilityService extends AccessibilityService {

    private final static String TAG = EventAccessibilityService.class.getSimpleName();

    private final static int REQ_CODE_OVERLAY_PERMISSION = 101;

    private TouchEventView touchEventView;

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.d(TAG, "onKeyEvent(" + event.toString() + ")");

        String model = Build.MODEL;
        final int keyCode = event.getKeyCode();
        final int action = event.getAction();

        // !!! 중요정책
        // HOME 버튼은 Baby 시청모드를 on/off 하는 entry point.
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            onPressedHomeKey(action);
        }

        Log.d(TAG, "onKeyEvent:" + getSharedPref().enableTapOnBabyMode() + " : " + getSharedPref().enableHomeOnBabyMode());

        if (getSharedPref().isBabyMode()
                && getSharedPref().enableHomeOnBabyMode()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_HOME:
                case KeyEvent.KEYCODE_APP_SWITCH:
                case KeyEvent.KEYCODE_BACK:
                    // show Lock icon.
                    if (action == KeyEvent.ACTION_UP) {
                        EventBus.getDefault().post(new KidModeEvent());
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

        PermissionHelper permissionHelper = new PermissionHelper(getApplicationContext());
        if (!permissionHelper.hasSystemAlertWindowsPermission()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        touchEventView = new TouchEventView(getBaseContext());
        touchEventView.updateParamsForLocation(getWindowManager(), true);

        // check floating permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
//            return;
//        }
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

    /// ---------------------------------------------------------------------------- android service

    private WindowManager _windowManager;

    private WindowManager getWindowManager() {
        if (_windowManager == null) {
            _windowManager = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        }

        return _windowManager;
    }

    private SharedPreferencesHelper _sp;

    private SharedPreferencesHelper getSharedPref() {
        if (_sp == null) {
            _sp = new SharedPreferencesHelper(getApplicationContext());
        }

        return _sp;
    }

    /// ------------------------------------------------------------------------ android service end

    /// -------------------------------------------------------------------------------------- timer

    private final static long NEED_SECONDS_FOR_BABY_MODE = 3000;

    private final static long TIMER_PERIOD = 1000;

    private boolean isTimeout;

    private CountDownTimer timer = new CountDownTimer(NEED_SECONDS_FOR_BABY_MODE, TIMER_PERIOD) {
        @Override
        public void onTick(long millisUntilFinished) {
            isTimeout = false;
        }

        @Override
        public void onFinish() {
            isTimeout = true;

            if (getSharedPref().isBabyMode()) {
                Toast.makeText(getApplicationContext(), "아기시청모드가 해제 되었습니다.", Toast.LENGTH_LONG).show();

                // show MainActivity
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MainActivity.EXTRA_KEY, true);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "아기시청모드가 되었습니다.", Toast.LENGTH_LONG).show();

                EventBus.getDefault().post(new TapServiceEvent(true));
                getSharedPref().setBabyMode(true);
            }
        }
    };

    private void onPressedHomeKey(int action) throws UnknownError {
        if (action == KeyEvent.ACTION_DOWN) {
            timer.cancel();
            timer.start();
        } else if (action == KeyEvent.ACTION_UP) {
            timer.cancel();
        } else {
            Log.e(TAG, "onPressedHomeKey: unknown action " + action);
            throw new UnknownError();
        }
    }

    /// ---------------------------------------------------------------------------------- end timer
}
