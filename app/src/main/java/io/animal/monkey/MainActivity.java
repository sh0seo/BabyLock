package io.animal.monkey;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

import io.animal.monkey.bus.events.AlertBoxStatusEvent;
import io.animal.monkey.bus.events.AppGuideEvent;
import io.animal.monkey.setting.SettingFragment;
import io.animal.monkey.ui.alert.PermissionFragment;
import io.animal.monkey.ui.guide.AppGuideFragment;
import io.animal.monkey.util.PermissionHelper;
import io.animal.monkey.util.SharedPreferencesHelper;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private SettingFragment settingFragment;

    private AppGuideFragment appGuideFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settingFragment = new SettingFragment();
        permissionFragment = new PermissionFragment();
        appGuideFragment = new AppGuideFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_container, settingFragment)
                    .commitNow();
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

        // check model
        boolean notSupportedDevice = Arrays.asList(getNotSupportedModels()).contains(Build.MODEL);
        if (notSupportedDevice) {
            new AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("제조사에서 필요한 기능을 제공하지 않는 모델입니다.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
            return;
        }

        PermissionHelper permissionHelper = new PermissionHelper(this);
        if (!permissionHelper.hasSystemAlertWindowsPermission()
                || !permissionHelper.isAccessibilitySettingsOn()) {
            onInitializeSharedPref();

            showPermissionAlertBox();
        }

        // App 사용 가이드를 본적이 없다면 Guide
        if (!getSharedPref().getGuided()) {
            showAppGuide();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppGuideEvent(AppGuideEvent e) {
        showAppGuide();
    }

    PermissionFragment permissionFragment;

    private void showPermissionAlertBox() {
        if (permissionFragment.isAdded() && permissionFragment.isVisible()) {
           return;
        }

        permissionFragment.show(getSupportFragmentManager().beginTransaction(), PermissionFragment.TAG);
    }

    private void showAppGuide() {
        if (appGuideFragment.isAdded() && appGuideFragment.isVisible()) {
            return;
        }

        appGuideFragment.show(getSupportFragmentManager().beginTransaction(), AppGuideFragment.TAG);
    }

    private void dismissAppGuide() {
        getSupportFragmentManager().beginTransaction().hide(appGuideFragment);
    }

    private View getRootView() {
        return getWindow().getDecorView().getRootView();
    }

    /// --------------------------------------------------------------------------------- SharedPref

    private SharedPreferencesHelper _sp;

    private SharedPreferencesHelper getSharedPref() {
        if (_sp == null) {
            _sp = new SharedPreferencesHelper(this);
        }
        return _sp;
    }

    private void onInitializeSharedPref() {
        getSharedPref().setBabyMode(false);
    }

    /// ----------------------------------------------------------------------------- SharedPref end

    /// ----------------------------------------------------------------------------------- Resource

    private String[] getNotSupportedModels() {
        return getResources().getStringArray(getNotSupportedModel());
    }

    @SuppressLint("ResourceType")
    private int getNotSupportedModel() {
        return R.array.not_supported_model;
    }
    /// ------------------------------------------------------------------------------- Resource end

    /// -------------------------------------------------------------------------------------- AdMob

//    // AdMob
//    private InterstitialAd mInterstitialAd;
//
//    private void initializeAdMob() {
//        // Initialize the Mobile Ads SDK.
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {}
//        });
//
//        // Create the InterstitialAd and set the adUnitId.
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id_for_full_test));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Log.d(TAG, "onAdLoaded");
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                Log.d(TAG, "onAdFailedToLoad");
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//                Log.d(TAG, "onAdOpended");
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//                Log.d(TAG, "onAdClicked");
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                Log.d(TAG, "onAdLeftApplication");
//            }
//
//            @Override
//            public void onAdClosed() {
//                Log.d(TAG, "onAdClosed");
//
//                // load next ad.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//                // todo stop service
//            }
//        });
//    }
//
//    private void showInterstitial() {
//        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }
//    }

    /// ---------------------------------------------------------------------------------- AdMob end
}
