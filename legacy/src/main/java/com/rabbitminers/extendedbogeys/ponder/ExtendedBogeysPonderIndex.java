package com.rabbitminers.extendedbogeys.ponder;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.ponder.scenes.BogeyStyleScenes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;

public class ExtendedBogeysPonderIndex {

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(ExtendedBogeys.MODID);

    public static void register() {
        HELPER.forComponents(AllBlocks.RAILWAY_CASING)
                .addStoryBoard("styling", BogeyStyleScenes::styling);
    }
}
