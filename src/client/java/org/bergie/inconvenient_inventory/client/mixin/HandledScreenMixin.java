package org.bergie.inconvenient_inventory.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin( HandledScreen.class )
public abstract class HandledScreenMixin<T extends ScreenHandler> implements ScreenHandlerProvider<T> {
    @ModifyArg(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V"), index = 3)
    private SlotActionType injected(SlotActionType actionType)
    {
        if (actionType == SlotActionType.QUICK_MOVE)
        {
            actionType = SlotActionType.PICKUP;
        }
        return actionType;
    }

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    private void injected(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci)
    {
        if (actionType == SlotActionType.PICKUP_ALL)
        {
            ci.cancel();
        }
    }

    @Inject(method = "handleHotbarKeyPressed", at = @At("HEAD"), cancellable = true)
    private void injected(KeyInput keyInput, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(false);
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"), cancellable = true)
    private void injected(Click click, double offsetX, double offsetY, CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(true);
    }
}
