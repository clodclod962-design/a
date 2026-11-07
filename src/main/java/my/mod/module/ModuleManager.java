package my.mod.module;

import my.mod.module.impl.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // Combat
        register(new AutoTotem(GLFW.GLFW_KEY_Z));
        register(new AutoPearl(GLFW.GLFW_KEY_X));
        register(new KillAura(GLFW.GLFW_KEY_R));
        register(new Velocity(GLFW.GLFW_KEY_V));
        // Movement
        register(new Sprint(0));
        register(new Flight(GLFW.GLFW_KEY_G));
        register(new Speed(0));
        register(new NoFall(0));
        register(new Spider(0));
        // Player
        register(new FastPlace(0));
        register(new AutoRespawn(0));
        // Render
        register(new Fullbright(GLFW.GLFW_KEY_B));
        register(new Esp(0));
        // Misc
        HudModule hud = new HudModule(0);
        register(hud);
        hud.setEnabled(true);
    }

    public void register(Module m) { modules.add(m); }

    public List<Module> getAll() { return modules; }

    public List<Module> byCategory(Category c) {
        List<Module> out = new ArrayList<>();
        for (Module m : modules) if (m.getCategory() == c) out.add(m);
        return out;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz) {
        for (Module m : modules) if (m.getClass() == clazz) return (T) m;
        return null;
    }

    public void onTick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        long window = mc.getWindow().getHandle();
        for (Module m : modules) {
            boolean down = mc.currentScreen == null && m.getKey() > 0
                    && InputUtil.isKeyPressed(window, m.getKey());
            if (down && !m.keyWasDown) m.toggle();
            m.keyWasDown = down;

            if (m.isEnabled()) m.onTick();
        }
    }
}
