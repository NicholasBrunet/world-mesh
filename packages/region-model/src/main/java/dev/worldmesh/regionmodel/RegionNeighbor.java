package dev.worldmesh.regionmodel;

public record RegionNeighbor(
        RegionDirection direction,
        RegionId regionId,
        String endpoint
) {

    public RegionNeighbor {
        if (direction == null) {
            throw new IllegalArgumentException("Neighbor direction cannot be null.");
        }

        if (regionId == null) {
            throw new IllegalArgumentException("Neighbor region ID cannot be null.");
        }

        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalArgumentException("Neighbor endpoint cannot be blank.");
        }

        endpoint = endpoint.trim();
    }

    public String asDebugString() {
        return direction + " -> " + regionId + " @ " + endpoint;
    }
}