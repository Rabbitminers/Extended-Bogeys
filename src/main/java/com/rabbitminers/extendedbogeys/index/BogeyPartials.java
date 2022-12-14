package com.rabbitminers.extendedbogeys.index;

import com.jozufozu.flywheel.core.PartialModel;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.simibubi.create.AllBlockPartials;

public class BogeyPartials {
    public static final PartialModel
    TWO_WHEEL_BOGEY_ROD = block("twowheelbogeyrod"),
    THREE_WHEEL_BOGEY_ROD = block("threewheelbogeyrod")
    ;

    private static PartialModel block(String path) {
        return new PartialModel(ExtendedBogeys.asResource("block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(ExtendedBogeys.asResource("entity/" + path));
    }

    public static void init() {
        // init static fields
    }
}
