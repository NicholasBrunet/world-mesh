package dev.worldmesh.regionworker;

import dev.worldmesh.regionmodel.RegionPosition;
import dev.worldmesh.regionworker.border.BorderMonitor;
import dev.worldmesh.regionworker.command.RegionCommand;
import dev.worldmesh.regionworker.config.RegionConfig;
import dev.worldmesh.regionworker.handoff.LoggingHandoffDispatcher;
import dev.worldmesh.regionworker.handoff.LoggingHandoffReceiver;
import dev.worldmesh.regionworker.world.RegionWorldGenerator;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        RegionConfig config = RegionConfig.fromEnvironment();

        MinecraftServer server = MinecraftServer.init();

        InstanceContainer instance = createInstance();
        GlobalEventHandler events = MinecraftServer.getGlobalEventHandler();

        registerPlayerSetup(events, instance, config);
        registerCommands(config);
        registerHandoffSystems(events, config);

        System.out.println("Starting WorldMesh RegionWorker");
        System.out.println("Region ID: " + config.regionId());
        System.out.println("Bind: " + config.bindAddress());
        System.out.println("Bounds: " + config.bounds().asDebugString());
        System.out.println("Spawn: " + config.spawnPosition());

        server.start(config.host(), config.port());
    }

    private static InstanceContainer createInstance() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();

        instance.setGenerator(RegionWorldGenerator.flatGrassWorld());

        return instance;
    }

    private static void registerPlayerSetup(
            GlobalEventHandler events,
            InstanceContainer instance,
            RegionConfig config
    ) {
        events.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            RegionPosition spawn = config.spawnPosition();

            event.setSpawningInstance(instance);

            event.getPlayer().setRespawnPoint(new Pos(spawn.x(), spawn.y(), spawn.z()));
            event.getPlayer().setGameMode(GameMode.CREATIVE);
        });
    }

    private static void registerCommands(RegionConfig config) {
        MinecraftServer.getCommandManager().register(new RegionCommand(config));
    }

    private static void registerHandoffSystems(GlobalEventHandler events, RegionConfig config) {
        LoggingHandoffReceiver receiver = new LoggingHandoffReceiver(config);

        new BorderMonitor(
                config,
                new LoggingHandoffDispatcher()
        ).register(events);

        System.out.println("Registered handoff receiver: " + receiver.getClass().getSimpleName());
    }
}