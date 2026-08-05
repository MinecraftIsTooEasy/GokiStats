package com.moddedmite.mitemod.goki_stats.common.stat.tool;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.ItemPickaxe;
import net.minecraft.ItemWarHammer;

public class StatMining extends ToolSpecificStat {
    public StatMining(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Mining Tools";
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[0];
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        if (super.isItemSupported(item)) return true;
        Item it = item.getItem();
        return it instanceof ItemPickaxe || it instanceof ItemWarHammer;
    }
}
