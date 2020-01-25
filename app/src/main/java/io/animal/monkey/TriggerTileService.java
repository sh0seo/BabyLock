package io.animal.monkey;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TriggerTileService extends TileService {

    private final static String TAG = TriggerTileService.class.getSimpleName();

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Log.d(TAG, "onTileAdded()");

        getQsTile().setState(Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        Log.d(TAG, "onTileRemoved()");

        getQsTile().setState(Tile.STATE_INACTIVE);
        getQsTile().updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening()");
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        Log.d(TAG, "onStopListening()");
    }

    @Override
    public void onClick() {
        super.onClick();

        Tile tile = getQsTile();
        if (tile.getState() == Tile.STATE_INACTIVE) {
            tile.setState(Tile.STATE_ACTIVE);
        } else if (tile.getState() == Tile.STATE_ACTIVE) {
            tile.setState(Tile.STATE_INACTIVE);
        }

        tile.updateTile();
    }
}
