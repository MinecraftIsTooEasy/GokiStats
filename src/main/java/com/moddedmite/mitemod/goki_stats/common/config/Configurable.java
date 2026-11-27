package com.moddedmite.mitemod.goki_stats.common.config;

import java.lang.reflect.Type;

public interface Configurable<T> {
    T createConfig();

    default void saveConfig() {
        save();
        ConfigManager.INSTANCE.getConfigMap().put(getKey(), getConfig());
        ConfigManager.INSTANCE.saveConfig(getKey());
    }

    default Type getType() {
        return createConfig().getClass();
    }

    default void reloadConfig() {
        ConfigManager.INSTANCE.registerConfig(getKey(), getType());
        // 先读取磁盘上已存在的配置, 只有文件不存在时才生成默认值,
        // 否则每次启动都会用默认值覆盖用户修改过的配置文件。
        if (ConfigManager.INSTANCE.hasConfig(getKey())
                || ConfigManager.INSTANCE.configFileExists(getKey())) {
            ConfigManager.INSTANCE.reloadConfig(getKey());
        }
        if (!ConfigManager.INSTANCE.hasConfig(getKey())) {
            ConfigManager.INSTANCE.createConfig(getKey(), createConfig());
        }
        reload();
    }

    void save();

    void reload();

    String getKey();

    default T getConfig() {
        return ConfigManager.INSTANCE.getOrCreateConfig(getKey(), createConfig());
    }
}