package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.base.helpers.LangHelpers;
import com.rabbitminers.extendedbogeys.base.types.BogeySizeBlockSet;
import com.rabbitminers.extendedbogeys.bogeys.unlinked.UnlinkedBogeyBlock;
import com.rabbitminers.extendedbogeys.bogeys.unlinked.UnlinkedBogeyCarriageMovementBehaviour;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.StandardBogeyBlock;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;

public class ExtendedBogeysBlocks {
	public static final CreateRegistrate REGISTRATE = ExtendedBogeys.registrate();
	public static final BogeySizeBlockSet<UnlinkedBogeyBlock> UNLINKED_BOGEYS = new BogeySizeBlockSet<>(size ->
		REGISTRATE.block(size.id() + "_unlinked_bogey", p -> new UnlinkedBogeyBlock(p, size.size))
				.properties(p -> p.color(MaterialColor.PODZOL))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(BlockBehaviour.Properties::noOcclusion)
				.transform(TagGen.pickaxeOnly())
				.blockstate((c, p) -> BlockStateGen.horizontalAxisBlock(c, p, s -> p.models()
						.getExistingFile(p.modLoc("block/track/bogey/unlinked_top"))))
				.loot((p, l) -> p.dropOther(l, AllBlocks.RAILWAY_CASING.get()))
				.onRegister(movementBehaviour(new UnlinkedBogeyCarriageMovementBehaviour()))
				.register());

	public static final BogeySizeBlockSet<StandardBogeyBlock> STANDARD_BOGEYS = new BogeySizeBlockSet<>(size -> {
		if (!size.inBuilt)
			return REGISTRATE.block(size.id() + "_bogey", p -> new StandardBogeyBlock(p, size.size))
					.properties(p -> p.color(MaterialColor.PODZOL))
					.transform(BuilderTransformers.bogey())
					.register();
		else return size.size == BogeySizes.LARGE ? AllBlocks.LARGE_BOGEY : AllBlocks.SMALL_BOGEY;
	});

	public static final DyedBlockList<CasingBlock> PAINTED_RAILWAY_CASING = new DyedBlockList<>(color ->
			REGISTRATE.block(color.getName() + "_railway_casing", CasingBlock::new)
					.transform(BuilderTransformers.layeredCasing(() -> AllSpriteShifts.RAILWAY_CASING_SIDE,
							() -> AllSpriteShifts.RAILWAY_CASING))
					.properties(p -> p.color(MaterialColor.TERRACOTTA_CYAN))
					.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
					.lang(LangHelpers.capitalize(color.getName()) + " Train Casing")
					.register());

	public static void register() {
		ExtendedBogeys.LOGGER.info("Registering blocks for " + ExtendedBogeys.MOD_NAME);
	}
}
