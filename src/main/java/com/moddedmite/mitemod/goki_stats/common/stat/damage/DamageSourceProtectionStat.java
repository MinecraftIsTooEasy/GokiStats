package com.moddedmite.mitemod.goki_stats.common.stat.damage;

import com.moddedmite.mitemod.goki_stats.common.config.stats.DamageSourceProtectionConfig;
import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import net.minecraft.DamageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DamageSourceProtectionStat extends StatBase<DamageSourceProtectionConfig> {
    public List<String> damageSources = new ArrayList<>();

    public DamageSourceProtectionStat(int id, String key, int limit) {
        super(id, key, limit);
    }

    @Override
    public DamageSourceProtectionConfig createConfig() {
        DamageSourceProtectionConfig config = new DamageSourceProtectionConfig();
        Collections.addAll(config.damageSources, getDefaultDamageSources());
        return config;
    }

    @Override
    public void save() {
        super.save();
        getConfig().damageSources.clear();
        getConfig().damageSources.addAll(damageSources);
    }

    @Override
    public void reload() {
        super.reload();
        damageSources.clear();
        damageSources.addAll(getConfig().damageSources);
    }

    @Override
    public boolean isEffectiveOn(Object... obj) {
        if (obj != null) {
            if ((obj[0] instanceof DamageSource)) {
                DamageSource source = (DamageSource) obj[0];
                for (String damageSource : this.damageSources) {
                    if (source.damageType.equals(damageSource)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public abstract String[] getDefaultDamageSources();
}