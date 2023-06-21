package de.luludodo.dmc.mixin;

import de.luludodo.dmc.config.ConfigAPI;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MathHelper.class)
public class ObscureRotationsMixin {
    @ModifyVariable(argsOnly = true, method = "hashCode(III)J", at = @At("HEAD"), ordinal = 0)
    private static int x(int x) {
        return ConfigAPI.getObscureRotations()? x + ConfigAPI.getOffsetRotations() : x + ConfigAPI.getOffsetX();
    }
    @ModifyVariable(argsOnly = true, method = "hashCode(III)J", at = @At("HEAD"), ordinal = 1)
    private static int y(int y) {
        return ConfigAPI.getObscureRotations()? y + ConfigAPI.getOffsetRotations() : y + ConfigAPI.getOffsetY();
    }
    @ModifyVariable(argsOnly = true, method = "hashCode(III)J", at = @At("HEAD"), ordinal = 2)
    private static int z(int z) {
        return ConfigAPI.getObscureRotations()? z + ConfigAPI.getOffsetRotations() : z + ConfigAPI.getOffsetZ();
    }
}
