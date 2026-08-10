package com.moddedmite.mitemod.goki_stats.common.init;

import com.google.common.eventbus.Subscribe;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.reload.event.MITEEvents;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

public class GokiSounds {
    public static final ResourceLocation REAPER = new ResourceLocation("goki_stats", "reaper");

    public static void register() {
        MITEEvents.MITE_EVENT_BUS.register(new GokiSounds());
    }

    @Subscribe
    public void onSoundsRegister(SoundsRegisterEvent event) {
        event.registerSound(REAPER, 3);
    }
}
