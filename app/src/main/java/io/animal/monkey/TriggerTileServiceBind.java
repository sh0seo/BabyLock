package io.animal.monkey;

import android.os.Binder;
import android.service.quicksettings.TileService;

public class TriggerTileServiceBind extends Binder {

    private TileService service;

    public TriggerTileServiceBind(TileService s) {
        this.service = s;
    }

    public TileService getService() {
        return service;
    }

}
