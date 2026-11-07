package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import my.mod.module.setting.NumberSetting;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class AutoTotem extends Module {
    private final BoolSetting always = bool("Always", true);          // всегда держать тотем в offhand
    private final NumberSetting health = num("Health", 10, 1, 20, 0.5); // порог HP, если Always выключен
    private final BoolSetting absorption = bool("Absorption", true);    // считать золотые сердца
    private final NumberSetting delay = num("Delay", 1, 0, 10, 1);      // пауза между свапами (тики)

    private int cooldown;

    public AutoTotem(int key) { super("AutoTotem", Category.COMBAT, key); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.interactionManager == null) return;

        if (cooldown > 0) { cooldown--; return; }
        if (p.currentScreenHandler != p.playerScreenHandler) return; // открыт сундук и т.п.
        if (p.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)) return;

        if (!always.get()) {
            float hp = p.getHealth() + (absorption.get() ? p.getAbsorptionAmount() : 0f);
            if (hp > health.f()) return;
        }

        int slot = findTotem(p);
        if (slot == -1) return;

        // button 40 + SWAP = обмен со слотом offhand
        mc.interactionManager.clickSlot(p.playerScreenHandler.syncId, toScreenSlot(slot), 40, SlotActionType.SWAP, p);
        cooldown = delay.i();
    }

    private int findTotem(ClientPlayerEntity p) {
        for (int i = 0; i < 36; i++) {
            if (p.getInventory().getStack(i).isOf(Items.TOTEM_OF_UNDYING)) return i;
        }
        return -1;
    }

    private int toScreenSlot(int invIndex) { return invIndex < 9 ? invIndex + 36 : invIndex; }
}
