package com.moddedmite.mitemod.goki_stats.common.stat.tool;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.ItemAxe;
import net.minecraft.ItemBattleAxe;

public class StatChopping extends ToolSpecificStat {
    public StatChopping(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public String getConfigurationKey() {
        return "Chopping Tools";
    }

    @Override
    public float getBonus(int level) {
        return StatBase.getFinalBonus((float) Math.pow(level, 1.3D) * 0.01523F);
    }

    @Override
    public String[] getDefaultSupportedItems() {
        return new String[0];
    }

    @Override
    public boolean isItemSupported(ItemStack item) {
        if (super.isItemSupported(item)) return true;
        Item it = item.getItem();
        return it instanceof ItemAxe || it instanceof ItemBattleAxe;
    }
}
