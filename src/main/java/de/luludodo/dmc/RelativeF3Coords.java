package de.luludodo.dmc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class RelativeF3Coords {
    private static double oldX;
    private static double oldY;
    private static double oldZ;
    private static int oldBlockX;
    private static int oldBlockY;
    private static int oldBlockZ;
    private static boolean closed = true;

    public static void init() {
        // Initializer Class to register a tickEvent at START_CLIENT_TICK which sets oldX, oldY, etc. to the current corresponding coord
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            if (minecraft.getDebugHud().shouldShowDebugHud() && closed) {
                // set oldX, oldY, etc. to corresponding coord
                oldX = minecraft.getCameraEntity().getX();
                oldY = minecraft.getCameraEntity().getY();
                oldZ = minecraft.getCameraEntity().getZ();
                oldBlockX = minecraft.getCameraEntity().getBlockX();
                oldBlockY = minecraft.getCameraEntity().getBlockY();
                oldBlockZ = minecraft.getCameraEntity().getBlockZ();
                // set closed to false so it only happens once
                closed = false;
            } else if (!minecraft.getDebugHud().shouldShowDebugHud()){
                // reset closed
                closed = true;
            }
        });
    }

    // A LOT OF GETTERS
    public static double getOldX() {
        return oldX;
    }
    public static double getOldY() {
        return oldY;
    }
    public static double getOldZ() {
        return oldZ;
    }
    public static int getOldBlockX() {
        return oldBlockX;
    }
    public static int getOldBlockY() {
        return oldBlockY;
    }
    public static int getOldBlockZ() {
        return oldBlockZ;
    }
}
