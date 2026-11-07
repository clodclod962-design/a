package my.mod.module.impl;

import my.mod.MyMod;
import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;

import java.util.Comparator;
import java.util.List;

public class HudModule extends Module {
    private final BoolSetting watermark = bool("Watermark", true);
    private final BoolSetting arrayList = bool("ArrayList", true);

    public HudModule(int key) { super("HUD", Category.MISC, key); }

    public void render(DrawContext ctx) {
        if (mc.player == null || mc.options.hudHidden) return;
        TextRenderer tr = mc.textRenderer;

        if (watermark.get()) {
            ctx.drawText(tr, "MyMod 1.20.1", 4, 4, 0xFF55FFAA, true);
        }
        if (arrayList.get()) {
            List<Module> on = MyMod.MODULES.getAll().stream()
                    .filter(m -> m.isEnabled() && m != this)
                    .sorted(Comparator.comparingInt((Module m) -> tr.getWidth(m.getName())).reversed())
                    .toList();
            int w = mc.getWindow().getScaledWidth();
            int y = 4;
            for (int i = 0; i < on.size(); i++) {
                String n = on.get(i).getName();
                int color = 0xFF000000 | MathHelper.hsvToRgb((i * 0.05f) % 1f, 0.55f, 1f);
                ctx.drawText(tr, n, w - tr.getWidth(n) - 4, y, color, true);
                y += 10;
            }
        }
    }
}
