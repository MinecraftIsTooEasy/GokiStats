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
        return this.gokiStatsNBT;
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void onWriteEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        compound.setCompoundTag("gokistats_Stats", this.gokiStatsNBT);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void onReadEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey("gokistats_Stats")) {
            this.gokiStatsNBT = compound.getCompoundTag("gokistats_Stats");
        } else {
            this.gokiStatsNBT = new NBTTagCompound();
        }
        EntityPlayer self = (EntityPlayer) (Object) this;
        DataHelper.resetMaxHealth(self);
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
