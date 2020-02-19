package io.animal.monkey;

import android.os.Bundle;
import android.os.Handler;
import android.service.quicksettings.Tile;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.greenrobot.eventbus.EventBus;

import io.animal.monkey.bus.events.TileServiceEvent;
import io.animal.monkey.ui.admob.AdMobFragment;
import io.animal.monkey.util.SharedPreferencesHelper;

public class AdMobActivity extends AppCompatActivity {

    private final static String TAG = "AdMobActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admob_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admob_container, AdMobFragment.newInstance())
                    .commitNow();
        }

        initializeAdMob();
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

                showInterstitial();

                // stop service
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new TileServiceEvent(false));
                    }
                }, 2000);
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


                finish();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    /// ---------------------------------------------------------------------------------- AdMob end

    /// -------------------------------------------------------------------------- SharedPreferences

    private SharedPreferencesHelper _sp;

    private SharedPreferencesHelper getSharedPref() {
        if (_sp == null) {
            _sp = new SharedPreferencesHelper(getApplicationContext());
        }

        return _sp;
    }

    /// ---------------------------------------------------------------------- SharedPreferences end
}
