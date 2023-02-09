package com.rabbitminers.extendedbogeys.index;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedBogeyTileEntity;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedBogeyTileEntityRenderer;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedStandardBogeyBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.logistics.trains.BogeyTileEntityRenderer;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import java.util.List;

public class ExtendedBogeysTileEntities {
    private static final CreateRegistrate REGISTRATE = ExtendedBogeys.registrate();
    public static final BlockEntityEntry<UnlinkedBogeyTileEntity> UNLINKED_BOGEY_TILE_ENTITY = REGISTRATE
            .tileEntity("unlinked_bogey", UnlinkedBogeyTileEntity::new)
            .renderer(() -> UnlinkedBogeyTileEntityRenderer::new)
            .validBlocks(ExtendedBogeysBlocks.UNLINKED_BOGEYS.toArray())
            .register();
    public static void register() {}
}

