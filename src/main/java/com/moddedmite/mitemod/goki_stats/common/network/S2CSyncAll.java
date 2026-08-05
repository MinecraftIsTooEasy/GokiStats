package com.moddedmite.mitemod.goki_stats.common.network;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;

public class S2CSyncAll implements Packet {
    public int[] statLevels;
    public int[] revertedStatLevels;

    public S2CSyncAll() {
    }

    public S2CSyncAll(EntityPlayer player) {
        this.statLevels = new int[StatBase.stats.size()];
        this.revertedStatLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            if (StatBase.stats.get(i) != null) {
                this.statLevels[i] = DataHelper.getPlayerStatLevel(player,
                        StatBase.stats.get(i));
                this.revertedStatLevels[i] = DataHelper.getPlayerRevertStatLevel(player,
                        StatBase.stats.get(i));
            }
        }
    }

    public S2CSyncAll(PacketByteBuf buf) {
        this.statLevels = new int[StatBase.stats.size()];
        this.revertedStatLevels = new int[StatBase.stats.size()];
        for (int i = 0; i < this.statLevels.length; i++) {
            this.statLevels[i] = buf.readInt();
        }
        for (int i = 0; i < this.revertedStatLevels.length; i++) {
            this.revertedStatLevels[i] = buf.readInt();
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        for (int statLevel : this.statLevels) {
            buf.writeInt(statLevel);
        }
        for (int revertedStatLevel : this.revertedStatLevels) {
            buf.writeInt(revertedStatLevel);
        }
    }

    @Override
    public void apply(EntityPlayer player) {
        for (int i = 0; i < this.statLevels.length; i++) {
            DataHelper.setPlayerStatLevel(player,
                    StatBase.stats.get(i),
                    this.statLevels[i]);
        }
        for (int i = 0; i < this.revertedStatLevels.length; i++) {
            DataHelper.setPlayerRevertStatLevel(player,
                    StatBase.stats.get(i),
                    this.revertedStatLevels[i]);
        }
        DataHelper.resetMaxHealth(player);
    }

    @Override
    public ResourceLocation getChannel() {
        return GokiNetwork.SYNC_ALL;
    }
}