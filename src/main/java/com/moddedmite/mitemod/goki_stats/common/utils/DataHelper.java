package com.moddedmite.mitemod.goki_stats.common.utils;

import com.google.common.collect.Lists;
import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import com.moddedmite.mitemod.goki_stats.common.config.GokiConfig;
import net.minecraft.Entity;
import net.minecraft.EntityLivingBase;
import net.minecraft.SharedMonsterAttributes;
import net.minecraft.AttributeModifier;
import net.minecraft.AttributeInstance;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.minecraft.Potion;
import net.minecraft.PotionEffect;
import net.minecraft.DamageSource;

import java.util.Collection;
import java.util.WeakHashMap;
import java.util.function.IntFunction;

public class DataHelper {
    private static final WeakHashMap<EntityPlayer, NBTTagCompound> playerStatNBT = new WeakHashMap<>();
    private static final String STAT_TAG = "gokistats_Stats";

    public static NBTTagCompound getPlayerPersistentNBT(EntityPlayer player) {
        return playerStatNBT.computeIfAbsent(player, k -> new NBTTagCompound());
    }

    public static void saveToNBT(EntityPlayer player, NBTTagCompound compound) {
        NBTTagCompound statsNBT = playerStatNBT.get(player);
        if (statsNBT != null) {
            compound.setCompoundTag(STAT_TAG, statsNBT);
        }
    }

    public static void loadFromNBT(EntityPlayer player, NBTTagCompound compound) {
        if (compound.hasKey(STAT_TAG)) {
            playerStatNBT.put(player, compound.getCompoundTag(STAT_TAG));
        } else {
            playerStatNBT.put(player, new NBTTagCompound());
        }
        resetMaxHealth(player);
    }

    public static boolean canPlayerRevertStat(EntityPlayer player, StatBase stat) {
        return GokiConfig.globalModifiers.globalMaxRevertLevel == -1 ||
                (GokiConfig.globalModifiers.globalMaxRevertLevel >= 0
                        && getPlayerRevertStatLevel(player, stat) < GokiConfig.globalModifiers.globalMaxRevertLevel
                        && getPlayerStatLevel(player, stat) > 0);
    }

    public static int getPlayerRevertStatLevel(EntityPlayer player, StatBase stat) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        return nbt.getInteger(stat.getKey() + ".revert");
    }

    public static int setPlayerRevertStatLevel(EntityPlayer player, StatBase stat, int level) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        nbt.setInteger(stat.getKey() + ".revert", level);
        if (stat == Stats.MAX_HEALTH) {
            DataHelper.resetMaxHealth(player);
        }
        return 0;
    }

    public static int getPlayerStatLevel(EntityPlayer player, StatBase stat) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        return nbt.getInteger(stat.getKey());
    }

    public static void setPlayerStatLevel(EntityPlayer player, StatBase stat, int level) {
        NBTTagCompound nbt = getPlayerPersistentNBT(player);
        nbt.setInteger(stat.getKey(), level);
        if (stat == Stats.MAX_HEALTH) {
            DataHelper.resetMaxHealth(player);
        }
    }

    public static void multiplyPlayerStatLevel(EntityPlayer player, StatBase stat, IntFunction<Integer> multiplier) {
        setPlayerStatLevel(player, stat, multiplier.apply(getPlayerStatLevel(player, stat)));
    }

    public static float trimDecimals(float in, int decimals) {
        float f = (float) (in * Math.pow(10.0D, decimals));
        int i = (int) f;
        return i / (float) Math.pow(10.0D, decimals);
    }

    public static void addMaxHealth(EntityPlayer player, int amount) {
        // 不修改 SharedMonsterAttributes.maxHealth
        // MITE 使用 getHealthLimit() 计算最大生命值，不需要修改此属性
        // 保留此方法以兼容现有调用，但不再修改属性值
        // 这样 Bread-Skin 等模组读取 SharedMonsterAttributes.maxHealth 时得到默认值 20
        // 饱和度显示位置不会因生命值增加而偏移
    }

    public static void resetMaxHealth(EntityPlayer player) {
        addMaxHealth(player, DataHelper.getPlayerStatLevel(player, Stats.MAX_HEALTH));
    }

    public static void setPlayersExpTo(EntityPlayer player, int total) {
        player.experience = 0;
        player.addExperience(total);
    }

    public static int getXPTotal(int xpLevel, float current) {
        return (int) (getXPValueFromLevel(xpLevel) + getXPValueToNextLevel(xpLevel) * current);
    }

    public static int getXPTotal(EntityPlayer player) {
        return player.experience;
    }

    public static int getXPValueFromLevel(int xpLevel) {
        int val;
        if (xpLevel > 31) {
            val = (int) (4.5d * Math.pow(xpLevel, 2d) - 162.5d * xpLevel + 2220d);
        } else if (xpLevel > 16) {
            val = (int) (2.5d * Math.pow(xpLevel, 2d) - 40.5d * xpLevel + 360d);
        } else {
            val = (int) (Math.pow(xpLevel, 2d) + 6d * xpLevel);
        }
        return val;
    }

    public static int getXPValueToNextLevel(int xpLevel) {
        int val;
        if (xpLevel > 30) {
            val = 9 * xpLevel - 158;
        } else if (xpLevel > 15) {
            val = 5 * xpLevel - 38;
        } else {
            val = 2 * xpLevel + 7;
        }

        return val;
    }

    public static float getDamageDealt(EntityPlayer player, Entity target, DamageSource source) {
        float damage = (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        boolean targetIsLiving = target instanceof EntityLivingBase;
        boolean critical;
        ItemStack stack = player.getHeldItemStack();
        if ((damage > 0.0F)) {
            critical = (player.fallDistance > 0.0F) && (!player.onGround) && (!player.isOnLadder()) && (!player.isInWater()) && (!player.isPotionActive(Potion.blindness.id)) && (player.ridingEntity == null) && (targetIsLiving);
            if ((critical) && (damage > 0.0F)) {
                damage *= 1.5F;
            }
        }
        return damage;
    }

    public static float getFallResistance(EntityLivingBase entity) {
        float resistance = 3.0F;
        PotionEffect potioneffect = entity.getActivePotionEffect(Potion.jump.id);
        float bonus = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0.0F;

        return resistance + bonus;
    }
}
