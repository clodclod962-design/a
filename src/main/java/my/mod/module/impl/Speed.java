package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import my.mod.module.setting.NumberSetting;
import my.mod.util.MoveUtil;
import net.minecraft.client.network.ClientPlayerEntity;

public class Speed extends Module {
    private final NumberSetting speed = num("Speed", 0.35, 0.10, 1.00, 0.01);
    private final BoolSetting bhop = bool("BunnyHop", true);

    public Speed(int key) { super("Speed", Category.MOVEMENT, key); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null || p.isTouchingWater() || p.isClimbing() || p.getAbilities().flying) return;
        if (!MoveUtil.isMoving(p)) return;

        if (bhop.get() && p.isOnGround()) p.jump();
        MoveUtil.setHorizontal(p, speed.get());
    }
}
