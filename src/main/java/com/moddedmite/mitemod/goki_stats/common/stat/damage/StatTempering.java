package com.moddedmite.mitemod.goki_stats.common.stat.damage;

import net.minecraft.DamageSource;

public class StatTempering extends DamageSourceProtectionStat {
    public StatTempering(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public float getBonus(int level) {
        return getFinalBonus(level * 0.026F);
    }

    @Override
    public String[] getDefaultDamageSources() {
        return new String[]
                {"lava", "inFire", "onFire"};
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj != null && obj[0] instanceof DamageSource) {
            DamageSource source = (DamageSource) obj[0];
            return source.isFireDamage() || source.isLavaDamage();
        }
        return false;
    }
}