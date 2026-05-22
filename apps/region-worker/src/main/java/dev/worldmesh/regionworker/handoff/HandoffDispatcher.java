package dev.worldmesh.regionworker.handoff;

import dev.worldmesh.regionmodel.RegionNeighbor;
import dev.worldmesh.transfermodel.HandoffIntent;

public interface HandoffDispatcher {

    void dispatch(HandoffIntent intent, RegionNeighbor target);
}