package com.rabbitminers.extendedbogeys.fabric;

import com.rabbitminers.extendedbogeys.ExtendedBogeysClient;
import net.fabricmc.api.ClientModInitializer;

public class ExtendedBogeysClientImpl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtendedBogeysClient.init();
    }
}
