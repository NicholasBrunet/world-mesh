package dev.worldmesh.regionworker;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

public final class Main {

	private static final String REGION_ID = env("WORLD_MESH_REGION_ID", "region-dev");
	private static final String HOST = env("WORLD_MESH_HOST", "0.0.0.0");
	private static final int PORT = envInt("WORLD_MESH_PORT", 25565);

	private Main() {
	}

	public static void main(String[] args) {
		MinecraftServer server = MinecraftServer.init();

		InstanceManager instanceManager = MinecraftServer.getInstanceManager();
		InstanceContainer instance = instanceManager.createInstanceContainer();

		instance.setGenerator(unit -> {
			unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
		});

		registerPlayerSetup(instance);
		registerRegionCommand();

		System.out.println("Starting WorldMesh RegionWorker");
		System.out.println("Region ID: " + REGION_ID);
		System.out.println("Bind: " + HOST + ":" + PORT);

		server.start(HOST, PORT);
	}

	private static void registerPlayerSetup(InstanceContainer instance) {
		GlobalEventHandler events = MinecraftServer.getGlobalEventHandler();

		events.addListener(AsyncPlayerConfigurationEvent.class, event -> {
			event.setSpawningInstance(instance);

			event.getPlayer().setRespawnPoint(new Pos(0, 42, 0));
			event.getPlayer().setGameMode(GameMode.CREATIVE);
		});
	}

	private static void registerRegionCommand() {
		Command command = new Command("region");

		command.setDefaultExecutor((sender, context) -> {
			sender.sendMessage(Component.text("WorldMesh RegionWorker"));
			sender.sendMessage(Component.text("Region ID: " + REGION_ID));
			sender.sendMessage(Component.text("Bind: " + HOST + ":" + PORT));
		});

		MinecraftServer.getCommandManager().register(command);
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