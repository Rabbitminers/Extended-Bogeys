package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.simibubi.create.AllBlockEntityTypes;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AllBlockEntityTypes.class)
public class MixinAllBlockEntityTypes {
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
        for (BlockEntry<? extends Block> customBogey : ExtendedBogeysBlocks.UNLINKED_BOGEYS) {
            if (customBogey != null)
                instance.validBlock(customBogey);
        }
        for (BlockEntry<? extends Block> customBogey : ExtendedBogeysBlocks.STANDARD_BOGEYS) {
            if (customBogey != null)
                instance.validBlock(customBogey);
        }
        return instance; // Don't add existing blocks to prevent double registry
    }
}
