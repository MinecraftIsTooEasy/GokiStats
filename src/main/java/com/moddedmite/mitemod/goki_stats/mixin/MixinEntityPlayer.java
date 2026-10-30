package com.moddedmite.mitemod.goki_stats.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moddedmite.mitemod.goki_stats.api.IGokiStatsPlayer;
import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.EntityPlayer;
import net.minecraft.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for EntityPlayer to hook getHealthLimit and NBT save/load for stat data.
 * 实现 IGokiStatsPlayer 接口，将技能 NBT 数据直接存储在实体上。
 */
@Mixin(EntityPlayer.class)
public class MixinEntityPlayer implements IGokiStatsPlayer {

    @Unique
    private NBTTagCompound gokiStatsNBT = new NBTTagCompound();

    @Override
    @Unique
    public NBTTagCompound gokiStats$getStatsNBT() {
        if (this.gokiStatsNBT == null) {
            this.gokiStatsNBT = new NBTTagCompound();
        }
        return this.gokiStatsNBT;
    }

    @Override
    @Unique
    public void gokiStats$setStatsNBT(NBTTagCompound nbt) {
        this.gokiStatsNBT = nbt != null ? nbt : new NBTTagCompound();
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void onWriteEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        compound.setCompoundTag("gokistats_Stats", this.gokiStats$getStatsNBT());
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void onReadEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey("gokistats_Stats") && compound.getTag("gokistats_Stats") instanceof NBTTagCompound) {
            this.gokiStatsNBT = (NBTTagCompound) compound.getCompoundTag("gokistats_Stats").copy();
            EntityPlayer self = (EntityPlayer) (Object) this;
            DataHelper.resetMaxHealth(self);
        }
    }

    /**
     * 玩家死亡重生等场景会新建实体并调用 clonePlayer，原版只复制背包/经验/血量，
     * 不会复制技能数据，这里补上（与 RustedIronCore 处理 FirstLogin 的做法一致）。
     */
    @Inject(method = "clonePlayer", at = @At("RETURN"))
    private void onClonePlayer(EntityPlayer original, boolean keepInventory, CallbackInfo ci) {
        NBTTagCompound nbt = ((IGokiStatsPlayer) original).gokiStats$getStatsNBT();
        this.gokiStats$setStatsNBT((NBTTagCompound) nbt.copy());
        DataHelper.resetMaxHealth((EntityPlayer) (Object) this);
    }

    @ModifyReturnValue(method = "getHealthLimit()F", at = @At("RETURN"))
    private float modifyHealthLimit(float original) {
        if (this.gokiStatsNBT == null) {
            return original;
        }
        EntityPlayer self = (EntityPlayer) (Object) this;
        int statLevel = DataHelper.getPlayerStatLevel(self, Stats.MAX_HEALTH);
        if (statLevel > 0) {
            return original + Stats.MAX_HEALTH.getBonus(statLevel);
        }
        return original;
    }
}
