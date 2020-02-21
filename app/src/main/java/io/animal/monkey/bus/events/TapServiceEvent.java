package io.animal.monkey.bus.events;

import androidx.annotation.NonNull;

public class TapServiceEvent {

    private boolean enableTap;

    public TapServiceEvent(boolean enable) {
        this.enableTap = enable;
    }

    public boolean isEnable() {
        return enableTap;
    }

    @NonNull
    @Override
    public String toString() {
        return "tap is " + enableTap;
    }
}
