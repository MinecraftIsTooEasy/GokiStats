package com.moddedmite.mitemod.goki_stats.common.stat.tool;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.ItemBow;

public class StatBowmanship extends ToolSpecificStat {
    public StatBowmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Bowmanship Tools";
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[0];
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        if (super.isItemSupported(item)) return true;
        return item.getItem() instanceof ItemBow;
    }
}
