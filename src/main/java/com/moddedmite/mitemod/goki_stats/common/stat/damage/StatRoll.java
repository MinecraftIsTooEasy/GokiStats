package com.moddedmite.mitemod.goki_stats.common.stat.damage;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;

public class StatRoll extends StatBase {
    public StatRoll(int imgId, String key, int limit) {
        super(imgId, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.025f);
    }
}