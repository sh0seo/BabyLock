package io.animal.monkey.powerswitch.quicksetting;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.animal.monkey.AdMobActivity;
import io.animal.monkey.bus.events.TileServiceEvent;
import io.animal.monkey.touch.TouchEventView;
import io.animal.monkey.util.PermissionHelper;
import io.animal.monkey.util.SharedPreferencesHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchTileService extends TileService {

    private final static String TAG = SwitchTileService.class.getSimpleName();

    private PermissionHelper permissionHelper;

    private TouchEventView touchEventView;

    @Override
    public void onCreate() {
        super.onCreate();

//        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick() {
        super.onClick();
//        Log.d(TAG, "onClick()");

//        boolean hasAccessibility = getPermissionHelper().isAccessibilitySettingsOn();
//        if (!hasAccessibility) {
//            // TODO Show permission Dialog.
////            Toast.makeText(getApplicationContext(), "not permission", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            return;
//        }
//
//        int state = getTile().getState();
//        if (state == Tile.STATE_INACTIVE) {
//            getTile().setState(Tile.STATE_ACTIVE);
//            getSharedPref().setTileState(Tile.STATE_ACTIVE);
//
//            EventBus.getDefault().post(new TileServiceEvent(true));
//        } else if (state == Tile.STATE_ACTIVE) {
//            // TODO Show AdMob.
//            // TODO AdMob Success and be change inactive.
//            Intent intent = getAdMobActivityIntent();
//            startActivity(intent);
//
//        } else if (state == Tile.STATE_UNAVAILABLE) {
//            // TODO Show permission Dialog.
//            Toast.makeText(getApplicationContext(), "not permission", Toast.LENGTH_SHORT).show();
//        } else {
//            throw new UnknownError("Tile Service State is " + state);
//        }
//
//        getTile().updateTile();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
//        Log.d(TAG, "onTileAdded()");
//
//        // 초기화.
//        getTile().setState(Tile.STATE_INACTIVE);
//        getTile().updateTile();
//
//        updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
//        Log.d(TAG, "onStartListening()");
//
//        updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
//        Log.d(TAG, "onStopListening()");
    }


    private Intent getAdMobActivityIntent() {
        Intent intent = new Intent(getApplicationContext(), AdMobActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    private void updateTile() {
        boolean hasAccessibility = getPermissionHelper().isAccessibilitySettingsOn();
        if (!hasAccessibility) {
            getTile().setState(Tile.STATE_INACTIVE);
            getTile().updateTile();
            return;
        }
    }

    private PermissionHelper getPermissionHelper() {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper(this);
        }

        return permissionHelper;
    }

    /// ----------------------------------------------------------------------------------- EventBus

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTileServiceEvent(TileServiceEvent event) {
        Log.d(TAG, "onTileServiceEvent: " + event.isEnable());
        if (!event.isEnable()) {
            getTile().setState(Tile.STATE_INACTIVE);
            getSharedPref().setTileState(Tile.STATE_INACTIVE);
        }

        getTile().updateTile();
    }

    /// ------------------------------------------------------------------------------- EventBus end

    /// --------------------------------------------------------------------------- SharedPreference

    private SharedPreferencesHelper _sharedPref;

    private SharedPreferencesHelper getSharedPref() {
        if (_sharedPref == null) {
            _sharedPref = new SharedPreferencesHelper(getApplication());
        }

        return _sharedPref;
    }

    /// ----------------------------------------------------------------------- SharedPreference end

    /// --------------------------------------------------------------------------------------- Tile

    private Tile _tile;

    private Tile getTile() {
        if (_tile == null) {
            _tile = getQsTile();
        }
        return _tile;
    }

    /// ----------------------------------------------------------------------------------- Tile end
}
