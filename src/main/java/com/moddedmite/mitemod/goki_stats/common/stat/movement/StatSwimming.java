package com.moddedmite.mitemod.goki_stats.common.stat.movement;

import com.moddedmite.mitemod.goki_stats.common.config.stats.StatConfig;
import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import net.minecraft.EntityPlayer;
import net.minecraft.I18n;

public class StatSwimming extends StatBase<StatConfig> {
    public StatSwimming(int id, String key, int limit) {
        super(id, key, limit);
    }

    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.029F);
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.getStringParams(this.key + ".des0", new Object[]{
                this.getDescriptionFormatArguments(player)[0]});
    }
}