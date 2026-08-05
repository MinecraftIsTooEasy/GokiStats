package com.moddedmite.mitemod.goki_stats.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public static final ConfigManager INSTANCE = new ConfigManager();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Object> configMap = new HashMap<>();
    private final Map<String, Type> typeMap = new HashMap<>();
    private File configDir;

    public void init(File configDir) {
        this.configDir = configDir;
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
    }

    public Map<String, Object> getConfigMap() {
        return configMap;
    }

    public void registerConfig(String key, Type type) {
        typeMap.put(key, type);
    }

    public boolean hasConfig(String key) {
        return configMap.containsKey(key);
    }

    public <T> T getOrCreateConfig(String key, T defaultConfig) {
        if (configMap.containsKey(key)) {
            return (T) configMap.get(key);
        }
        createConfig(key, defaultConfig);
        return (T) configMap.get(key);
    }

    public <T> void createConfig(String key, T config) {
        configMap.put(key, config);
        saveConfig(key);
    }

    public void saveConfig(String key) {
        if (configDir == null) return;
        Object config = configMap.get(key);
        if (config == null) return;

        File file = new File(configDir, key + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig(String key) {
        if (configDir == null) return;
        Type type = typeMap.get(key);
        if (type == null) return;

        File file = new File(configDir, key + ".json");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Object config = gson.fromJson(reader, type);
                if (config != null) {
                    configMap.put(key, config);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadAllConfigs() {
        // Load all configs from the directory
    }
}