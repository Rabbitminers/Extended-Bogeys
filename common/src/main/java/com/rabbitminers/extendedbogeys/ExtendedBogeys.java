package com.rabbitminers.extendedbogeys;

import com.rabbitminers.extendedbogeys.data.ExtendedBogeysLanguageProvider;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBogeySizes;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBogeyStyles;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.LangMerger;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtendedBogeys {
    public static final String MOD_ID = "extendedbogeys";
    public static final String MOD_NAME = "Extended Bogeys";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ExtendedBogeys.MOD_ID)
            .creativeModeTab(() -> ExtendedBogeysItems.itemGroup);

    public static void init() {
        ExtendedBogeysBlocks.register();
        ExtendedBogeysBogeyStyles.register();
        ExtendedBogeysBogeySizes.register();
    }

    public static void gatherData(DataGenerator gen) {
        gen.addProvider(new LangMerger(gen, MOD_ID, "Extended Bogeys",
                ExtendedBogeysLanguageProvider.values()));
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
