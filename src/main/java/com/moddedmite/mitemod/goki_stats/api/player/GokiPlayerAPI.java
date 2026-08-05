package com.moddedmite.mitemod.goki_stats.api.player;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.api.stat.StatState;
import com.moddedmite.mitemod.goki_stats.api.stat.StatStorage;
import com.moddedmite.mitemod.goki_stats.api.stat.CapabilityStat;
import net.minecraft.EntityPlayer;

/**
 * Simple player data API for GokiStats in MITE.
 * Provides access to stat levels and stat storage for a player using NBT-based persistence.
 */
public class GokiPlayerAPI {

    /**
     * Get the stat level for a player.
     */
    public static int getStatLevel(EntityPlayer player, StatBase stat) {
        StatStorage storage = CapabilityStat.getStatStorage(player);
        StatState state = storage.stateMap.get(stat);
        return state != null ? state.level : 0;
    }

    /**
     * Set the stat level for a player.
     */
    public static void setStatLevel(EntityPlayer player, StatBase stat, int level) {
        StatStorage storage = CapabilityStat.getStatStorage(player);
        StatState state = storage.stateMap.get(stat);
        if (state != null) {
            state.level = level;
        } else {
            storage.stateMap.put(stat, new StatState(stat, level));
        }
        CapabilityStat.saveStatStorage(player, storage);
    }

    /**
     * Get the reverted stat level for a player.
     */
    public static int getRevertedStatLevel(EntityPlayer player, StatBase stat) {
        StatStorage storage = CapabilityStat.getStatStorage(player);
        StatState state = storage.stateMap.get(stat);
        return state != null ? state.revertedLevel : 0;
    }

    /**
     * Set the reverted stat level for a player.
     */
    public static void setRevertedStatLevel(EntityPlayer player, StatBase stat, int level) {
        StatStorage storage = CapabilityStat.getStatStorage(player);
        StatState state = storage.stateMap.get(stat);
        if (state != null) {
            state.revertedLevel = level;
        } else {
            storage.stateMap.put(stat, new StatState(stat, 0, level));
        }
        CapabilityStat.saveStatStorage(player, storage);
    }

    /**
     * Get the full StatStorage for a player.
     */
    public static StatStorage getStorage(EntityPlayer player) {
        return CapabilityStat.getStatStorage(player);
    }

    /**
     * Save the StatStorage for a player.
     */
    public static void saveStorage(EntityPlayer player, StatStorage storage) {
        CapabilityStat.saveStatStorage(player, storage);
    }
}