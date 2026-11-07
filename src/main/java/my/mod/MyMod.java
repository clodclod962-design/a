package my.mod;

import my.mod.config.Config;
import my.mod.gui.ClickGui;
import my.mod.module.ModuleManager;
import my.mod.module.impl.HudModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MyMod implements ClientModInitializer {
    /** Горячая клавиша меню. */
    public static final int GUI_KEY = GLFW.GLFW_KEY_RIGHT_SHIFT;

    public static ModuleManager MODULES;
    private static boolean guiKeyWasDown;

    @Override
    public void onInitializeClient() {
        // Модули создаём, когда клиент уже полностью запущен (MinecraftClient точно существует).
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ModuleManager mm = new ModuleManager();
            mm.init();
            MODULES = mm;
            Config.load();
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (MODULES == null) return;

            // Правый Shift -> меню (опрашиваем клавишу напрямую, без KeyBinding)
            boolean down = client.currentScreen == null
                    && InputUtil.isKeyPressed(client.getWindow().getHandle(), GUI_KEY);
            if (down && !guiKeyWasDown) client.setScreen(new ClickGui());
            guiKeyWasDown = down || (client.currentScreen != null && guiKeyWasDown);

            MODULES.onTick();
        });

        HudRenderCallback.EVENT.register((ctx, tickDelta) -> {
            if (MODULES == null) return;
            HudModule hud = MODULES.get(HudModule.class);
            if (hud != null && hud.isEnabled()) hud.render(ctx);
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> Config.save());
    }
}
