package com.rabbitminers.extendedbogeys.index;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedStandardBogeyBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;

public class ExtendedBogeysBlocks {
    private static final CreateRegistrate REGISTRATE = ExtendedBogeys.registrate();

    public static final BlockEntry<UnlinkedStandardBogeyBlock> SMALL_UNLINKED_BOGEY =
            REGISTRATE.block("small_unlinked_bogey", p -> new UnlinkedStandardBogeyBlock(p, false))
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(p -> p.noOcclusion())
                    .transform(AllTags.pickaxeOnly())
                    .blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, s -> p.models()
                            .getExistingFile(p.modLoc("block/track/bogey/top"))))
                    .loot((p, l) -> p.dropOther(l, AllBlocks.RAILWAY_CASING.get()))
                    .register();

    public static final BlockEntry<UnlinkedStandardBogeyBlock> LARGE_UNLINKED_BOGEY =
            REGISTRATE.block("large_unlinked_bogey", p -> new UnlinkedStandardBogeyBlock(p, true))
                    .properties(p -> p.color(MaterialColor.PODZOL))
                    .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
                    .properties(p -> p.noOcclusion())
                    .transform(AllTags.pickaxeOnly())
                    .blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, s -> p.models()
                            .getExistingFile(p.modLoc("block/track/bogey/top"))))
                    .loot((p, l) -> p.dropOther(l, AllBlocks.RAILWAY_CASING.get()))
                    .register();

    public static void register() {}
}
