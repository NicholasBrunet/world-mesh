package dev.worldmesh.regionworker;

import dev.worldmesh.regionworker.command.RegionCommand;
import dev.worldmesh.regionworker.config.RegionConfig;
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
        registerPlayerSetup(instance);
        registerCommands(config);

        System.out.println("Starting WorldMesh RegionWorker");
        System.out.println("Region ID: " + config.regionId());
        System.out.println("Bind: " + config.bindAddress());

        server.start(config.host(), config.port());
    }

    private static InstanceContainer createInstance() {
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();

        instance.setGenerator(RegionWorldGenerator.flatGrassWorld());

        return instance;
    }

    private static void registerPlayerSetup(InstanceContainer instance) {
        GlobalEventHandler events = MinecraftServer.getGlobalEventHandler();

        events.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            event.setSpawningInstance(instance);

            event.getPlayer().setRespawnPoint(new Pos(0, 42, 0));
            event.getPlayer().setGameMode(GameMode.CREATIVE);
        });
    }

    private static void registerCommands(RegionConfig config) {
        MinecraftServer.getCommandManager().register(new RegionCommand(config));
    }
}