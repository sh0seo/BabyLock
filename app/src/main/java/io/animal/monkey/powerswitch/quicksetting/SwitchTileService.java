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

    public SwitchTileService() {
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Override
    public void onClick() {
        super.onClick();
        Log.d(TAG, "onClick()");

        int state = getQsTile().getState();
        if (state == Tile.STATE_INACTIVE) {
            getQsTile().setState(Tile.STATE_ACTIVE);
        } else if (state == Tile.STATE_ACTIVE) {
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

    private void updateTile() {
        Tile tile = getQsTile();
//        int state = tile.getState();
//        if (state != Tile.STATE_INACTIVE) {
//            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
//        }
    }

}
