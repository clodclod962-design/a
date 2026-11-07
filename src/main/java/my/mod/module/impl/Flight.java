package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.EnumSetting;
import my.mod.module.setting.NumberSetting;
import my.mod.util.MoveUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module {
    public enum Mode { ABILITIES, VELOCITY }

    private final EnumSetting<Mode> mode = mode("Mode", Mode.VELOCITY);
    private final NumberSetting speed = num("Speed", 1.0, 0.1, 5.0, 0.1);

    public Flight(int key) { super("Flight", Category.MOVEMENT, key); }

    @Override
    protected void onDisable() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        p.getAbilities().setFlySpeed(0.05f);
        if (!p.isCreative() && !p.isSpectator()) {
            p.getAbilities().allowFlying = false;
            p.getAbilities().flying = false;
        }
    }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;

        if (mode.get() == Mode.ABILITIES) {
            p.getAbilities().allowFlying = true;
            p.getAbilities().flying = true;
            p.getAbilities().setFlySpeed(0.05f * speed.f());
        } else {
            double sp = speed.get();
            if (MoveUtil.isMoving(p)) MoveUtil.setHorizontal(p, sp);
            else p.setVelocity(0, p.getVelocity().y, 0);

            double y = 0;
            if (p.input.jumping) y = sp * 0.6;
            else if (p.input.sneaking) y = -sp * 0.6;
            Vec3d v = p.getVelocity();
            p.setVelocity(v.x, y, v.z);
            p.fallDistance = 0;
        }
    }
}
