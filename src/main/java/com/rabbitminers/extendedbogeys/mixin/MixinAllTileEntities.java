package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.simibubi.create.AllTileEntities;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

/*
 * Re-used / Modified from Create Steam n Rails
 * https://github.com/Layers-of-Railways/Railway/blob/HEAD/src/main/java/com/railwayteam/railways/mixin/MixinAllTileEntities.java
 */

@Mixin(value = AllTileEntities.class, remap = false)
public class MixinAllTileEntities {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/tterrag/registrate/builders/BlockEntityBuilder;validBlocks([Lcom/tterrag/registrate/util/nullness/NonNullSupplier;)Lcom/tterrag/registrate/builders/BlockEntityBuilder;"
        ),
        slice = @Slice(
            from = @At(value = "CONSTANT", args = "stringValue=fake_track"),
            to = @At(value = "CONSTANT", args = "stringValue=track_station")
        )
    )
    private static BlockEntityBuilder<?, ?> addCustomBogeysToTileEntity(BlockEntityBuilder<?, ?> instance, NonNullSupplier<? extends Block>[] blocks) {
        for (BlockEntry<? extends Block> customBogey : ExtendedBogeysBlocks.CUSTOM_BOGEYS) {
            if (customBogey != null)
                instance.validBlock(customBogey);
        }
        return instance.validBlocks(blocks);
    }
}
