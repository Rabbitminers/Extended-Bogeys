package com.rabbitminers.extendedbogeys.config;

import com.rabbitminers.extendedbogeys.config.client.ExtendedBogeysClientConfig;
import com.rabbitminers.extendedbogeys.config.common.ExtendedBogeysCommonConfig;
import com.rabbitminers.extendedbogeys.config.server.ExtendedBogeysServerConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtendedBogeysConfig {
    private static final Map<ModConfig.Type, ExtendedBogeysConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);
    public static ExtendedBogeysClientConfig CLIENT;
    public static ExtendedBogeysCommonConfig COMMON;
    public static ExtendedBogeysServerConfig SERVER;

    public static ExtendedBogeysConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }
    private static <T extends ExtendedBogeysConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }
    public static void register(ModLoadingContext context) {
        CLIENT = register(ExtendedBogeysClientConfig::new, ModConfig.Type.CLIENT);
        COMMON = register(ExtendedBogeysCommonConfig::new, ModConfig.Type.COMMON);
        SERVER = register(ExtendedBogeysServerConfig::new, ModConfig.Type.SERVER);

        for (Map.Entry<ModConfig.Type, ExtendedBogeysConfigBase> pair : CONFIGS.entrySet())
            context.registerConfig(pair.getKey(), pair.getValue().specification);
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        for (ExtendedBogeysConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onLoad();
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        for (ExtendedBogeysConfigBase config : CONFIGS.values())
            if (config.specification == event.getConfig()
                    .getSpec())
                config.onReload();
    }
}
