package com.moddedmite.mitemod.goki_stats.common.stat.damage;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;

public class StatPugilism extends StatBase {
    public StatPugilism(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return level == 0 ? 0 : getFinalBonus((float) Math.pow(level, 1.03D) * 0.1816F);
    }
}