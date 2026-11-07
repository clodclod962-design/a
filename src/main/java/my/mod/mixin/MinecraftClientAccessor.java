package my.mod.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor("itemUseCooldown")
    int mymod$getItemUseCooldown();

    @Accessor("itemUseCooldown")
    void mymod$setItemUseCooldown(int value);
}
