package com.moddedmite.mitemod.goki_stats.api.stat;

import net.minecraft.EntityPlayer;

/**
 * Special stat with a secondary bonus slot
 */
public interface StatSpecial extends Stat {
    float getSecondaryBonus(EntityPlayer player);

    float getSecondaryBonus(int level);
}