package com.moddedmite.mitemod.goki_stats.api.stat;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;

public class StatStorage {
    public Map<Stat, StatState> stateMap = new Object2ObjectOpenHashMap<>();
}