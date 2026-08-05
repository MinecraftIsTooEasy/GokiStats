package com.moddedmite.mitemod.goki_stats.common.stat.special.leaper;

import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import net.minecraft.EntityPlayer;
import net.minecraft.I18n;

public class StatStealth extends StatLeaper {
    public StatStealth(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.3416D));
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        return ((obj[0] instanceof EntityPlayer)) && (((EntityPlayer) obj[0]).isSneaking());
    }

    @Override
    public float getSecondaryBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.4307D));
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        // TODO special
        float speed = DataHelper.trimDecimals(getBonus(player), 1);
        float reapBonus = DataHelper.trimDecimals(getSecondaryBonus(player), 1);
        float reap = Stats.REAPER.getBonus(player) * 100.0F;
        float newReap = DataHelper.trimDecimals(reap + reap * reapBonus / 100.0F,
                1);
        return new float[]
                {speed, reapBonus, newReap};
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.getStringParams(this.key + ".des", new Object[]{
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1],
                this.getDescriptionFormatArguments(player)[2]});
    }
}