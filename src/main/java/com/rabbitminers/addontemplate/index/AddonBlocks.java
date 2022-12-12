package com.rabbitminers.addontemplate.index;

import com.rabbitminers.addontemplate.AddonTemplate;
import com.simibubi.create.foundation.data.CreateRegistrate;

public class AddonBlocks {
    private static final CreateRegistrate REGISTRATE = AddonTemplate.registrate().creativeModeTab(
            () -> AddonTemplate.itemGroup
    );

    // See create git for how to register blocks
    // - https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/AllBlocks.java

    public static void register() {}
}
