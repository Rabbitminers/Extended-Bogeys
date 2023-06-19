package com.rabbitminers.extendedbogeys.registry;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.rabbitminers.extendedbogeys.multiloader.ItemGroupHooks.getNextAvailableTabId;

public class ExtendedBogeysItems {

    public static final CreativeModeTab itemGroup = new CreativeModeTab(getNextAvailableTabId(), ExtendedBogeys.MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return ExtendedBogeysBlocks.PAINTED_RAILWAY_CASING.get(DyeColor.RED).asStack();
        }
    };

    public static void init() {

    }
}
