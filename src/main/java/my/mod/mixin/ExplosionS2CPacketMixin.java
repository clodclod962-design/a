package my.mod.mixin;

import my.mod.MyMod;
import my.mod.module.impl.Velocity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExplosionS2CPacket.class)
public abstract class ExplosionS2CPacketMixin {

    private Velocity mymod$active() {
        if (MyMod.MODULES == null) return null;
        Velocity v = MyMod.MODULES.get(Velocity.class);
        return (v != null && v.isEnabled() && v.affectsExplosions()) ? v : null;
    }

    @Inject(method = "getPlayerVelocityX", at = @At("RETURN"), cancellable = true)
    private void mymod$x(CallbackInfoReturnable<Float> cir) {
        Velocity v = mymod$active();
        if (v != null) cir.setReturnValue(cir.getReturnValueF() * v.h());
    }

    @Inject(method = "getPlayerVelocityY", at = @At("RETURN"), cancellable = true)
    private void mymod$y(CallbackInfoReturnable<Float> cir) {
        Velocity v = mymod$active();
        if (v != null) cir.setReturnValue(cir.getReturnValueF() * v.v());
    }

    @Inject(method = "getPlayerVelocityZ", at = @At("RETURN"), cancellable = true)
    private void mymod$z(CallbackInfoReturnable<Float> cir) {
        Velocity v = mymod$active();
        if (v != null) cir.setReturnValue(cir.getReturnValueF() * v.h());
    }
}
