package dev.worldmesh.regionworker.handoff;

import dev.worldmesh.regionmodel.RegionNeighbor;
import dev.worldmesh.transfermodel.HandoffIntent;

public final class LoggingHandoffDispatcher implements HandoffDispatcher {

    @Override
    public void dispatch(HandoffIntent intent, RegionNeighbor target) {
        System.out.println("Dispatching handoff intent:");
        System.out.println("Intent: " + intent.asDebugString());
        System.out.println("Target endpoint: " + target.endpoint());
    }
}