package dev.worldmesh.regionworker.config;

public final class RegionConfig {

    private final String regionId;
    private final String host;
    private final int port;

    private RegionConfig(String regionId, String host, int port) {
        this.regionId = regionId;
        this.host = host;
        this.port = port;
    }

    public static RegionConfig fromEnvironment() {
        return new RegionConfig(
                env("WORLD_MESH_REGION_ID", "region-dev"),
                env("WORLD_MESH_HOST", "0.0.0.0"),
                envInt("WORLD_MESH_PORT", 25565)
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
}