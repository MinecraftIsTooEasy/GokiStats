package com.moddedmite.mitemod.goki_stats.common;

import com.google.common.collect.Lists;
import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.common.config.ConfigManager;
import com.moddedmite.mitemod.goki_stats.common.config.Configurable;
import com.moddedmite.mitemod.goki_stats.common.network.GokiNetwork;
import com.moddedmite.mitemod.goki_stats.common.network.S2COpenGui;
import moddedmite.rustedironcore.api.event.Handlers;
import net.minecraft.CommandBase;
import net.minecraft.ICommandSender;
import net.minecraft.EntityPlayer;
import net.minecraft.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.ChatMessageComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatsCommand extends CommandBase {
    @Nonnull
    @Override
    public String getCommandName() {
        return "gokistats";
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/gokistats";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        MinecraftServer server = MinecraftServer.getServer();
        if (args.length < 1) {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("Invalid usage! Valid commands: /gokistats reload, /gokistats gui"));
            return;
        }

        if (args[0].equals("reload")) {
            StatBase.stats.forEach(Configurable::reloadConfig);
            ConfigManager.INSTANCE.reloadConfig("gokistats");
            com.moddedmite.mitemod.goki_stats.common.config.GokiStatsConfig.getInstance().load();
            EntityPlayer player;
            if ((sender instanceof EntityPlayer)) {
                player = (EntityPlayer) sender;
                player.sendChatToPlayer(ChatMessageComponent.createFromText("Reloaded gokistats configuration file."));
            } else {
                server.logInfo("Reloaded gokistats configuration file.");
            }
        } else if (args[0].equals("gui")) {
            ServerPlayer player = null;
            if (args.length == 1) {
                if ((sender instanceof ServerPlayer)) {
                    player = (ServerPlayer) sender;
                } else {
                    sender.sendChatToPlayer(ChatMessageComponent.createFromText("This command should be only used by player"));
                    return;
                }
            } else if (args.length == 2) {
                player = server.getConfigurationManager().getPlayerForUsername(args[1]);
            }
            if (player == null) {
                sender.sendChatToPlayer(ChatMessageComponent.createFromText("Player not found: " + (args.length == 2 ? args[1] : "")));
                return;
            }
            GokiNetwork.sendToClient(player, new S2COpenGui(0));
        } else {
            sender.sendChatToPlayer(ChatMessageComponent.createFromText("No sub-command " + args[0]));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        MinecraftServer server = MinecraftServer.getServer();
        if (args.length == 1) {
            String s = args[args.length - 1];
            if (args[0].isEmpty()) return Arrays.asList("reload", "gui");
            if (doesStringStartWith(s, "reload"))
                return Collections.singletonList("reload");
            else if (doesStringStartWith(s, "gui"))
                return Collections.singletonList("gui");
        } else if (args.length == 2) {
            if (args[0].equals("gui")) {
                List<String> list = Lists.newArrayList();

                for (String username : server.getConfigurationManager().getAllUsernames())
                    if (!server.getConfigurationManager().isPlayerOpped(username) && doesStringStartWith(args[1], username))
                        list.add(username);

                return list;
            }
        }
        return Arrays.asList("reload", "gui");
    }

    public static void register() {
        Handlers.Command.register(event -> event.register(new StatsCommand()));
    }
}