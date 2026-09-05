package com.moddedmite.mitemod.goki_stats.common.stat.tool;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.ItemTool;

public class StatSwordsmanship extends ToolSpecificStat {
    public StatSwordsmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Swordsmanship Tools";
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        if (super.isItemSupported(item)) return true;
        Item it = item.getItem();
        return it instanceof ItemTool;
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[0];
    }
}