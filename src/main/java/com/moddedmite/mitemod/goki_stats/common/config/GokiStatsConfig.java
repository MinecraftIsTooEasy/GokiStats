package com.moddedmite.mitemod.goki_stats.common.config;

import fi.dy.masa.malilib.config.SimpleConfigs;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;

import java.util.ArrayList;
import java.util.List;

public class GokiStatsConfig extends SimpleConfigs {
    private static GokiStatsConfig instance;

    public static final ConfigBoolean keyBindingEnabled = new ConfigBoolean("keyBindingEnabled", true, "Enable keybindings");
    public static final ConfigBoolean initiativeSync = new ConfigBoolean("initiativeSync", false, "Sync stats on join");
    public static final ConfigInteger syncTicks = new ConfigInteger("syncTicks", 400, 20, Integer.MAX_VALUE, false, "Sync interval in ticks (min 20)");

    public static final ConfigDouble globalCostMultiplier = new ConfigDouble("globalCostMultiplier", 1.0, 0.0, 100.0, false, "Global cost multiplier");
    public static final ConfigDouble globalLimitMultiplier = new ConfigDouble("globalLimitMultiplier", 2.5, 0.0, 100.0, false, "Global limit multiplier");
    public static final ConfigDouble globalBonusMultiplier = new ConfigDouble("globalBonusMultiplier", 1.0, 0.0, 100.0, false, "Global bonus multiplier");
    public static final ConfigBoolean loseStatsOnDeath = new ConfigBoolean("loseStatsOnDeath", false, "Lose stats on death");
    public static final ConfigDouble loseStatsMultiplier = new ConfigDouble("loseStatsMultiplier", 1.0, 0.0, 1.0, false, "Stats loss multiplier on death");
    public static final ConfigInteger globalMaxRevertLevel = new ConfigInteger("globalMaxRevertLevel", -1, -1, Integer.MAX_VALUE, false, "Max revert level (-1 = unlimited)");
    public static final ConfigDouble globalRevertFactor = new ConfigDouble("globalRevertFactor", 0.8, 0.0, 1.0, false, "Revert factor");

    public static final ConfigDouble reaperLimit = new ConfigDouble("reaperLimit", 20.0, 0.0, 1000.0, false, "Reaper limit");

    private static final List<ConfigBase<?>> values;

    static {
        values = new ArrayList<>();
        values.add(keyBindingEnabled);
        values.add(initiativeSync);
        values.add(syncTicks);
        values.add(globalCostMultiplier);
        values.add(globalLimitMultiplier);
        values.add(globalBonusMultiplier);
        values.add(loseStatsOnDeath);
        values.add(loseStatsMultiplier);
        values.add(globalMaxRevertLevel);
        values.add(globalRevertFactor);
        values.add(reaperLimit);
    }

    public GokiStatsConfig() {
        super("GokiStats", null, values, "GokiStats configuration");
    }

    public static GokiStatsConfig getInstance() {
        if (instance == null) {
            instance = new GokiStatsConfig();
        }
        return instance;
    }

    @Override
    public void load() {
        super.load();
        syncToStaticFields();
    }

    public void syncToStaticFields() {
        GokiConfig.keyBindingEnabled = keyBindingEnabled.getBooleanValue();
        GokiConfig.initiativeSync = initiativeSync.getBooleanValue();
        GokiConfig.syncTicks = syncTicks.getIntegerValue();
        GokiConfig.globalModifiers.globalCostMultiplier = (float) globalCostMultiplier.getDoubleValue();
        GokiConfig.globalModifiers.globalLimitMultiplier = (float) globalLimitMultiplier.getDoubleValue();
        GokiConfig.globalModifiers.globalBonusMultiplier = (float) globalBonusMultiplier.getDoubleValue();
        GokiConfig.globalModifiers.loseStatsOnDeath = loseStatsOnDeath.getBooleanValue();
        GokiConfig.globalModifiers.loseStatsMultiplier = (float) loseStatsMultiplier.getDoubleValue();
        GokiConfig.globalModifiers.globalMaxRevertLevel = globalMaxRevertLevel.getIntegerValue();
        GokiConfig.globalModifiers.globalRevertFactor = (float) globalRevertFactor.getDoubleValue();
        GokiConfig.support.reaperLimit = (float) reaperLimit.getDoubleValue();
    }
}
