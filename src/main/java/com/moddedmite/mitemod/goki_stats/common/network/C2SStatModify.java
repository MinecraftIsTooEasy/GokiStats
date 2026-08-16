package com.moddedmite.mitemod.goki_stats.common.network;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.common.config.GokiConfig;
import com.moddedmite.mitemod.goki_stats.common.stat.StatMaxHealth;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.ServerPlayer;
import net.minecraft.ResourceLocation;

public class C2SStatModify implements Packet {
    public int stat;
    public int amount;

    public C2SStatModify() {
    }

    public C2SStatModify(int stat, int amount) {
        this.stat = stat;
        this.amount = amount;
    }

    public C2SStatModify(PacketByteBuf buf) {
        this.stat = buf.readInt();
        this.amount = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.stat);
        buf.writeInt(this.amount);
    }

    @Override
    public void apply(EntityPlayer player) {
        ServerPlayer playerMP = (ServerPlayer) player;
        StatBase stat = StatBase.stats.get(this.stat);
        if (!stat.enabled)
            return;

        int level = DataHelper.getPlayerStatLevel(playerMP, stat);

        if (this.amount < 0 && level == 0)
            return;

        if (level + this.amount > stat.getLimit())
            return;

        int cost = stat.getCost(level + this.amount - 1);
        int currentXP = DataHelper.getXPTotal(playerMP);

        int reverted = DataHelper.getPlayerRevertStatLevel(playerMP, stat);
        reverted = Math.max(reverted - this.amount, 0);
        if (GokiConfig.globalModifiers.globalMaxRevertLevel < reverted && GokiConfig.globalModifiers.globalMaxRevertLevel != -1)
            return;
        if (this.amount <= 0)
            DataHelper.setPlayerRevertStatLevel(playerMP, stat, reverted);

        if (this.amount > 0) {
            // Upgrade: requires enough XP
            if (currentXP >= cost) {
                DataHelper.setPlayerStatLevel(playerMP, stat, level + this.amount);
                DataHelper.setPlayersExpTo(playerMP, currentXP - cost);
            }
        } else {
            // Downgrade: always allowed (level > 0 and revert limit already checked)
            DataHelper.setPlayerStatLevel(playerMP, stat, level + this.amount);
            DataHelper.setPlayersExpTo(playerMP,
                    currentXP + (int) (stat.getCost(level + this.amount + 1) * GokiConfig.globalModifiers.globalRevertFactor));
        }

        // Sync back to client
        GokiNetwork.sendToClient(playerMP, new S2CSyncAll(playerMP));
    }

    @Override
    public ResourceLocation getChannel() {
        return GokiNetwork.STAT_MODIFY;
    }
}