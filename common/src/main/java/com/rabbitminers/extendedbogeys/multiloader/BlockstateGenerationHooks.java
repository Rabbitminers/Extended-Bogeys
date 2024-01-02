package com.rabbitminers.extendedbogeys.multiloader;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;

public class BlockstateGenerationHooks {

    @ExpectPlatform
    public static <T extends Block> void unlinkedTop(DataGenContext<Block, T> context, RegistrateBlockstateProvider provider) {
        // Calling this directly in common seems to cause porting lib to be loaded at least in dev which causes
        // a crash on forge so although the implementations are identical they are seperated to avoid this crash
        throw new AssertionError();
    }
}
