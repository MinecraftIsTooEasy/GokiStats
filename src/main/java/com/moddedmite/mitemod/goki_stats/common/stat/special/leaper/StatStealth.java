package com.moddedmite.mitemod.goki_stats.common.stat.special.leaper;

import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
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
        float speed = DataHelper.trimDecimals(getBonus(player), 1);
        float miningBonus = DataHelper.trimDecimals(getSecondaryBonus(player), 1);
        return new float[]
                {speed, miningBonus};
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        float[] args = getDescriptionFormatArguments(player);
        return I18n.getStringParams(this.key + ".des", new Object[]{
                args[0],
                args[1]});
    }
}