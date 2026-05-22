package dev.worldmesh.regionworker.command;

import dev.worldmesh.regionworker.config.RegionConfig;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;

public final class RegionCommand extends Command {

    public RegionCommand(RegionConfig config) {
        super("region");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage(Component.text("WorldMesh RegionWorker"));
            sender.sendMessage(Component.text("Region ID: " + config.regionId()));
            sender.sendMessage(Component.text("Bind: " + config.bindAddress()));
        });
    }
}