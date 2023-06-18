package de.luludodo.dmc.mixin;

import de.luludodo.dmc.RelativeF3Coords;
import de.luludodo.dmc.config.ConfigAPI;
import de.luludodo.dmc.coords.Mode;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Mixin(DebugHud.class)
public abstract class F3MenuModifierMixin {

    // Shadow functions
    @Shadow
    @Nullable
    protected abstract String getServerWorldDebugString();
    @Shadow protected abstract String propertyToString(Map.Entry<Property<?>, Comparable<?>> propEntry);
    @Shadow private HitResult blockHit;
    @Shadow private HitResult fluidHit;
    // Coords when player opened F3 menu
    private static double x = 0;
    private static double y = 0;
    private static double z = 0;
    private static int blockX = 0;
    private static int blockY = 0;
    private static int blockZ = 0;

    @SuppressWarnings("unchecked")
    @Inject(at = @At("RETURN"), method = "getRightText", cancellable = true)
    protected void getRightTextMixin(@NotNull CallbackInfoReturnable<List<String>> info) {
        // Checks if values should be offset or relative
        if(ConfigAPI.getMode() == Mode.CUSTOM) {
            // Gets client
            MinecraftClient client = MinecraftClient.getInstance();
            // Gets ArrayList of Text
            List<String> debug = info.getReturnValue();

            // Checks if Client doesn't use reduced Debug
            if (!client.hasReducedDebugInfo()) {
                // Cuts all parts with coords out
                debug = new ArrayList<>(debug.subList(0, 10));
                BlockPos blockPos;

                // If looking at a Block change the Coordinates of the Block and add it
                if (this.blockHit.getType() == HitResult.Type.BLOCK) {
                    blockPos = ((BlockHitResult)this.blockHit).getBlockPos();
                    BlockState blockState = client.world.getBlockState(blockPos);
                    debug.add("");
                    debug.add(Formatting.UNDERLINE + "Targeted Block: " + (blockPos.getX() - blockX) + ", " + (blockPos.getY() - blockY) + ", " + (blockPos.getZ() - blockZ));
                    debug.add(String.valueOf(Registries.BLOCK.getId(blockState.getBlock())));
                    for (Map.Entry entry : blockState.getEntries().entrySet()) {
                        debug.add(propertyToString(entry));
                    }
                    blockState.streamTags().map(tag -> "#" + tag.id()).forEach(debug::add);
                }

                // If looking at a Fluid change the Coordinates of the Fluid and add it
                if (this.fluidHit.getType() == HitResult.Type.BLOCK) {
                    blockPos = ((BlockHitResult)this.fluidHit).getBlockPos();
                    FluidState fluidState = client.world.getFluidState(blockPos);
                    debug.add("");
                    debug.add(Formatting.UNDERLINE + "Targeted Fluid: " + (blockPos.getX() - blockX) + ", " + (blockPos.getY() - blockY) + ", " + (blockPos.getZ() - blockZ));
                    debug.add(String.valueOf(Registries.FLUID.getId(fluidState.getFluid())));
                    for (Map.Entry entry : fluidState.getEntries().entrySet()) {
                        debug.add(this.propertyToString(entry));
                    }
                    fluidState.streamTags().map(tag -> "#" + tag.id()).forEach(debug::add);
                }

                // If looking at an Entity add it
                Entity entity;
                if ((entity = client.targetedEntity) != null) {
                    debug.add("");
                    debug.add(Formatting.UNDERLINE + "Targeted Entity");
                    debug.add(String.valueOf(Registries.ENTITY_TYPE.getId(entity.getType())));
                }
            }
            // Return ArrayList of Text
            info.setReturnValue(debug);
            // Checks if Values should be offset
        } else {
            // Grab offsets from main method
            int offsetX = ConfigAPI.getOffsetX();
            int offsetY = ConfigAPI.getOffsetY();
            int offsetZ = ConfigAPI.getOffsetZ();
            // Get client
            MinecraftClient client = MinecraftClient.getInstance();
            // Gets ArrayList of Text
            List<String> debug = info.getReturnValue();

            // Checks if Client doesn't use reduced Debug
            if (!client.hasReducedDebugInfo()) {
                // Cuts all parts with coords out
                debug = new ArrayList<>(debug.subList(0, 10));
                BlockPos blockPos;

                // If looking at a Block change the Coordinates of the Block and add it
                if (this.blockHit.getType() == HitResult.Type.BLOCK) {
                    blockPos = ((BlockHitResult)this.blockHit).getBlockPos();
                    BlockState blockState = client.world.getBlockState(blockPos);
                    debug.add("");
                    debug.add(Formatting.UNDERLINE + "Targeted Block: " + (blockPos.getX() + offsetX) + ", " + (blockPos.getY() + offsetY) + ", " + (blockPos.getZ() + offsetZ));
                    debug.add(String.valueOf(Registries.BLOCK.getId(blockState.getBlock())));
                    for (Map.Entry entry : blockState.getEntries().entrySet()) {
                        debug.add(propertyToString(entry));
                    }
                    blockState.streamTags().map(tag -> "#" + tag.id()).forEach(debug::add);
                }

                // If looking at a Fluid change the Coordinates of the Fluid and add it
                if (this.fluidHit.getType() == HitResult.Type.BLOCK) {
                    blockPos = ((BlockHitResult)this.fluidHit).getBlockPos();
                    FluidState fluidState = client.world.getFluidState(blockPos);
                    debug.add("");
                    debug.add(Formatting.UNDERLINE + "Targeted Fluid: " + (blockPos.getX() + offsetX) + ", " + (blockPos.getY() + offsetY) + ", " + (blockPos.getZ()+ offsetZ));
                    debug.add(String.valueOf(Registries.FLUID.getId(fluidState.getFluid())));
                    for (Map.Entry entry : fluidState.getEntries().entrySet()) {
                        debug.add(this.propertyToString(entry));
                    }
                    fluidState.streamTags().map(tag -> "#" + tag.id()).forEach(debug::add);
                }

                // If looking at an Entity add it
                Entity entity;
                if ((entity = client.targetedEntity) != null) {
                    debug.add("");
                    debug.add(Formatting.UNDERLINE + "Targeted Entity");
                    debug.add(String.valueOf(Registries.ENTITY_TYPE.getId(entity.getType())));
                }
            }
            // Returns the ArrayList of Text
            info.setReturnValue(debug);
        }
    }

    @Inject(at = @At("RETURN"), method = "getLeftText", cancellable = true)
    protected void getLeftTextMixin(@NotNull CallbackInfoReturnable<List<String>> info) {
        // Checks if values should be offset or relative
        if(ConfigAPI.getMode() == Mode.CUSTOM) {
            // defines player positions while opening inventory
            x = RelativeF3Coords.getOldX();
            y = RelativeF3Coords.getOldY();
            z = RelativeF3Coords.getOldZ();
            blockX = RelativeF3Coords.getOldBlockX();
            blockY = RelativeF3Coords.getOldBlockY();
            blockZ = RelativeF3Coords.getOldBlockZ();

            // Gets client
            MinecraftClient client = MinecraftClient.getInstance();
            // Gets blockPos and modifies the coords
            BlockPos blockPos = client.getCameraEntity().getBlockPos();
            blockPos = new BlockPos(blockPos.getX() - blockX, blockPos.getY() - blockY, blockPos.getZ() - blockZ);
            // Gets chunkPos with modified blockPos
            ChunkPos chunkPos = new ChunkPos(blockPos);

            // Define Strings with replaced coords
            String XYZ = String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", client.getCameraEntity().getX() - x, client.getCameraEntity().getY() - y, client.getCameraEntity().getZ() - z);
            String BlockPos = String.format(Locale.ROOT, "Block: %d %d %d [%d %d %d]", blockPos.getX(), blockPos.getY(), blockPos.getZ(), (blockPos.getX()) & 0xF, (blockPos.getY()) & 0xF, (blockPos.getZ()) & 0xF);
            String ChunkPos = String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", chunkPos.x, ChunkSectionPos.getSectionCoord(blockPos.getY()), chunkPos.z, chunkPos.getRegionRelativeX(), chunkPos.getRegionRelativeZ(), chunkPos.getRegionX(), chunkPos.getRegionZ());

            // Gets the ArrayList of Text
            List<String> debug = info.getReturnValue();

            // Checks if the start of the coords section is 9 or 10
            if (this.getServerWorldDebugString() == null) {
                // replace strings with the modified strings from earlier
                debug.set(9, XYZ);
                debug.set(10, BlockPos);
                debug.set(10, ChunkPos);
            } else {
                // replace strings with the modified strings from earlier
                debug.set(10, XYZ);
                debug.set(11, BlockPos);
                debug.set(12, ChunkPos);
            }

            // Returns ArrayList of Text
            info.setReturnValue(debug);
            // checks if an offset should be applied
        } else {
            // Grabs the offsets
            double offsetX = ConfigAPI.getOffsetX();
            double offsetY = ConfigAPI.getOffsetY();
            double offsetZ = ConfigAPI.getOffsetZ();

            // Gets client
            MinecraftClient client = MinecraftClient.getInstance();
            // Gets blockPos and modified it
            BlockPos blockPos = client.getCameraEntity().getBlockPos();
            blockPos = blockPos.add((int) Math.round(offsetX+0.5), (int) Math.round(offsetY+0.5), (int) Math.round(offsetZ+0.5));
            // Gets chunkPos with modified blockPos
            ChunkPos chunkPos = new ChunkPos(blockPos);

            // Define strings with replaced coords
            String XYZ = String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", client.getCameraEntity().getX() + offsetX, client.getCameraEntity().getY() + offsetY, client.getCameraEntity().getZ() + offsetZ);
            String BlockPos = String.format(Locale.ROOT, "Block: %d %d %d [%d %d %d]", blockPos.getX(), blockPos.getY(), blockPos.getZ(), (blockPos.getX()) & 0xF, (blockPos.getY()) & 0xF, (blockPos.getZ()) & 0xF);
            String ChunkPos = String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", chunkPos.x, ChunkSectionPos.getSectionCoord(blockPos.getY()), chunkPos.z, chunkPos.getRegionRelativeX(), chunkPos.getRegionRelativeZ(), chunkPos.getRegionX(), chunkPos.getRegionZ());

            // Gets the ArrayList of Text
            List<String> debug = info.getReturnValue();

            // Checks if the start of the coords section is 9 or 10
            if (this.getServerWorldDebugString() == null) {
                // replace strings with the modified strings from earlier
                debug.set(9, XYZ);
                debug.set(10, BlockPos);
                debug.set(10, ChunkPos);
            } else {
                // replace strings with the modified strings from earlier
                debug.set(10, XYZ);
                debug.set(11, BlockPos);
                debug.set(12, ChunkPos);
            }

            // Returns the ArrayList of Text
            info.setReturnValue(debug);
        }
    }
}
