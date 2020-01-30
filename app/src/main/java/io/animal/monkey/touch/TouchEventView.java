package io.animal.monkey.touch;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.PixelFormat;
import android.os.Build;
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

public class TouchEventView extends ContextWrapper implements View.OnTouchListener {

    private final static String TAG = "TouchEventView";

    private int miniTouchGestureHeight;

    private View touchView;

    private ImageView kidModeImage;

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
        kidModeImage = touchView.findViewById(R.id.kid_mode);
//        touchView = new View(getApplicationContext());
//        touchView.setBackgroundColor(Color.parseColor("#44FF1122"));
        touchView.setOnTouchListener(this);
    }

    public void updateMiniTouchGestureHeight(@Nullable Integer heightPx) {
        miniTouchGestureHeight = heightPx;
    }

    public void updateParamsForLocation(WindowManager windowManager, Boolean isPortrait) {
        windowManager.addView(touchView, createTouchViewParams());

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
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
        } else {
            params = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch(" + event.toString() + ")");
        if (true) {
            if (event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS) {
                // TODO event prevent
            }
        } else {
            // TODO event prevent
        }

        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showKidMode(KidModeEvent e) {
        Log.d(TAG, "showKizMode()");
        kidModeImage.animate().alpha(0.0f).setDuration(1000).start();
    }

}
