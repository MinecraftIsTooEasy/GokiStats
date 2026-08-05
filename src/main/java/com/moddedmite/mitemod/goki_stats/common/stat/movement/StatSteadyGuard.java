package com.moddedmite.mitemod.goki_stats.common.stat.movement;

import com.moddedmite.mitemod.goki_stats.common.config.stats.StatConfig;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import net.minecraft.EntityPlayer;

public class StatSteadyGuard extends StatBase<StatConfig> {
    public StatSteadyGuard(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return Math.min(getFinalBonus((float) Math.pow(level, 1.3615D)), 100.0F);
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        // TODO special
        return new float[]
                {DataHelper.trimDecimals(getBonus(player), 1)};
    }
}