package com.moddedmite.mitemod.goki_stats.common.config;

public class GokiConfig {
    public static String version = "v3";

    public static boolean keyBindingEnabled = true;

    public static boolean initiativeSync = false;

    public static int syncTicks = 400;

    public static GlobalModifiers globalModifiers = new GlobalModifiers();

    public static Support support = new Support();

    public static class Support {
        public float reaperLimit = 20;
    }

    public static class GlobalModifiers {
        public float globalCostMultiplier = 1;

        public float globalLimitMultiplier = 2.5f;

        public float globalBonusMultiplier = 1;

        public boolean loseStatsOnDeath = false;

        public float loseStatsMultiplier = 1;

        public int globalMaxRevertLevel = -1;

        public float globalRevertFactor = 0.8F;
    }
}