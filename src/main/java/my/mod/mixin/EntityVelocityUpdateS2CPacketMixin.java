package my.mod.mixin;

import my.mod.MyMod;
import my.mod.module.impl.Velocity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public abstract class EntityVelocityUpdateS2CPacketMixin {
    @Shadow public abstract int getId();

    private Velocity mymod$active() {
        if (MyMod.MODULES == null) return null;
        Velocity v = MyMod.MODULES.get(Velocity.class);
        return (v != null && v.isEnabled() && v.isOwnPlayer(getId())) ? v : null;
    }

    @Inject(method = "getVelocityX", at = @At("RETURN"), cancellable = true)
    private void mymod$x(CallbackInfoReturnable<Integer> cir) {
        Velocity v = mymod$active();
        if (v != null) cir.setReturnValue((int) (cir.getReturnValueI() * v.h()));
    }

    @Inject(method = "getVelocityY", at = @At("RETURN"), cancellable = true)
    private void mymod$y(CallbackInfoReturnable<Integer> cir) {
        Velocity v = mymod$active();
        if (v != null) cir.setReturnValue((int) (cir.getReturnValueI() * v.v()));
    }

    @Inject(method = "getVelocityZ", at = @At("RETURN"), cancellable = true)
    private void mymod$z(CallbackInfoReturnable<Integer> cir) {
        Velocity v = mymod$active();
        if (v != null) cir.setReturnValue((int) (cir.getReturnValueI() * v.h()));
    }
}
