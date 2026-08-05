package com.moddedmite.mitemod.goki_stats.common.stat;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.common.config.GokiConfig;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.I18n;
import net.minecraft.EntityLivingBase;
import net.minecraft.EntityPlayer;

public class StatReaper extends StatBase {
    public StatReaper(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0768D) * 0.0025F);
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), GokiConfig.support.reaperLimit};
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.getStringParams(this.key + ".des", new Object[]{
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]});
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj[0] != null) {
            if (!(obj[0] instanceof EntityPlayer)) {
                if ((obj[0] instanceof EntityLivingBase)) {
                    EntityLivingBase target = (EntityLivingBase) obj[0];

                    return target.getMaxHealth() <= GokiConfig.support.reaperLimit;
                }
            }
        }
        return false;
    }
}