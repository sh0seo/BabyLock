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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.monkey.R;
import io.animal.monkey.bus.events.KidModeEvent;
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

        final int keyCode = event.getKeyCode();
        final int action = event.getAction();

        // !!! 중요정책
        // HOME 버튼은 Baby 시청모드를 on/off 하는 entry point.
        if (getSharedPref().isBabyMode()) {
            // todo baby mode
        } else {
            if (keyCode == KeyEvent.KEYCODE_HOME) {
                if (action == KeyEvent.ACTION_DOWN) {
                    timer.cancel();
                    timer.start();
                } else if (action == KeyEvent.ACTION_UP) {
                    if (ti) {

                    }
                }
            }
        }

//        if (getSharedPref().getTileState() == Tile.STATE_ACTIVE) {
//            int code = event.getKeyCode();
//            switch (code) {
//                case KeyEvent.KEYCODE_HOME:
//                case KeyEvent.KEYCODE_APP_SWITCH:
//                case KeyEvent.KEYCODE_BACK:
//                    // TODO volume up, down key is ready.
////                case KeyEvent.KEYCODE_VOLUME_UP:
////                case KeyEvent.KEYCODE_VOLUME_DOWN:
//                    // TODO show Lock icon.
//                    if (event.getAction() == KeyEvent.ACTION_UP) {
////                        Toast.makeText(getApplicationContext(), "show lock icon", Toast.LENGTH_SHORT).show();
//                        EventBus.getDefault().post(new KidModeEvent());
//                    }
//                     // TODO Home Pressed 3 sec.
//                    return true;
//            }
//        }

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

        getSharedPref().setTileState(Tile.STATE_INACTIVE);

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

    /// -------------------------------------------------------------------------------------- timer

    private final static long NEED_SECONDS_FOR_BABY_MODE = 3000;

    private final static long TIMER_PERIOD = 1000;

    private CountDownTimer timer = new CountDownTimer(NEED_SECONDS_FOR_BABY_MODE, TIMER_PERIOD) {

        private boolean isTimeout = false;

        @Override
        public void onTick(long millisUntilFinished) {
            isTimeout = false;
        }

        @Override
        public void onFinish() {
            isTimeout = true;
        }

        public boolean isTimeout() {
            return isTimeout;
        }
    };

    /// ---------------------------------------------------------------------------------- end timer
}
