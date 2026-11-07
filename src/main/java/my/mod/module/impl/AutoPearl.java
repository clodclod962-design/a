package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.EnumSetting;
import my.mod.module.setting.NumberSetting;
import my.mod.util.RotationUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class AutoPearl extends Module {
    public enum Mode { DOWN, AWAY }

    private final NumberSetting health = num("Health", 6, 1, 20, 0.5);
    private final EnumSetting<Mode> mode = mode("Mode", Mode.AWAY);
    private final NumberSetting pitchAway = num("AwayPitch", -20, -80, 30, 1);
    private final NumberSetting threatRange = num("ThreatRange", 10, 3, 30, 1);
    private final NumberSetting cooldownTicks = num("Cooldown", 40, 20, 200, 1);

    private int cooldown;

    public AutoPearl(int key) { super("AutoPearl", Category.COMBAT, key); }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.interactionManager == null || mc.getNetworkHandler() == null) return;

        if (cooldown > 0) { cooldown--; return; }
        if (p.getHealth() + p.getAbsorptionAmount() > health.f()) return;
        if (p.getItemCooldownManager().isCoolingDown(Items.ENDER_PEARL)) return;

        int slot = findPearlInHotbar(p);
        if (slot == -1) return;

        float yaw = p.getYaw();
        float pitch;
        if (mode.get() == Mode.DOWN) {
            pitch = 90f;
        } else {
            PlayerEntity threat = nearestPlayer(p);
            if (threat != null) yaw = RotationUtil.yaw(p.getPos(), threat.getPos()) + 180f;
            pitch = pitchAway.f();
        }

        int prevSlot = p.getInventory().selectedSlot;
        float oldYaw = p.getYaw(), oldPitch = p.getPitch();

        // сервер берёт направление броска из последнего move-пакета
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, p.isOnGround()));
        p.getInventory().selectedSlot = slot;
        mc.interactionManager.interactItem(p, Hand.MAIN_HAND); // сам синкает слот
        p.swingHand(Hand.MAIN_HAND);
        p.getInventory().selectedSlot = prevSlot; // синк обратно пойдёт на следующем тике
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(oldYaw, oldPitch, p.isOnGround()));

        cooldown = cooldownTicks.i();
    }

    private int findPearlInHotbar(ClientPlayerEntity p) {
        for (int i = 0; i < 9; i++) {
            if (p.getInventory().getStack(i).isOf(Items.ENDER_PEARL)) return i;
        }
        return -1;
    }

    private PlayerEntity nearestPlayer(ClientPlayerEntity p) {
        PlayerEntity best = null;
        double bestDist = threatRange.get();
        for (PlayerEntity other : mc.world.getPlayers()) {
            if (other == p || !other.isAlive() || other.isSpectator()) continue;
            double d = p.distanceTo(other);
            if (d < bestDist) { bestDist = d; best = other; }
        }
        return best;
    }
}
