package dev.worldmesh.regionmodel;

public record RegionId(String value) {

    public RegionId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Region ID cannot be blank.");
        }

        value = value.trim();
    }

    @Override
    public String toString() {
        return value;
    }
}