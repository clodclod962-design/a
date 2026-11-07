package my.mod.util;

import net.minecraft.client.network.ClientPlayerEntity;

public final class MoveUtil {
    private MoveUtil() {}

    public static boolean isMoving(ClientPlayerEntity p) {
        return p.input.movementForward != 0 || p.input.movementSideways != 0;
    }

    /** Задаёт горизонтальную скорость по направлению ввода (WASD) и взгляду. */
    public static void setHorizontal(ClientPlayerEntity p, double speed) {
        float f = p.input.movementForward;
        float s = p.input.movementSideways; // >0 = влево
        if (f == 0 && s == 0) return;
        double rad = Math.toRadians(p.getYaw());
        double x = -Math.sin(rad) * f + Math.cos(rad) * s;
        double z = Math.cos(rad) * f + Math.sin(rad) * s;
        double len = Math.hypot(x, z);
        if (len < 1.0E-6) return;
        p.setVelocity(x / len * speed, p.getVelocity().y, z / len * speed);
    }
}
