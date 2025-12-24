package dk.mosberg.api.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Helper for sending custom packets between client and server
 */
public class NetworkHelper {

    public static void sendToServer(CustomPayload payload) {
        // Client -> Server
    }

    public static void sendToPlayer(ServerPlayerEntity player, CustomPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendToAllPlayers(ServerWorld world, CustomPayload payload) {
        // Broadcast to all players
    }
}
