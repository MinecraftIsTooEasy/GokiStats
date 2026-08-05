package com.moddedmite.mitemod.goki_stats.client;

import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.listener.IKeybindingListener;
import moddedmite.rustedironcore.api.event.listener.ITickListener;
import moddedmite.rustedironcore.keybinding.KeyBindingExtra;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Minecraft;
import net.minecraft.KeyBinding;
import net.minecraft.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class GokiKeyHandler {
    public static KeyBindingExtra statsMenu;

    public static void register() {
        Handlers.Keybinding.register(new IKeybindingListener() {
            @Override
            public void onKeybindingRegister(Consumer<KeyBinding> registry) {
                statsMenu = new KeyBindingExtra("key.categories.goki_stats:ui.opmenu.name", 21, "Goki Stats");
                registry.accept(statsMenu);
            }
        });

        Handlers.Tick.register(new ITickListener() {
            @Override
            public void onEntityPlayerTick(EntityPlayer player) {}

            @Override
            public void onClientTick(Minecraft client) {
                onKeyInput();
            }

            @Override
            public void onServerTick(MinecraftServer server) {}
        });
    }

    public static void onKeyInput() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player == null) return;
        if (statsMenu != null && statsMenu.isPressed()) {
            mc.displayGuiScreen(null);
            com.moddedmite.mitemod.goki_stats.client.gui.GuiStats guiStats = new com.moddedmite.mitemod.goki_stats.client.gui.GuiStats(player);
            mc.displayGuiScreen(guiStats);
        }
    }
}