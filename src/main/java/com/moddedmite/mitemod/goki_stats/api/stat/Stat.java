package com.moddedmite.mitemod.goki_stats.api.stat;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.World;

public interface Stat {
    /**
     * Used to be an identifier and translation key
     * @deprecated change to ResourceLocation in the future
     * @return unique key of this stat
     */
    @Deprecated
    String getKey();

    /**
     * Return if this stat is effective on the specified objects
     * @param obj in-world objects, like ItemStack, Entity, etc
     * @return if objects meets the stat requirements
     */
    boolean isEffectiveOn(Object... obj);

    boolean isEffectiveOn(ItemStack stack, Object pos, World world);

    /**
     * Get arguments to format the description
     * @param player player instance
     * @return format arguments, most commonly the bonus and the limit of the stat
     */
    float[] getDescriptionFormatArguments(EntityPlayer player);

    /**
     * Bonus to be used for this stat
     * @param level stat level
     * @return bonus
     */
    float getBonus(int level);

    /**
     * Get final bonus for a player to process the stat modifier
     * Game mechanic handler calls this
     * @param player player instance
     * @return final bonus
     */
    float getBonus(EntityPlayer player);

    /**
     * Get final bonus applied on a game object
     * @param player player instance
     * @param paramObject game object such as ItemStack or Entity
     * @return final bonus
     */
    float getAppliedBonus(EntityPlayer player, Object paramObject);

    /**
     * XP Cost for each level
     * @param level level
     * @return cost
     */
    int getCost(int level);

    /**
     * Stat limit
     * @return limit
     */
    int getLimit();
}