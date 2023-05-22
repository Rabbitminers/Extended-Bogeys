package com.rabbitminers.extendedbogeys.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;

public class ExtendedBogeysPartials {
    public static final PartialModel
            SINGLE_AXEL_LEADING_TRUCK_FRAME = block("singleaxle/small/single_axel_leading_truck_frame"),
            SINGLE_AXEL_LEADING_TRUCK_PIN = block("singleaxle/small/single_axel_leading_truck_pin")
    ;


    private static PartialModel block(String path) {
        return new PartialModel(ExtendedBogeys.asResource("block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(ExtendedBogeys.asResource("entity/" + path));
    }

    public static void init() {

    }
}
