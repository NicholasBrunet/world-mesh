package dev.worldmesh.regionworker.handoff;

import dev.worldmesh.regionworker.config.RegionConfig;
import dev.worldmesh.transfermodel.HandoffIntent;

public final class LoggingHandoffReceiver implements HandoffReceiver {

    private final RegionConfig config;

    public LoggingHandoffReceiver(RegionConfig config) {
        this.config = config;
    }

    @Override
    public void receive(HandoffIntent intent) {
        if (!intent.targetRegionId().equals(config.regionId())) {
            System.out.println("Ignoring handoff intent for different target region:");
            System.out.println("Current region: " + config.regionId());
            System.out.println("Intent: " + intent.asDebugString());
            return;
        }

        System.out.println("Received inbound handoff intent:");
        System.out.println("Intent: " + intent.asDebugString());
    }
}