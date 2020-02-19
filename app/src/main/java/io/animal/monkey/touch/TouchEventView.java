package io.animal.monkey.touch;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.monkey.R;
import io.animal.monkey.bus.events.KidModeEvent;
import io.animal.monkey.bus.events.TileServiceEvent;

public class TouchEventView extends ContextWrapper {

    private final static String TAG = "TouchEventView";

    private int miniTouchGestureHeight;

    private View touchView;

    private ImageView kidModeImage;
    private ImageView stopButton;

    public TouchEventView(Context c) {
        super(c);
        init();

        // register event bus
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        touchView = inflate.inflate(R.layout.screen_lock, null);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "setOnTouchListener:");
                return false;
            }
        });

        stopButton = touchView.findViewById(R.id.stop_button);
        stopButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "setOnTouchListener:");
                return false;
            }
        });
        kidModeImage = touchView.findViewById(R.id.kid_mode);
//        touchView = new View(getApplicationContext());
//        touchView.setBackgroundColor(Color.parseColor("#44FF1122"));
    }

    public void updateMiniTouchGestureHeight(@Nullable Integer heightPx) {
        miniTouchGestureHeight = heightPx;
    }

    public void updateParamsForLocation(WindowManager windowManager, Boolean isPortrait) {
        WindowManager w = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        w.addView(touchView, createTouchViewParams());

        if (isPortrait) {
            miniTouchGestureHeight = SPFManager.getTouchviewPortraitHeight(getApplicationContext());
            //transparent color
//            windowManager.addView(touchView,
//                    createTouchViewParams(
//                            SPFManager.getTouchviewPortraitHeight(getApplicationContext()),
//                            SPFManager.getTouchviewPortraitWidth(getApplicationContext()),
//                            SPFManager.getTouchviewPortraitPosition(getApplicationContext())));
        } else {
            miniTouchGestureHeight = SPFManager.getTouchviewLandscapeHeight(getApplicationContext());
            //transparent color
//            windowManager.addView(touchView,
//                    createTouchViewParams(
//                            SPFManager.getTouchviewLandscapeHeight(getApplicationContext()),
//                            SPFManager.getTouchviewLandscapeWidth(getApplicationContext()),
//                            SPFManager.getTouchviewLandscapePosition(getApplicationContext())));
        }
    }

    private WindowManager.LayoutParams createTouchViewParams() {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                            ,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                            | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    ,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        return params;
    }

    /**
     * Disable User' Touch
     *
     * @return
     */
    private WindowManager.LayoutParams updateTouchViewParams() {
        WindowManager.LayoutParams params;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                     WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

                    ,
                    PixelFormat.TRANSLUCENT);
        }
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        return params;
    }
//    private WindowManager.LayoutParams createTouchViewParams(int heightPx, int weightPx, int position) {
//        WindowManager.LayoutParams params;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            params = new WindowManager.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.TYPE_PHONE,
//                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                    PixelFormat.TRANSLUCENT);
//        } else {
//            params = new WindowManager.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//                    WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
//                    PixelFormat.TRANSLUCENT);
//        }
//        params.gravity = Gravity.TOP | Gravity.LEFT;
//        return params;
//    }

    public View getTouchView() {
        return touchView;
    }

    /// ---------------------------------------------------------------------------- android service

    private void sendBroadcastCloseSystemDialog() {
        Intent i = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        getApplicationContext().sendBroadcast(i);
    }

    private WindowManager getWindowManager() {
        return (WindowManager) getSystemService(Service.WINDOW_SERVICE);
    }

    /// ------------------------------------------------------------------------ android service end

    /// -------------------------------------------------------------------------- EventBus Listener

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showKidMode(KidModeEvent e) {
        Log.d(TAG, "showKizMode()");
        kidModeImage.animate().alpha(1.0f).setDuration(300).start();
        kidModeImage.setAlpha(1.0f);
        kidModeImage.animate().alpha(0.0f).setStartDelay(300).setDuration(300).start();
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTileServiceEvent(TileServiceEvent event) {
        Log.d(TAG, "onTileServiceEvent:" + event);

        if (event.isEnable()) {
            // Service Started
            // Ignore user'touch event at service start
            getWindowManager().updateViewLayout(getTouchView(), updateTouchViewParams());

            // 알림영역을 close.
            sendBroadcastCloseSystemDialog();

            // todo Remove. for temp code
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    WindowManager w = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
//                    w.updateViewLayout(getTouchView(), createTouchViewParams());
//                    EventBus.getDefault().post(new TileServiceEvent(false));
//                }
//            }, 3000);
            // end
        } else {
            // Service ended
            // Enable user'touch event at service end
            getWindowManager().updateViewLayout(getTouchView(), createTouchViewParams());
        }
    }

    /// ---------------------------------------------------------------------- EventBus Listener end

    /// -------



    /// -----
}
