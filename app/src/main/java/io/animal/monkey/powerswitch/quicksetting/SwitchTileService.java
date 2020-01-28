package io.animal.monkey.powerswitch.quicksetting;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class SwitchTileService extends TileService {

    private final static String TAG = SwitchTileService.class.getSimpleName();

    private IBinder binder;

    private Tile tile;

    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new SwitchTileServiceBinder(this);
        }
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        binder = new SwitchTileServiceBinder(this);
    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG, "onClick()");

        int state = getQsTile().getState();
        if (state == Tile.STATE_INACTIVE) {
            getQsTile().setState(Tile.STATE_ACTIVE);
        } else if (state == Tile.STATE_ACTIVE) {
            // TODO Show AdMob.
            // TODO AdMob Success and be change inactive.
            getQsTile().setState(Tile.STATE_INACTIVE);
        }

        getQsTile().updateTile();
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

    public Tile getTile() {
        if (tile == null) {
            tile = getQsTile();
        }
        return tile;
    }

    private void updateTile() {
        Tile tile = getQsTile();
//        int state = tile.getState();
//        if (state != Tile.STATE_INACTIVE) {
//            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
//        }
    }

}
