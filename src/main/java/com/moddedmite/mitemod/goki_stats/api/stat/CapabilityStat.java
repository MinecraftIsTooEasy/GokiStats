package com.moddedmite.mitemod.goki_stats.api.stat;

import com.moddedmite.mitemod.goki_stats.common.utils.Reference;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.EntityPlayer;
import net.minecraft.NBTTagCompound;

/**
 * MITE-adapted capability for stat storage.
 * Uses NBT-based persistent storage via DataHelper's WeakHashMap approach.
 */
public class CapabilityStat {
    /**
     * Get the StatStorage for a player, reading from the player's persistent NBT data.
     */
    public static StatStorage getStatStorage(EntityPlayer player) {
        StatStorage storage = new StatStorage();
        NBTTagCompound nbt = DataHelper.getPlayerPersistentNBT(player);
        if (nbt.hasKey(Reference.STAT_TAG)) {
            NBTTagCompound statTag = nbt.getCompoundTag(Reference.STAT_TAG);
            for (Object keyObj : statTag.getTags()) {
                String key = (String) keyObj;
                if (!StatBase.statKeyMap.containsKey(key)) continue;
                NBTTagCompound stateTag = statTag.getCompoundTag(key);
                StatBase statBase = StatBase.statKeyMap.get(key);
                storage.stateMap.put(statBase, new StatState(statBase,
                        stateTag.getInteger("level"),
                        stateTag.getInteger("revertedLevel")));
            }
        }
        return storage;
    }

    /**
     * Save the StatStorage to the player's persistent NBT data.
     */
    public static void saveStatStorage(EntityPlayer player, StatStorage storage) {
        NBTTagCompound nbt = DataHelper.getPlayerPersistentNBT(player);
        NBTTagCompound statTag = new NBTTagCompound();
        storage.stateMap.forEach((stat, state) -> {
            NBTTagCompound stateTag = new NBTTagCompound();
            stateTag.setInteger("level", state.level);
            stateTag.setInteger("revertedLevel", state.revertedLevel);
            statTag.setCompoundTag(stat.getKey(), stateTag);
        });
        nbt.setCompoundTag(Reference.STAT_TAG, statTag);
    }
}
