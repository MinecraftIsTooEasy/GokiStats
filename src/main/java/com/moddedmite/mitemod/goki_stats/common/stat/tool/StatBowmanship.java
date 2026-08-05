package com.moddedmite.mitemod.goki_stats.common.stat.tool;

import net.minecraft.Item;

public class StatBowmanship extends ToolSpecificStat {
    public StatBowmanship(int id, String key, int limit) {
        super(id, key, limit);
    }

    public String getConfigurationKey() {
        return "Bowmanship Tools";
    }

    public float getBonus(int level) {
        return getFinalBonus((float) Math.pow(level, 1.0895D) * 0.03F);
    }

    public String[] getDefaultSupportedItems() {
        return new String[]
                {Item.bow.itemID + ":0"};
    }
}