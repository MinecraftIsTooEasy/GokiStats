package com.moddedmite.mitemod.goki_stats.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.EntityPlayer;
import net.minecraft.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for EntityPlayer to hook getHealthLimit and NBT save/load for stat data.
 */
@Mixin(EntityPlayer.class)
public class MixinEntityPlayer {

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void onWriteEntityToNBT(NBTTagCompound compound, CallbackInfo ci) {
        EntityPlayer self = (EntityPlayer) (Object) this;
        DataHelper.saveToNBT(self, compound);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void onReadEntityFromNBT(NBTTagCompound compound, CallbackInfo ci) {
        EntityPlayer self = (EntityPlayer) (Object) this;
        DataHelper.loadFromNBT(self, compound);
    }

    @ModifyReturnValue(method = "getHealthLimit()F", at = @At("RETURN"))
    private float modifyHealthLimit(float original) {
        EntityPlayer self = (EntityPlayer) (Object) this;
        int statLevel = DataHelper.getPlayerStatLevel(self, Stats.MAX_HEALTH);
        if (statLevel > 0) {
            return original + statLevel;
        }
        return original;
    }
}
