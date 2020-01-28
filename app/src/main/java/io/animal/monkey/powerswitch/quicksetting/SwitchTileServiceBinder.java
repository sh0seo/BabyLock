package io.animal.monkey.powerswitch.quicksetting;

import android.os.Binder;

public class SwitchTileServiceBinder extends Binder {

    private SwitchTileService service;

    public SwitchTileServiceBinder(SwitchTileService s) {
        this.service = s;
    }

    public SwitchTileService getService() {
        return service;
    }

}
