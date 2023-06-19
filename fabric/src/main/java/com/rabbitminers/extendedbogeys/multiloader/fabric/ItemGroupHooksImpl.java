package com.rabbitminers.extendedbogeys.multiloader.fabric;

import io.github.fabricators_of_create.porting_lib.util.ItemGroupUtil;

public class ItemGroupHooksImpl {
    public static int getNextAvailableTabId() {
        return ItemGroupUtil.expandArrayAndGetId();
    }
}
