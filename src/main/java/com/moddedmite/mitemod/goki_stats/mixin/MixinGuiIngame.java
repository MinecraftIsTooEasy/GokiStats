package com.moddedmite.mitemod.goki_stats.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.GuiIngame;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Shadow
    private Minecraft mc;

    @ModifyExpressionValue(
            method = "func_110327_a",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/AttributeInstance;getAttributeValue()D")
    )
    private double modifyMaxHealthForRendering(double original) {
        return this.mc.thePlayer.getHealthLimit();
    }
}
