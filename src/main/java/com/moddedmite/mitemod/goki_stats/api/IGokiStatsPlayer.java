package com.moddedmite.mitemod.goki_stats.api;

import net.minecraft.NBTTagCompound;

/**
 * 接口注入到 EntityPlayer，直接在实体上存储技能 NBT 数据，
 * 替代 WeakHashMap 缓存，避免 GC 或实体重建导致的数据丢失。
 */
public interface IGokiStatsPlayer {
    NBTTagCompound gokiStats$getStatsNBT();
}
