package io.animal.monkey;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.monkey.bus.events.AlertBoxStatusEvent;
import io.animal.monkey.setting.SettingFragment;
import io.animal.monkey.ui.alert.PermissionFragment;
import io.animal.monkey.ui.alert.TileServiceGuideFragment;
import io.animal.monkey.util.PermissionHelper;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private SettingFragment settingFragment;

    private TileServiceGuideFragment tileServiceGuideFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        settingFragment = new SettingFragment();
        permissionFragment = new PermissionFragment();
        tileServiceGuideFragment = new TileServiceGuideFragment();

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
                    .replace(R.id.content_container, settingFragment)
                    .commitNow();
        }

//        try {
//            ActionBar actionBar = getSupportActionBar();
//            if (actionBar != null) {
                // app icon
//                actionBar.setIcon(R.drawable.ic_child_care_black_24dp);
//                actionBar.setDisplayUseLogoEnabled(true);
//                actionBar.setDisplayShowHomeEnabled(true);

                // Rubik font
//                SpannableString s = new SpannableString(" BABYLOCK");
//                s.setSpan(new TypefaceSpan("rubik_black.ttf"), 0, s.length(),
//                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                actionBar.setTitle(s);
//            }
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }

        initializeAdMob();

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
//           testShowTileServiceGuide();
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

    PermissionFragment permissionFragment;

    private void showPermissionAlertBox() {
        if (permissionFragment.isAdded() && permissionFragment.isVisible()) {
           return;
        }

        permissionFragment.show(getSupportFragmentManager().beginTransaction(), PermissionFragment.TAG);

//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        alertDialog.setTitle("권한확인");
//        alertDialog.setMessage("앱 구동에 필요한 권한 확인이 필요합니다.");
//        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(this, SettingFragment.class);
//                startActivity(intent);
//                Snackbar.make(getRootView(), "Show Full Dialog Box", Snackbar.LENGTH_SHORT).show();
//
//            }
//        });
//        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        alertDialog.setCancelable(false);
//        alertDialog.show();
    }

    private void testShowTileServiceGuide() {
        if (tileServiceGuideFragment.isAdded() && tileServiceGuideFragment.isVisible()) {
            return;
        }

        tileServiceGuideFragment.show(
                getSupportFragmentManager().beginTransaction(),
                TileServiceGuideFragment.TAG);
    }

    private View getRootView() {
        return getWindow().getDecorView().getRootView();
    }

    /// -------------------------------------------------------------------------------------- AdMob

    // AdMob
    private InterstitialAd mInterstitialAd;

    private void initializeAdMob() {
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id_for_full_test));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.d(TAG, "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.d(TAG, "onAdOpended");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(TAG, "onAdClicked");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG, "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                Log.d(TAG, "onAdClosed");

                // load next ad.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                // todo stop service
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    /// ---------------------------------------------------------------------------------- AdMob end
}
