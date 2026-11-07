package my.mod.util;

import net.minecraft.util.math.Vec3d;

public final class RotationUtil {
    private RotationUtil() {}

    public static float yaw(Vec3d from, Vec3d to) {
        Vec3d d = to.subtract(from);
        return (float) Math.toDegrees(Math.atan2(d.z, d.x)) - 90f;
    }

    public static float pitch(Vec3d from, Vec3d to) {
        Vec3d d = to.subtract(from);
        return (float) -Math.toDegrees(Math.atan2(d.y, Math.hypot(d.x, d.z)));
    }
}
