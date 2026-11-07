package my.mod.module;

import my.mod.module.impl.HudModule;
import my.mod.module.setting.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final Category category;
    private int key; // GLFW key code, 0 = не назначен
    private boolean enabled;
    boolean keyWasDown;
    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, Category category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public String getName() { return name; }
    public Category getCategory() { return category; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public boolean isEnabled() { return enabled; }
    public List<Setting<?>> getSettings() { return settings; }

    public void toggle() { setEnabled(!enabled); }

    public void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (enabled) onEnable(); else onDisable();
        if (mc.player != null && !(this instanceof HudModule)) {
            mc.player.sendMessage(Text.literal(name + (enabled ? " §aON" : " §cOFF")), true);
        }
    }

    protected void onEnable() {}
    protected void onDisable() {}
    public void onTick() {}

    // --- helpers для объявления настроек ---
    protected <S extends Setting<?>> S add(S s) { settings.add(s); return s; }
    protected BoolSetting bool(String n, boolean def) { return add(new BoolSetting(n, def)); }
    protected NumberSetting num(String n, double def, double min, double max, double step) {
        return add(new NumberSetting(n, def, min, max, step));
    }
    protected <E extends Enum<E>> EnumSetting<E> mode(String n, E def) { return add(new EnumSetting<>(n, def)); }
}
