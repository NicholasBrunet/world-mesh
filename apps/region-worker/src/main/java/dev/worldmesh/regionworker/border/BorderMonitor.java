package dev.worldmesh.regionworker.border;

import dev.worldmesh.regionmodel.RegionDirection;
import dev.worldmesh.regionmodel.RegionNeighbor;
import dev.worldmesh.regionworker.config.RegionConfig;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class BorderMonitor {

    private static final long MESSAGE_COOLDOWN_MILLIS = 1_500;

    private final RegionConfig config;
    private final Map<UUID, Long> lastMessageTimes = new HashMap<>();

    public BorderMonitor(RegionConfig config) {
        this.config = config;
    }

    public void register(GlobalEventHandler events) {
        events.addListener(PlayerMoveEvent.class, this::onPlayerMove);
    }

    private void onPlayerMove(PlayerMoveEvent event) {
        Pos position = event.getNewPosition();

        if (config.bounds().contains(position.x(), position.z())) {
            return;
        }

        RegionDirection exitDirection = config.bounds().exitDirection(position.x(), position.z());
        Optional<RegionNeighbor> neighbor = config.neighbor(exitDirection);

        UUID playerId = event.getPlayer().getUuid();

        if (!canSendMessage(playerId)) {
            return;
        }

        if (neighbor.isPresent()) {
            RegionNeighbor target = neighbor.get();

            event.getPlayer().sendMessage(Component.text(
                    "Border exit detected: " + exitDirection + " -> " + target.regionId()
            ));

            System.out.println(
                    "Player " + playerId
                            + " exited " + config.regionId()
                            + " toward " + exitDirection
                            + " target=" + target.regionId()
                            + " endpoint=" + target.endpoint()
                            + " position=" + position
            );
        } else {
            event.getPlayer().sendMessage(Component.text(
                    "Border exit detected: " + exitDirection + " but no neighbor is configured."
            ));

            System.out.println(
                    "Player " + playerId
                            + " exited " + config.regionId()
                            + " toward " + exitDirection
                            + " but no neighbor is configured"
                            + " position=" + position
            );
        }
    }

    private boolean canSendMessage(UUID playerId) {
        long now = System.currentTimeMillis();
        long lastMessageTime = lastMessageTimes.getOrDefault(playerId, 0L);

        if (now - lastMessageTime < MESSAGE_COOLDOWN_MILLIS) {
            return false;
        }

        lastMessageTimes.put(playerId, now);
        return true;
    }
}