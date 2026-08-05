package com.moddedmite.mitemod.goki_stats.common.stat.tool;

public class ItemIdMetadataTuple {
    public String id = "";
    public int metadata = 0;

    // Gson 需要无参构造函数
    public ItemIdMetadataTuple() {}

    public ItemIdMetadataTuple(String id, int meta) {
        this.id = id;
        this.metadata = meta;
    }

    public ItemIdMetadataTuple(String configString) {
        fromConfigString(configString);
    }

    public void fromConfigString(String configString) {
        String[] configStringSplit = configString.split(":");
        try {
            this.id = configStringSplit[0];
            this.metadata = Integer.parseInt(configStringSplit[1]);
        } catch (Exception ignored) {
        }
    }
}