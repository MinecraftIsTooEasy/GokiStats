package com.moddedmite.mitemod.goki_stats;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import com.moddedmite.mitemod.goki_stats.client.GokiKeyHandler;
import com.moddedmite.mitemod.goki_stats.common.StatsCommand;
import com.moddedmite.mitemod.goki_stats.common.config.Configurable;
import com.moddedmite.mitemod.goki_stats.common.config.GokiStatsConfig;
import com.moddedmite.mitemod.goki_stats.common.handlers.GokiHandlers;
import com.moddedmite.mitemod.goki_stats.common.init.GokiSounds;
import com.moddedmite.mitemod.goki_stats.common.network.GokiNetwork;
import fi.dy.masa.malilib.config.ConfigManager;
import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.util.FabricUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.ModResourceManager;

import java.io.File;

// GokiStats 模组主类
// 负责初始化技能、注册网络包、事件处理器、按键绑定和命令
public class GokiStats implements ModInitializer {
    public static final String MOD_ID = "goki_stats";

    @Override
    public void onInitialize() {
        ModResourceManager.addResourcePackDomain(MOD_ID);

        // 触发Stats接口的静态初始化（注册所有技能）
        // 注意: Stats.class.getName() 只触发类加载,不触发初始化!
        try {
            Class.forName("com.moddedmite.mitemod.goki_stats.api.stat.Stats");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 初始化技能配置目录
        com.moddedmite.mitemod.goki_stats.common.config.ConfigManager.INSTANCE.init(
                new File(FabricUtil.getConfigDirectory().toFile(), "gokistats"));

        // 加载所有技能配置（填充 supports/damageSources 列表等）
        StatBase.stats.forEach(Configurable::reloadConfig);

        // 注册网络包
        GokiNetwork.registerPackets();

        // 注册声音
        GokiSounds.register();

        // 注册事件处理器
        GokiHandlers.register();

        // 注册 ManyLib 配置
        ConfigManager.getInstance().registerConfig(GokiStatsConfig.getInstance());

        // 注册命令
        Handlers.Command.register(event -> event.register(new StatsCommand()));

        // 注册按键绑定（仅客户端）
        registerKeybinding();
    }

    @Environment(EnvType.CLIENT)
    private void registerKeybinding() {
        GokiKeyHandler.register();
    }
}