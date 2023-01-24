package com.rabbitminers.extendedbogeys;

import com.mojang.logging.LogUtils;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.SingleAxisBogey;
import com.rabbitminers.extendedbogeys.bogey.styles.ThreeWheelBogey;
import com.rabbitminers.extendedbogeys.bogey.styles.TwoWheelBogey;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtendedBogeys.MODID)
public class ExtendedBogeys {
    public static final String MODID = "extendedbogeys";
    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(ExtendedBogeys.MODID);
    public static final Logger LOGGER = LogUtils.getLogger();

    public ExtendedBogeys() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> BogeyPartials::init);
        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
    private void setup(final FMLCommonSetupEvent event) {
        BogeyStyles.addBogeyStyle(TwoWheelBogey.class);
        BogeyStyles.addBogeyStyle(ThreeWheelBogey.class);
        BogeyStyles.addBogeyStyle(SingleAxisBogey.class);
        LOGGER.info("Registered bogey types from: " + ExtendedBogeys.MODID);
    }

    public static CreateRegistrate registrate() {
        return registrate.get();
    }
}
