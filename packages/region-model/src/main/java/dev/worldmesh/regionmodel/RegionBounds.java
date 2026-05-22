package dev.worldmesh.regionmodel;

public record RegionBounds(
        int minX,
        int maxX,
        int minZ,
        int maxZ
) {

    public RegionBounds {
        if (minX > maxX) {
            throw new IllegalArgumentException("minX cannot be greater than maxX.");
        }

        if (minZ > maxZ) {
            throw new IllegalArgumentException("minZ cannot be greater than maxZ.");
        }
    }

    public boolean contains(double x, double z) {
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }

    public RegionDirection exitDirection(double x, double z) {
        double westDistance = Math.abs(x - minX);
        double eastDistance = Math.abs(x - maxX);
        double northDistance = Math.abs(z - minZ);
        double southDistance = Math.abs(z - maxZ);

        if (x < minX) {
            return RegionDirection.WEST;
        }

        if (x > maxX) {
            return RegionDirection.EAST;
        }

        if (z < minZ) {
            return RegionDirection.NORTH;
        }

        if (z > maxZ) {
            return RegionDirection.SOUTH;
        }

        RegionDirection nearestDirection = RegionDirection.WEST;
        double nearestDistance = westDistance;

        if (eastDistance < nearestDistance) {
            nearestDirection = RegionDirection.EAST;
            nearestDistance = eastDistance;
        }

        if (northDistance < nearestDistance) {
            nearestDirection = RegionDirection.NORTH;
            nearestDistance = northDistance;
        }

        if (southDistance < nearestDistance) {
            nearestDirection = RegionDirection.SOUTH;
        }

        return nearestDirection;
    }

    public int widthX() {
        return maxX - minX + 1;
    }

    public int widthZ() {
        return maxZ - minZ + 1;
    }

    public RegionPosition center(double y) {
        double centerX = minX + (widthX() / 2.0);
        double centerZ = minZ + (widthZ() / 2.0);

        return new RegionPosition(centerX, y, centerZ);
    }

    public String asDebugString() {
        return "x=" + minX + ".." + maxX + ", z=" + minZ + ".." + maxZ;
    }
}