package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

public class ExtendedBogeysBlocks {
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ExtendedBogeys.MOD_ID);

	public static void init() {
		// load the class and register everything
		ExtendedBogeys.LOGGER.info("Registering blocks for " + ExtendedBogeys.NAME);
	}
}
