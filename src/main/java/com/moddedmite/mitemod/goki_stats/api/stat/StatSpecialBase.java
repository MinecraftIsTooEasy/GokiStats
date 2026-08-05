package com.moddedmite.mitemod.goki_stats.api.stat;

import com.moddedmite.mitemod.goki_stats.common.config.stats.StatConfig;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.EntityPlayer;

public abstract class StatSpecialBase extends StatBase<StatConfig> implements StatSpecial {
    public StatSpecialBase(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public final float getSecondaryBonus(EntityPlayer player) {
        return getSecondaryBonus(DataHelper.getPlayerStatLevel(player, this));
    }
}