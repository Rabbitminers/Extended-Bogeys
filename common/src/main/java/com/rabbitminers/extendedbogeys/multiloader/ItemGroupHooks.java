package com.rabbitminers.extendedbogeys.multiloader;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ItemGroupHooks {
    @ExpectPlatform
    public static int getNextAvailableTabId() {
        throw new AssertionError();
    }
}
