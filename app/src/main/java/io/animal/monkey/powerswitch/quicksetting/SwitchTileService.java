package io.animal.monkey.powerswitch.quicksetting;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import io.animal.monkey.AdMobActivity;
import io.animal.monkey.util.PermissionHelper;
import io.animal.monkey.util.SharedPreferencesHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchTileService extends TileService {

    private final static String TAG = SwitchTileService.class.getSimpleName();

//    private IBinder binder;

    private Tile tile;

    private SharedPreferencesHelper sp;

    private PermissionHelper permissionHelper;

//    @Override
//    public IBinder onBind(Intent intent) {
//        return getBinder();
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        return super.onUnbind(intent);
//    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG, "onClick()");

        boolean hasAccessibility = getPermissionHelper().isAccessibilitySettingsOn();
        if (!hasAccessibility) {
            // TODO Show permission Dialog.
            Toast.makeText(getApplicationContext(), "not permission", Toast.LENGTH_SHORT).show();
            return;
        }

        int state = getTile().getState();
        if (state == Tile.STATE_INACTIVE) {
            getTile().setState(Tile.STATE_ACTIVE);
            getPref().setTileState(Tile.STATE_ACTIVE);
        } else if (state == Tile.STATE_ACTIVE) {
            // TODO Show AdMob.
            // TODO AdMob Success and be change inactive.
            Intent intent = getAdMobActivityIntent();
            startActivity(intent);

            getTile().setState(Tile.STATE_INACTIVE);
            getPref().setTileState(Tile.STATE_INACTIVE);
        } else if (state == Tile.STATE_UNAVAILABLE) {
            // TODO Show permission Dialog.
            Toast.makeText(getApplicationContext(), "not permission", Toast.LENGTH_SHORT).show();
        } else {
            throw new UnknownError("Tile Service State is " + state);
        }

        getTile().updateTile();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG, "onTileAdded()");

        // 초기화.
        getTile().setState(Tile.STATE_INACTIVE);
        getTile().updateTile();

        updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening()");

        updateTile();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(TAG, "onStopListening()");
    }

    private SharedPreferencesHelper getPref() {
        if (sp == null) {
           sp = new SharedPreferencesHelper(getApplication());
        }

        return sp;
    }

    private Tile getTile() {
        if (tile == null) {
            tile = getQsTile();
        }
        return tile;
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

}
