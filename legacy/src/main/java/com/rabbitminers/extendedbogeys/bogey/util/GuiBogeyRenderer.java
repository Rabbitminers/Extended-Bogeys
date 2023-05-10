package com.rabbitminers.extendedbogeys.bogey.util;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.gui.element.GuiGameElement;

public class GuiBogeyRenderer {
    public static void renderPartialModelInGUI(PartialModel partialModel) {
        GuiGameElement.of(partialModel);
    }
}
