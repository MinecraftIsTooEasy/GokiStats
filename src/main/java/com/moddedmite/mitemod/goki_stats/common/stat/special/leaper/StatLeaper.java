package com.moddedmite.mitemod.goki_stats.common.stat.special.leaper;

import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import com.moddedmite.mitemod.goki_stats.api.stat.StatSpecial;
import com.moddedmite.mitemod.goki_stats.api.stat.StatSpecialBase;
import net.minecraft.EntityPlayer;
import net.minecraft.I18n;

public abstract class StatLeaper extends StatSpecialBase implements StatSpecial {
    public StatLeaper(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.065D) * 0.0195F);
    }

    @Override
    public float getSecondaryBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.1D) * 0.0203F);
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        // TODO speical
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), DataHelper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)) * 100,
                        1)};
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.getStringParams(this.key + ".des", new Object[]{
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]});
    }
}