package com.moddedmite.mitemod.goki_stats.mixin;

import com.moddedmite.mitemod.goki_stats.api.stat.Stats;
import com.moddedmite.mitemod.goki_stats.common.handlers.GokiHandlers;
import com.moddedmite.mitemod.goki_stats.common.utils.DataHelper;
import net.minecraft.Block;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Mixin for EntityPlayer to hook harvest block events and NBT save/load for stat data.
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

    @Inject(method = "getHealthLimit()F", at = @At("RETURN"), cancellable = true)
    private void onGetHealthLimit(CallbackInfoReturnable<Float> cir) {
        EntityPlayer self = (EntityPlayer) (Object) this;
        int statLevel = DataHelper.getPlayerStatLevel(self, Stats.MAX_HEALTH);
        if (statLevel > 0) {
            cir.setReturnValue(cir.getReturnValue() + statLevel);
        }
    }
}
