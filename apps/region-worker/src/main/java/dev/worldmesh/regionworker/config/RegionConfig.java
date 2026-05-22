package dev.worldmesh.regionworker.config;

import dev.worldmesh.regionmodel.RegionBounds;
import dev.worldmesh.regionmodel.RegionPosition;

public final class RegionConfig {

    private final String regionId;
    private final String host;
    private final int port;
    private final RegionBounds bounds;
    private final RegionPosition spawnPosition;

    private RegionConfig(
            String regionId,
            String host,
            int port,
            RegionBounds bounds,
            RegionPosition spawnPosition
    ) {
        this.regionId = regionId;
        this.host = host;
        this.port = port;
        this.bounds = bounds;
        this.spawnPosition = spawnPosition;
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
                env("WORLD_MESH_REGION_ID", "region-dev"),
                env("WORLD_MESH_HOST", "0.0.0.0"),
                envInt("WORLD_MESH_PORT", 25565),
                bounds,
                spawnPosition
        );
    }

    public String regionId() {
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

    public String bindAddress() {
        return host + ":" + port;
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
            return Integer.parseInt(value);
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
            return Double.parseDouble(value);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Environment variable " + key + " must be a decimal number.", error);
        }
    }
}