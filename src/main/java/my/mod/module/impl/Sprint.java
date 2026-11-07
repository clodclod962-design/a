package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import net.minecraft.client.network.ClientPlayerEntity;

public class Sprint extends Module {
    private final BoolSetting omni = bool("AllDirections", false);

    public Sprint(int key) { super("Sprint", Category.MOVEMENT, key); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null) return;
        boolean moving = omni.get()
                ? (p.input.movementForward != 0 || p.input.movementSideways != 0)
                : p.input.movementForward > 0;
        if (moving && !p.isSneaking() && !p.horizontalCollision && p.getHungerManager().getFoodLevel() > 6) {
            p.setSprinting(true);
        }
    }
}
