package my.mod.module.impl;

import my.mod.mixin.MinecraftClientAccessor;
import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.NumberSetting;

public class FastPlace extends Module {
    private final NumberSetting delay = num("Delay", 0, 0, 4, 1);

    public FastPlace(int key) { super("FastPlace", Category.PLAYER, key); }

    @Override
    public void onTick() {
        MinecraftClientAccessor acc = (MinecraftClientAccessor) mc;
        if (acc.mymod$getItemUseCooldown() > delay.i()) acc.mymod$setItemUseCooldown(delay.i());
    }
}
