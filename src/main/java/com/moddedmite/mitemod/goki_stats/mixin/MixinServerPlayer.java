package com.moddedmite.mitemod.goki_stats.mixin;

import com.moddedmite.mitemod.goki_stats.api.IGokiStatsPlayer;
import net.minecraft.NBTBase;
import net.minecraft.NBTTagCompound;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * MITE 的 ServerPlayer 重写了 writeEntityToNBT / readEntityFromNBT。
 * 在此再补一次技能数据的读写，保证玩家存档落盘与重进读取不依赖父类方法是否被 super 调用。
 */
@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void gokiStats$write(NBTTagCompound compound, CallbackInfo ci) {
        NBTTagCompound nbt = ((IGokiStatsPlayer) (Object) this).gokiStats$getStatsNBT();
        if (nbt != null) {
            compound.setCompoundTag("gokistats_Stats", nbt);
        }
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void gokiStats$read(NBTTagCompound compound, CallbackInfo ci) {
        NBTBase tag = compound.getTag("gokistats_Stats");
        if (tag instanceof NBTTagCompound) {
            ((IGokiStatsPlayer) (Object) this).gokiStats$setStatsNBT((NBTTagCompound) tag.copy());
        }
    }
}
