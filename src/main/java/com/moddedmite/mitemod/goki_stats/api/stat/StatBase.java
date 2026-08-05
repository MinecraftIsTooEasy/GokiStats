package com.moddedmite.mitemod.goki_stats.api.stat;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import com.moddedmite.mitemod.goki_stats.common.config.Configurable;
import com.moddedmite.mitemod.goki_stats.common.config.GokiConfig;
import com.moddedmite.mitemod.goki_stats.common.config.stats.StatConfig;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.I18n;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class StatBase<T extends StatConfig> implements Stat, Configurable<T> {
    public static final Map<String, StatBase> statKeyMap = new HashMap<>(16);
    public static final ObjectList<StatBase> stats = new ObjectArrayList<>(16);
    public static int totalStats = 0;
    public int imageID;
    public String key;
    public float costMultiplier = 1.0F;
    public float limitMultiplier = 1.0F;
    public float bonusMultiplier = 1.0F;
    public boolean enabled = true;
    private int limit;

    public StatBase(int imgId, String key, int limit) {
        this.imageID = imgId;
        this.limit = limit;
        this.key = key;
        stats.add(this);
        totalStats++;
        statKeyMap.put(key, this);
    }

    protected static float getFinalBonus(float currentBonus) {
        return currentBonus * GokiConfig.globalModifiers.globalBonusMultiplier;
    }

    private static boolean isToolEffective(ItemStack stack, World world, Object pos) {
        if (pos == null) return false;
        return false; // BlockPos does not exist in MITE; position-based tool effectiveness check is not supported
    }

    public boolean isEffectiveOn(ItemStack stack) {
        return false;
    }

    @Override
    public T createConfig() {
        return (T) new StatConfig();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void save() {
        getConfig().bonusMultiplier = bonusMultiplier;
    }

    @Override
    public void reload() {
        bonusMultiplier = getConfig().bonusMultiplier;
    }

    @Override
    public float getBonus(EntityPlayer player) {
        return getBonus(DataHelper.getPlayerStatLevel(player, this)) * bonusMultiplier;
    }

    @Override
    public float[] getDescriptionFormatArguments(EntityPlayer player) {
        return new float[]
                {DataHelper.trimDecimals(getBonus(player) * 100, 1)};
    }

    @Override
    public int getCost(int level) {
        return (int) ((Math.pow(level, 1.6D) + 6.0D + level) * GokiConfig.globalModifiers.globalCostMultiplier);
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (((obj[1] instanceof ItemStack)) && ((obj[3] instanceof World))) {
            ItemStack stack = (ItemStack) obj[1];
            Object pos = obj[2];
            World world = (World) obj[3];

            return isToolEffective(stack, world, pos);
        }
        return false;
    }

    @Override
    public int getLimit() {
        if (GokiConfig.globalModifiers.globalLimitMultiplier <= 0.0F) {
            return 127;
        }
        return (int) (this.limit * GokiConfig.globalModifiers.globalLimitMultiplier);
    }

    @Override
    public float getAppliedBonus(EntityPlayer player, Object object) {
        if (isEffectiveOn(object))
            return getBonus(player);
        else
            return 0;
    }

    protected final int getPlayerStatLevel(EntityPlayer player) {
        return DataHelper.getPlayerStatLevel(player, this);
    }

    public final boolean isEffectiveOn(ItemStack stack, Object pos, World world) {
        if (isToolEffective(stack, world, pos))
            return true;
        else return isEffectiveOn(stack);
    }

    @Environment(EnvType.CLIENT)
    public String getLocalizedName() {
        return I18n.getString(this.key + ".name");
    }

    @Environment(EnvType.CLIENT)
    public String getLocalizedDescription(EntityPlayer player) {
        return I18n.getStringParams(this.key + ".des", new Object[]{
                this.getDescriptionFormatArguments(player)[0]});
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatBase)) return false;
        StatBase<?> statBase = (StatBase<?>) o;
        return Objects.equals(getKey(), statBase.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return getKey();
    }
}