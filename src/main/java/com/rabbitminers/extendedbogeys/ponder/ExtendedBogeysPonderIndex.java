package com.rabbitminers.extendedbogeys.ponder;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.ponder.scenes.BogeyStyleScenes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.content.PonderIndex;

public class ExtendedBogeysPonderIndex {

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(ExtendedBogeys.MODID);

    public static void register() {
        HELPER.forComponents(AllBlocks.TRACK_STATION)
                .addStoryBoard("styling", BogeyStyleScenes::styling);
    }
}
