package my.mod.module.impl;

import my.mod.module.Category;
import my.mod.module.Module;
import my.mod.module.setting.BoolSetting;
import my.mod.module.setting.EnumSetting;
import my.mod.module.setting.NumberSetting;
import my.mod.util.RotationUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class KillAura extends Module {
    public enum Sort { DISTANCE, HEALTH }

    private final NumberSetting range = num("Range", 4.2, 2.0, 6.0, 0.1);
    private final BoolSetting players = bool("Players", true);
    private final BoolSetting mobs = bool("Mobs", false);
    private final BoolSetting invisible = bool("Invisible", true);
    private final EnumSetting<Sort> sort = mode("Sort", Sort.DISTANCE);
    private final BoolSetting silent = bool("SilentRotation", true);
    private final BoolSetting crits = bool("Criticals", false);
    private final NumberSetting cooldownMin = num("MinCooldown", 0.95, 0.5, 1.0, 0.01);
    // TargetStrafe
    private final BoolSetting strafe = bool("TargetStrafe", true);
    private final NumberSetting strafeSpeed = num("StrafeSpeed", 0.28, 0.10, 0.80, 0.01);
    private final NumberSetting strafeDist = num("StrafeDistance", 3.0, 1.0, 5.0, 0.1);

    private LivingEntity target;
    private int dir = 1;

    public KillAura(int key) { super("KillAura", Category.COMBAT, key); }

    @Override protected void onDisable() { target = null; }

    @Override
    public void onTick() {
        ClientPlayerEntity p = mc.player;
        if (p == null || mc.world == null || mc.interactionManager == null || mc.getNetworkHandler() == null) return;

        target = findTarget(p);
        if (target == null) return;

        if (strafe.get()) doStrafe(p, target);

        if (p.getAttackCooldownProgress(0.5f) < cooldownMin.f()) return;

        float yaw = RotationUtil.yaw(p.getEyePos(), target.getEyePos());
        float pitch = RotationUtil.pitch(p.getEyePos(), target.getEyePos());

        if (silent.get()) {
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch, p.isOnGround()));
        } else {
            p.setYaw(yaw);
            p.setPitch(pitch);
        }

        if (crits.get() && p.isOnGround() && !p.isTouchingWater() && !p.isClimbing()) {
            double x = p.getX(), y = p.getY(), z = p.getZ();
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y + 0.0625, z, false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, false));
        }

        mc.interactionManager.attackEntity(p, target);
        p.swingHand(Hand.MAIN_HAND);
    }

    private LivingEntity findTarget(ClientPlayerEntity p) {
        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;
        for (Entity e : mc.world.getEntities()) {
            if (!(e instanceof LivingEntity le) || le == p) continue;
            if (le instanceof ArmorStandEntity) continue;
            if (!le.isAlive() || le.isSpectator()) continue;
            boolean isPlayer = le instanceof PlayerEntity;
            if (isPlayer ? !players.get() : !mobs.get()) continue;
            if (!invisible.get() && le.isInvisible()) continue;
            double dist = p.distanceTo(le);
            if (dist > range.get()) continue;
            double score = sort.get() == Sort.HEALTH ? le.getHealth() : dist;
            if (score < bestScore) { bestScore = score; best = le; }
        }
        return best;
    }

    /** Движение по кругу вокруг цели, держим дистанцию StrafeDistance. */
    private void doStrafe(ClientPlayerEntity p, LivingEntity t) {
        double dx = t.getX() - p.getX();
        double dz = t.getZ() - p.getZ();
        double dist = Math.hypot(dx, dz);
        if (dist < 1.0E-4) return;

        if (p.horizontalCollision) dir = -dir;

        double ux = dx / dist, uz = dz / dist;       // к цели
        double tx = -uz * dir, tz = ux * dir;        // касательная
        double radial = MathHelper.clamp(dist - strafeDist.get(), -1.0, 1.0);

        double mx = tx + ux * radial;
        double mz = tz + uz * radial;
        double len = Math.hypot(mx, mz);
        if (len < 1.0E-4) return;

        double sp = strafeSpeed.get();
        p.setVelocity(mx / len * sp, p.getVelocity().y, mz / len * sp);
    }
}
