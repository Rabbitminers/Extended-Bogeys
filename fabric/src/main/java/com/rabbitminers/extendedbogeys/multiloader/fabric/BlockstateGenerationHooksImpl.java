package com.rabbitminers.extendedbogeys.multiloader.fabric;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;

public class BlockstateGenerationHooksImpl {
    public static <T extends  Block> void unlinkedTop(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider) {
        BlockStateGen.horizontalAxisBlock(context, provider, s -> provider.models()
            .getExistingFile(provider.modLoc("block/track/bogey/unlinked_top")));
    }
}
