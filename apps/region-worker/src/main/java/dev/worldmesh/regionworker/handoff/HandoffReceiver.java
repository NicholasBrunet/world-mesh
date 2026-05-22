package dev.worldmesh.regionworker.handoff;

import dev.worldmesh.transfermodel.HandoffIntent;

public interface HandoffReceiver {

    void receive(HandoffIntent intent);
}