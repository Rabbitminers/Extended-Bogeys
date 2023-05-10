package com.rabbitminers.extendedbogeys.index;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedBogeyTileEntity;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedBogeyTileEntityRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ExtendedBogeysTileEntities {
    private static final CreateRegistrate REGISTRATE = ExtendedBogeys.registrate();
    public static final BlockEntityEntry<UnlinkedBogeyTileEntity> UNLINKED_BOGEY_TILE_ENTITY = REGISTRATE
            .tileEntity("unlinked_bogey", UnlinkedBogeyTileEntity::new)
            .renderer(() -> UnlinkedBogeyTileEntityRenderer::new)
            .validBlocks(ExtendedBogeysBlocks.UNLINKED_BOGEYS.toArray())
            .register();
    public static void register() {}
}

