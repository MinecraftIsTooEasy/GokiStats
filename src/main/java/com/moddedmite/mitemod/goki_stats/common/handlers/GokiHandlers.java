package com.moddedmite.mitemod.goki_stats.common.handlers;

import com.moddedmite.mitemod.goki_stats.api.stat.StatBase;
import com.moddedmite.mitemod.goki_stats.api.stat.StatSpecial;
import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import com.moddedmite.mitemod.goki_stats.common.config.GokiConfig;
import com.moddedmite.mitemod.goki_stats.common.init.GokiSounds;
import com.moddedmite.mitemod.goki_stats.common.network.GokiNetwork;
import com.moddedmite.mitemod.goki_stats.common.network.S2CSyncAll;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.events.PlayerCrossDimensionEvent;
import moddedmite.rustedironcore.api.event.events.PlayerRespawnEvent;
import moddedmite.rustedironcore.api.event.listener.ICombatListener;
import moddedmite.rustedironcore.api.event.listener.IEntityEventListener;
import moddedmite.rustedironcore.api.event.listener.IPlayerEventListener;
import moddedmite.rustedironcore.api.event.listener.ITickListener;
import net.minecraft.Block;
import net.minecraft.Minecraft;
import net.minecraft.Entity;
import net.minecraft.EntityLivingBase;
import net.minecraft.SharedMonsterAttributes;
import net.minecraft.AttributeModifier;
import net.minecraft.AttributeInstance;
import net.minecraft.EntityMob;
import net.minecraft.EntityPlayer;
import net.minecraft.Potion;
import net.minecraft.ItemBlock;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.ServerPlayer;
import net.minecraft.DamageSource;
import net.minecraft.ChatMessageComponent;
import net.minecraft.World;
import net.minecraft.Damage;
import net.minecraft.BlockInfo;
import net.minecraft.EnumEntityReachContext;

import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GokiHandlers {
    public static final UUID knockbackResistanceID = UUID.randomUUID();
    public static final UUID stealthSpeedID = UUID.randomUUID();
    public static final UUID swimSpeedID = UUID.randomUUID();

    public static AtomicInteger tickTimer = new AtomicInteger();

    private static final WeakHashMap<Entity, Boolean> knockbackFlag = new WeakHashMap<>();

    public static void register() {
        Handlers.Combat.register(new ICombatListener() {
            @Override
            public void onPlayerReceiveDamageModify(EntityPlayer player, Damage damage) {
                handlePlayerReceiveDamage(player, damage);
            }

            @Override
            public void onMobReceiveDamageModify(EntityMob mob, Damage damage) {
                handleMobReceiveDamage(mob, damage);
            }

            @Override
            public float onPlayerRawMeleeDamageModify(EntityPlayer player, Entity target, boolean critical, boolean suspended_in_liquid, float original) {
                return handlePlayerMeleeDamage(player, target, original);
            }

            @Override
            public float onPlayerRawStrVsBlockModify(EntityPlayer player, Item tool, Block block, int metadata, float original) {
                return handlePlayerRawStrVsBlock(player, tool, block, metadata, original);
            }

            @Override
            public float onPlayerStrVsBlockModify(EntityPlayer player, float original) {
                return original;
            }

            @Override
            public float onPlayerReceiveKnockBackModify(EntityPlayer player, Entity attacker, float original) {
                return handlePlayerKnockback(player, attacker, original);
            }

            @Override
            public float onEntityLivingFallDamageModify(EntityLivingBase instance, float fall_distance, BlockInfo block_landed_on_info, float original) {
                return handleEntityFall(instance, fall_distance, original);
            }
        });

        Handlers.EntityEvent.register(new IEntityEventListener() {
            @Override
            public void onJump(EntityLivingBase entity) {
                handlePlayerJump(entity);
            }
        });

        Handlers.Tick.register(new ITickListener() {
            @Override
            public void onEntityPlayerTick(EntityPlayer player) {
                handlePlayerTick(player);
            }

            @Override
            public void onServerTick(MinecraftServer server) {
                handleServerTick(server);
            }
        });

        Handlers.PlayerEvent.register(new IPlayerEventListener() {
            @Override
            public void onPlayerRespawn(PlayerRespawnEvent event) {
                handlePlayerRespawn(event);
            }

            @Override
            public void onPlayerCrossDimension(PlayerCrossDimensionEvent event) {
                DataHelper.resetMaxHealth(event.player());
            }
        });
    }

    // ===== Combat Handlers =====

    private static void handlePlayerReceiveDamage(EntityPlayer player, Damage damage) {
        DamageSource source = damage.getSource();

        if (!source.isFireDamage() && !source.isUnblockable()) {
            if (player.worldObj.rand.nextFloat() >= 1.0f - Stats.ROLL.getBonus(player)) {
                damage.setAmount(0);

                player.addPotionEffect(
                        new PotionEffect(Potion.damageBoost.id, 20, 2)
                );

                knockbackFlag.put(player, true);
                player.sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("grpg_Roll.message"));

                return;
            }
        }

        float damageMultiplier = 1.0F - (Stats.PROTECTION.getAppliedBonus(player,
                source) + Stats.TOUGH_SKIN.getAppliedBonus(player,
                source) + Stats.STAT_FEATHER_FALL.getAppliedBonus(player,
                source) + Stats.TEMPERING.getAppliedBonus(player,
                source));

        damage.setAmount(damage.getAmount() * damageMultiplier);
    }

    private static void handleMobReceiveDamage(EntityMob mob, Damage damage) {
        DamageSource source = damage.getSource();
        Entity src = source.getResponsibleEntity();

        if (src instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) src;
            ItemStack heldItem = player.getHeldItemStack();

            if (heldItem != null && Stats.BOWMANSHIP.isItemSupported(heldItem)) {
                float bonus = Math.round(damage.getAmount() * Stats.BOWMANSHIP.getAppliedBonus(player, heldItem));
                damage.setAmount(damage.getAmount() + bonus);
            }

            if (Stats.REAPER.isEffectiveOn(mob)) {
                float reap = Stats.REAPER.getBonus(player);
                float reapBonus = 0;
                if (Stats.STEALTH.isEffectiveOn(player))
                    reapBonus = reap * ((StatSpecial) Stats.STEALTH).getSecondaryBonus(player) / 100.0F;
                float reapChance = reap + reapBonus;
                if (player.getRNG().nextFloat() <= reapChance) {
                    player.onEnchantmentCritical(mob);
                    player.worldObj.playSoundEffect(mob.posX, mob.posY, mob.posZ, GokiSounds.REAPER.toString(), 1.0f, 1.0f);
                    damage.setAmount(100000.0F);
                }
            }
        }
    }

    private static float handlePlayerMeleeDamage(EntityPlayer player, Entity target, float original) {
        ItemStack heldItem = player.getHeldItemStack();
        float bonus = 0f;

        if (heldItem != null) {
            if (Stats.SWORDSMANSHIP.isItemSupported(heldItem)) {
                bonus = Math.round(original * Stats.SWORDSMANSHIP.getAppliedBonus(player, heldItem));
            }
        } else {
            bonus = Math.round(original + Stats.PUGILISM.getBonus(player));
        }

        return original + bonus;
    }

    private static float handlePlayerRawStrVsBlock(EntityPlayer player, Item tool, Block block, int metadata, float original) {
        ItemStack heldItem = player.getHeldItemStack();
        float multiplier = 1.0F;

        if (Stats.MINING.isEffectiveOn(heldItem, null, player.worldObj)) {
            multiplier += Stats.MINING.getBonus(player);
        }
        if (Stats.DIGGING.isEffectiveOn(heldItem, null, player.worldObj)) {
            multiplier += Stats.DIGGING.getBonus(player);
        }
        if (Stats.CHOPPING.isEffectiveOn(heldItem, null, player.worldObj)) {
            multiplier += Stats.CHOPPING.getBonus(player);
        }

        // 忍术: 潜行时增加开采速度
        if (player.isSneaking() && Stats.STEALTH.getBonus(player) > 0) {
            multiplier += ((StatSpecial) Stats.STEALTH).getSecondaryBonus(player) / 100.0F;
        }

        return original * multiplier;
    }

    private static float handlePlayerKnockback(EntityPlayer player, Entity attacker, float original) {
        if (attacker == null) return original;
        if (knockbackFlag.containsKey(attacker)) {
            knockbackFlag.remove(attacker);
            if (attacker instanceof EntityPlayer) {
                ((EntityPlayer) attacker).sendChatToPlayer(ChatMessageComponent.createFromTranslationKey("grpg_Roll.knockback"));
            }
            return original * 2f;
        }
        return original;
    }

    private static float handleEntityFall(EntityLivingBase entity, float fallDistance, float original) {
        if ((entity instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) entity;
            int featherFallLevel = DataHelper.getPlayerStatLevel(player,
                    Stats.STAT_FEATHER_FALL);
            if (fallDistance < 3.0D + featherFallLevel * 0.1D) {
                return 0.0F;
            }
        }
        return original;
    }

    // ===== Entity Event Handlers =====

    private static void handlePlayerJump(EntityLivingBase entity) {
        if ((entity instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.isSprinting()) {
                player.motionY *= 1.0F + Stats.LEAPER_V.getBonus(player);
                player.motionX *= 1.0F + Stats.LEAPER_H.getBonus(player);
                player.motionZ *= 1.0F + Stats.LEAPER_H.getBonus(player);
            }
        }
    }

    // ===== Tick Handlers =====

    private static void handlePlayerTick(EntityPlayer player) {
        handleTaskPlayerAPI(player);

        AttributeInstance atinst = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        AttributeModifier mod = new AttributeModifier(stealthSpeedID, "SneakSpeed", Stats.STEALTH.getBonus(player) / 100.0F, 1);
        atinst.removeModifier(mod);
        if (player.isSneaking() && Stats.STEALTH.getBonus(player) > 0) {
            atinst.applyModifier(mod);
        }

        atinst = player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance);
        mod = new AttributeModifier(knockbackResistanceID, "KnockbackResistance", Stats.STEADY_GUARD.getBonus(player), 0);
        atinst.removeModifier(mod);
        if (player.isBlocking() && Stats.STEADY_GUARD.getBonus(player) > 0) {
            atinst.applyModifier(mod);
        }
    }

    private static void handleServerTick(MinecraftServer server) {
        if (tickTimer.get() == GokiConfig.syncTicks) {
            tickTimer.lazySet(0);
            for (ServerPlayer player : (List<ServerPlayer>) server.getConfigurationManager().playerEntityList) {
                GokiNetwork.sendToClient(player, new S2CSyncAll(player));
            }
        } else {
            tickTimer.getAndIncrement();
        }
    }

    // ===== Player Event Handlers =====

    private static void handlePlayerRespawn(PlayerRespawnEvent event) {
        ServerPlayer serverPlayer = event.player();
        if (!serverPlayer.worldObj.isRemote) {
            if (GokiConfig.globalModifiers.loseStatsOnDeath) {
                for (int stat = 0; stat < StatBase.totalStats; stat++) {
                    DataHelper.multiplyPlayerStatLevel(serverPlayer,
                            StatBase.stats.get(stat),
                            level -> level - (int) (GokiConfig.globalModifiers.loseStatsMultiplier * level));
                }
            }
            GokiNetwork.sendToClient(serverPlayer, new S2CSyncAll(serverPlayer));
            serverPlayer.heal(serverPlayer.getMaxHealth());
        }
    }

    // ===== Helper Methods =====

    private static void handleTaskPlayerAPI(EntityPlayer player) {
        if (player.isInWater() && !player.capabilities.isFlying) {
            float multiplier = Math.max(0.0F,
                    Stats.SWIMMING.getBonus(player));
            if (multiplier > 0.0F) {
                if (isJumping(player)) {
                    player.jumpMovementFactor += multiplier;
                } else {
                    player.jumpMovementFactor += multiplier * 0.2F;
                }
            }
        }

        if (player.isOnLadder() && !player.isSneaking()) {
            float multiplier = Stats.CLIMBING.getBonus(player);
            if (multiplier > 0.0F) {
                player.moveEntity(player.motionX,
                        player.motionY * multiplier,
                        player.motionZ);
            }
        }
    }

    private static boolean isJumping(EntityLivingBase livingBase) {
        try {
            java.lang.reflect.Field field = EntityLivingBase.class.getDeclaredField("isJumping");
            field.setAccessible(true);
            return field.getBoolean(livingBase);
        } catch (Exception e) {
            try {
                java.lang.reflect.Field field = EntityLivingBase.class.getDeclaredField("field_70703_bu");
                field.setAccessible(true);
                return field.getBoolean(livingBase);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }
}
