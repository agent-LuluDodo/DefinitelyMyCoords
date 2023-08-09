package de.luludodo.dmc.mixins.betterf3;

import me.cominixo.betterf3.modules.ChunksModule;
import de.luludodo.dmc.api.DMCApi;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChunksModule.class)
public class ChunksModuleMixin {
    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/ChunkPos;<init>(Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0), index = 0)
    public BlockPos getBlockPos(BlockPos original) {
        return new BlockPos(
                DMCApi.getOffsetBlockX(original.getX()),
                DMCApi.getOffsetBlockY(original.getY()),
                DMCApi.getOffsetBlockZ(original.getZ())
        );
    }
}
