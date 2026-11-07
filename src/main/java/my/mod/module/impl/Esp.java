package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;

/** Подсветка (outline) сущностей сквозь стены. Логика в MinecraftClientMixin.hasOutline. */
public class Esp extends Module {
    private final BoolSetting players = bool("Players", true);
    private final BoolSetting mobs = bool("Mobs", false);

    public Esp(int key) { super("ESP", Category.RENDER, key); }

    public boolean shouldOutline(Entity e) {
        if (e == mc.player || !(e instanceof LivingEntity) || e instanceof ArmorStandEntity) return false;
        return e instanceof PlayerEntity ? players.get() : mobs.get();
    }
}
