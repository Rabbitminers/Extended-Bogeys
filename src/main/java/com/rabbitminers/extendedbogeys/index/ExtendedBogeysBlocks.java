package com.rabbitminers.extendedbogeys.index;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogey.sizes.CustomSizeBogeyBlock;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedBogeyCarriageMovementBehaviour;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.bogey.util.BogeyBlockList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;

import java.util.List;

import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.Create.REGISTRATE;

public class ExtendedBogeysBlocks {
    private static final CreateRegistrate REGISTRATE = ExtendedBogeys.registrate();

    // Extra Bogeys

    public static final BogeyBlockList<? extends StandardBogeyBlock> CUSTOM_BOGEYS = new BogeyBlockList<>(size -> {
       if (!size.isDefault()) { // Don't re-register existing bogey blocks
           return REGISTRATE.block(size.getName() + "_bogey", p -> new CustomSizeBogeyBlock(p, size))
                   .properties(p -> p.color(MaterialColor.PODZOL))
                   .transform(BuilderTransformers.bogey())
                   .register();
       }
       return null;
    });

    public static final BogeyBlockList<? extends UnlinkedStandardBogeyBlock> UNLINKED_BOGEYS = new BogeyBlockList<>(size -> REGISTRATE.block(size.getName() + "_unlinked_bogey", p -> new UnlinkedStandardBogeyBlock(p, false))
            .properties(p -> p.color(MaterialColor.PODZOL))
            .properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
            .properties(p -> p.noOcclusion())
            .transform(TagGen.pickaxeOnly())
            .blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, s -> p.models()
                    .getExistingFile(p.modLoc("block/track/bogey/unlinked_top"))))
            .loot((p, l) -> p.dropOther(l, AllBlocks.RAILWAY_CASING.get()))
            .onRegister(movementBehaviour(new UnlinkedBogeyCarriageMovementBehaviour()))
            .register());

    public static void register() {}
}
