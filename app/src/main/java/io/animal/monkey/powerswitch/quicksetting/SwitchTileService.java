package io.animal.monkey.powerswitch.quicksetting;

import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

import io.animal.monkey.util.SharedPreferencesHelper;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchTileService extends TileService {

    private final static String TAG = SwitchTileService.class.getSimpleName();

//    private IBinder binder;

    private Tile tile;

    private SharedPreferencesHelper sp;

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

        int state = getTile().getState();
        if (state == Tile.STATE_INACTIVE) {
            getTile().setState(Tile.STATE_ACTIVE);
            getPref().setTileState(Tile.STATE_ACTIVE);
        } else if (state == Tile.STATE_ACTIVE) {
            // TODO Show AdMob.
            // TODO AdMob Success and be change inactive.
            getTile().setState(Tile.STATE_INACTIVE);
            getPref().setTileState(Tile.STATE_INACTIVE);
        } else if (state == Tile.STATE_UNAVAILABLE) {
            // TODO Show permission Dialog.
        } else {
            throw new UnknownError("Tile Service State is " + state);
        }

        getTile().updateTile();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG, "onTileAdded()");

        updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening()");

        updateTile();
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

//    public int getState() {
//        return getTile().getState();
//    }

//    private IBinder getBinder() {
//        if (binder == null) {
//            binder = new SwitchTileServiceBinder(this);
//        }
//        return binder;
//    }

    private void updateTile() {
        Tile tile = getQsTile();
//        int state = tile.getState();
//        if (state != Tile.STATE_INACTIVE) {
//            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
//        }

        // TODO check permission.
        int state = tile.getState();
        if (state != Tile.STATE_INACTIVE) {
        }
    }

}
