package dev.worldmesh.transfermodel;

import java.time.Instant;
import java.util.UUID;

import dev.worldmesh.regionmodel.RegionDirection;
import dev.worldmesh.regionmodel.RegionId;

public record HandoffIntent(
        UUID playerId,
        RegionId sourceRegionId,
        RegionId targetRegionId,
        RegionDirection exitDirection,
        HandoffPosition position,
        Instant createdAt
) {

    public HandoffIntent {
        if (playerId == null) {
            throw new IllegalArgumentException("Player ID cannot be null.");
        }

        if (sourceRegionId == null) {
            throw new IllegalArgumentException("Source region ID cannot be null.");
        }

        if (targetRegionId == null) {
            throw new IllegalArgumentException("Target region ID cannot be null.");
        }

        if (exitDirection == null) {
            throw new IllegalArgumentException("Exit direction cannot be null.");
        }

        if (position == null) {
            throw new IllegalArgumentException("Handoff position cannot be null.");
        }

        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public static HandoffIntent create(
            UUID playerId,
            RegionId sourceRegionId,
            RegionId targetRegionId,
            RegionDirection exitDirection,
            HandoffPosition position
    ) {
        return new HandoffIntent(
                playerId,
                sourceRegionId,
                targetRegionId,
                exitDirection,
                position,
                Instant.now()
        );
    }

    public String asDebugString() {
        return "player=" + playerId
                + ", source=" + sourceRegionId
                + ", target=" + targetRegionId
                + ", direction=" + exitDirection
                + ", position=" + position
                + ", createdAt=" + createdAt;
    }
}