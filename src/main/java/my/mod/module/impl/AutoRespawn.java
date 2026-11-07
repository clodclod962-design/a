package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module {
    public AutoRespawn(int key) { super("AutoRespawn", Category.PLAYER, key); }

    @Override
    public void onTick() {
        if (mc.player != null && mc.currentScreen instanceof DeathScreen) {
            mc.player.requestRespawn();
        }
    }
}
