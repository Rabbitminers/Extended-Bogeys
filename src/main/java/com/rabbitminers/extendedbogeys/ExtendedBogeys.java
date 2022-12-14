package com.rabbitminers.extendedbogeys;

import com.mojang.logging.LogUtils;
import com.rabbitminers.extendedbogeys.index.AddonBlocks;
import com.rabbitminers.extendedbogeys.index.AddonItems;
import com.rabbitminers.extendedbogeys.index.AddonTileEntities;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExtendedBogeys.MODID)
public class ExtendedBogeys {
    public static final String MODID = "extendedbogeys";
    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(ExtendedBogeys.MODID);
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // TODO: Add new icon for your mod's item group
    public static final CreativeModeTab itemGroup = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(AllBlocks.FLYWHEEL.get());
        }
    };

    public ExtendedBogeys()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        /*
         For adding simple kinetic blocks this is all you need but for fluids etc.
         see the Create GitHub repo -
         https://github.com/Creators-of-Create/Create/tree/mc1.18/dev/src/main/java/com/simibubi/create
         */

        AddonBlocks.register();
        AddonItems.register(eventBus);
        AddonTileEntities.register();
    }

    private void setup(final FMLCommonSetupEvent event) {}

    public static CreateRegistrate registrate() {
        return registrate.get();
    }
}
