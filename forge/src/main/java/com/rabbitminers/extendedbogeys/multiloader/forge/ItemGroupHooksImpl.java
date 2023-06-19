package com.rabbitminers.extendedbogeys.multiloader.forge;

import net.minecraft.world.item.CreativeModeTab;

public class ItemGroupHooksImpl {
    public static int getNextAvailableTabId() {
        return CreativeModeTab.getGroupCountSafe();
    }
}
