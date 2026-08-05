package com.moddedmite.mitemod.goki_stats.common.stat.damage;

import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.EntityPlayer;
import net.minecraft.I18n;

public class StatFeatherFall extends DamageSourceProtectionStat {
    public StatFeatherFall(int id, String key, int limit) {
        super(id, key, limit);
    }

    public float getSecondaryBonus(int level) {
        return getFinalBonus(level * 0.1F);
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        float height = DataHelper.getFallResistance(player) + DataHelper.trimDecimals(getSecondaryBonus(getPlayerStatLevel(player)),
                1);
        return new float[]
                {DataHelper.trimDecimals(getBonus(getPlayerStatLevel(player)) * 100, 1), height};
    }

    @Override
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.getStringParams(this.key + ".des", new Object[]{
                this.getDescriptionFormatArguments(player)[0],
                this.getDescriptionFormatArguments(player)[1]});
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"fall"};
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.026F);
    }

}