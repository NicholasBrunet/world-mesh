package dev.worldmesh.regionworker.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.worldmesh.regionmodel.RegionBounds;
import dev.worldmesh.regionmodel.RegionDirection;
import dev.worldmesh.regionmodel.RegionId;
import dev.worldmesh.regionmodel.RegionNeighbor;
import dev.worldmesh.regionmodel.RegionPosition;

public final class RegionConfig {

    private final RegionId regionId;
    private final String host;
    private final int port;
    private final RegionBounds bounds;
    private final RegionPosition spawnPosition;
    private final List<RegionNeighbor> neighbors;

    private RegionConfig(
            RegionId regionId,
            String host,
            int port,
            RegionBounds bounds,
            RegionPosition spawnPosition,
            List<RegionNeighbor> neighbors
    ) {
        this.regionId = regionId;
        this.host = host;
        this.port = port;
        this.bounds = bounds;
        this.spawnPosition = spawnPosition;
        this.neighbors = List.copyOf(neighbors);
    }

    public static RegionConfig fromEnvironment() {
        RegionBounds bounds = new RegionBounds(
                envInt("WORLD_MESH_REGION_MIN_X", -128),
                envInt("WORLD_MESH_REGION_MAX_X", 127),
                envInt("WORLD_MESH_REGION_MIN_Z", -128),
                envInt("WORLD_MESH_REGION_MAX_Z", 127)
        );

        RegionPosition spawnPosition = bounds.center(
                envDouble("WORLD_MESH_SPAWN_Y", 42.0)
        );

        return new RegionConfig(
                new RegionId(env("WORLD_MESH_REGION_ID", "region-dev")),
                env("WORLD_MESH_HOST", "0.0.0.0"),
                envInt("WORLD_MESH_PORT", 25565),
                bounds,
                spawnPosition,
                loadNeighbors()
        );
    }

    public RegionId regionId() {
        return regionId;
    }

    public String host() {
        return host;
    }

    public int port() {
        return port;
    }

    public RegionBounds bounds() {
        return bounds;
    }

    public RegionPosition spawnPosition() {
        return spawnPosition;
    }

    public List<RegionNeighbor> neighbors() {
        return neighbors;
    }

    public Optional<RegionNeighbor> neighbor(RegionDirection direction) {
        return neighbors.stream()
                .filter(neighbor -> neighbor.direction() == direction)
                .findFirst();
    }

    public String bindAddress() {
        return host + ":" + port;
    }

    private static List<RegionNeighbor> loadNeighbors() {
        List<RegionNeighbor> neighbors = new ArrayList<>();

        addNeighborIfPresent(neighbors, RegionDirection.NORTH);
        addNeighborIfPresent(neighbors, RegionDirection.SOUTH);
        addNeighborIfPresent(neighbors, RegionDirection.EAST);
        addNeighborIfPresent(neighbors, RegionDirection.WEST);

        return neighbors;
    }

    private static void addNeighborIfPresent(List<RegionNeighbor> neighbors, RegionDirection direction) {
        String prefix = "WORLD_MESH_NEIGHBOR_" + direction.name();

        String regionId = System.getenv(prefix + "_ID");
        String endpoint = System.getenv(prefix + "_ENDPOINT");

        if (regionId == null || regionId.isBlank()) {
            return;
        }

        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalArgumentException(prefix + "_ENDPOINT is required when " + prefix + "_ID is set.");
        }

        neighbors.add(new RegionNeighbor(
                direction,
                new RegionId(regionId),
                endpoint
        ));
    }

    private static String env(String key, String fallback) {
        String value = System.getenv(key);

        if (value == null || value.isBlank()) {
            return fallback;
        }

        return value;
    }

    private static int envInt(String key, int fallback) {
        String value = System.getenv(key);

        if (value == null || value.isBlank()) {
            return fallback;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Environment variable " + key + " must be an integer.", error);
        }
    }

    private static double envDouble(String key, double fallback) {
        String value = System.getenv(key);

        if (value == null || value.isBlank()) {
            return fallback;
        }

        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Environment variable " + key + " must be a decimal number.", error);
        }
    }
}