package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.NumberSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class Spider extends Module {
    private final NumberSetting speed = num("Speed", 0.22, 0.05, 0.6, 0.01);

    public Spider(int key) { super("Spider", Category.MOVEMENT, key); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        if (p.horizontalCollision) {
            Vec3d v = p.getVelocity();
            p.setVelocity(v.x, speed.get(), v.z);
        }
    }
}
