package com.moddedmite.mitemod.goki_stats.common.network;

import com.moddedmite.mitemod.goki_stats.client.gui.GuiStats;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.Minecraft;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;

public class S2COpenGui implements Packet {
    public int id;

    public S2COpenGui() {
    }

    public S2COpenGui(int id) {
        this.id = id;
    }

    public S2COpenGui(PacketByteBuf buf) {
        this.id = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.id);
    }

    @Override
    public void apply(EntityPlayer player) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiStats(player));
    }

    @Override
    public ResourceLocation getChannel() {
        return GokiNetwork.OPEN_GUI;
    }
}