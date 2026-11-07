package my.mod.mixin;

import my.mod.MyMod;
import my.mod.module.impl.Esp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "hasOutline", at = @At("HEAD"), cancellable = true)
    private void mymod$outline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (MyMod.MODULES == null) return;
        Esp esp = MyMod.MODULES.get(Esp.class);
        if (esp != null && esp.isEnabled() && esp.shouldOutline(entity)) cir.setReturnValue(true);
    }
}
