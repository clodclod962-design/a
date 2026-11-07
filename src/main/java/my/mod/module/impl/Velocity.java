package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import my.mod.module.setting.NumberSetting;

public class Velocity extends Module {
    private final NumberSetting horizontal = num("Horizontal%", 0, 0, 100, 1);
    private final NumberSetting vertical = num("Vertical%", 0, 0, 100, 1);
    private final BoolSetting explosions = bool("Explosions", true);

    public Velocity(int key) { super("Velocity", Category.COMBAT, key); }

    public float h() { return horizontal.f() / 100f; }
    public float v() { return vertical.f() / 100f; }
    public boolean affectsExplosions() { return explosions.get(); }

    public boolean isOwnPlayer(int entityId) {
        return mc.player != null && mc.player.getId() == entityId;
    }
}
