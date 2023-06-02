package com.rabbitminers.extendedbogeys.fabric;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import net.fabricmc.api.ModInitializer;

public class ExtendedBogeysImpl implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtendedBogeys.init();
        ExtendedBogeys.registrate().register();
    }
}
