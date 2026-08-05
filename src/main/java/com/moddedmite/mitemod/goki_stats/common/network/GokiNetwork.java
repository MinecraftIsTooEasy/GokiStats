package com.moddedmite.mitemod.goki_stats.common.network;

import moddedmite.rustedironcore.network.Network;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ServerPlayer;
import net.minecraft.ResourceLocation;

public class GokiNetwork {
    public static final ResourceLocation CHANNEL = new ResourceLocation("goki_stats", "network");
    public static final ResourceLocation SYNC_ALL = new ResourceLocation("goki_stats", "sync_all");
    public static final ResourceLocation OPEN_GUI = new ResourceLocation("goki_stats", "open_gui");
    public static final ResourceLocation STAT_MODIFY = new ResourceLocation("goki_stats", "stat_modify");

    public static void registerPackets() {
        // Server-side packet readers (client -> server)
        PacketReader.registerServerPacketReader(STAT_MODIFY, C2SStatModify::new);

        // Client-side packet readers (server -> client)
        PacketReader.registerClientPacketReader(SYNC_ALL, S2CSyncAll::new);
        PacketReader.registerClientPacketReader(OPEN_GUI, S2COpenGui::new);
    }

    public static void sendToClient(ServerPlayer player, Packet packet) {
        Network.sendToClient(player, packet);
    }

    public static void sendToServer(Packet packet) {
        Network.sendToServer(packet);
    }

    public static void sendToAllPlayers(Packet packet) {
        Network.sendToAllPlayers(packet);
    }
}