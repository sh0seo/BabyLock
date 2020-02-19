package io.animal.monkey.bus.events;

public class TileServiceEvent {

    private boolean enableTileService;

    public TileServiceEvent(boolean enable) {
        this.enableTileService = enableTileService;
    }

    public boolean isEnable() {
        return enableTileService;
    }
}
