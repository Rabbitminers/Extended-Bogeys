package com.rabbitminers.addontemplate.index;

import com.rabbitminers.addontemplate.AddonTemplate;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraftforge.eventbus.api.IEventBus;

public class AddonItems {
    private static final CreateRegistrate REGISTRATE = AddonTemplate.registrate()
            .creativeModeTab(() -> AddonTemplate.itemGroup);

    // See create git for how to register items
    // - https://github.com/Creators-of-Create/Create/blob/mc1.18/dev/src/main/java/com/simibubi/create/AllItems.java
    public static void register(IEventBus eventBus) {

    }
}
