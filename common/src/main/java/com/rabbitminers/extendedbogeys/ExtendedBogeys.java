package com.rabbitminers.extendedbogeys;

import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBogeyStyles;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedBogeys {
    public static final String MOD_ID = "Extended Bogeys";
    public static final String NAME = "Extended Bogeys";
    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ExtendedBogeys.MOD_ID);

    public static void init() {
        ExtendedBogeysBlocks.register();
        ExtendedBogeysBogeyStyles.register();
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
