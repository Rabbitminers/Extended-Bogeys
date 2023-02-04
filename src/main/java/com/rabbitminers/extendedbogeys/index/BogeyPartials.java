package com.rabbitminers.extendedbogeys.index;

import com.jozufozu.flywheel.core.PartialModel;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.simibubi.create.AllBlockPartials;

public class BogeyPartials {
    public static final PartialModel
    SINGLE_AXEL_LEADING_TRUCK_FRAME = block("single/single_axel_leading_truck_frame"),
    SINGLE_AXEL_LEADING_TRUCK_PIN = block("single/single_axel_leading_truck_pin"),
    TWO_WHEEL_BOGEY_ROD = block("twowheelbogeyrod"),
    THREE_WHEEL_BOGEY_ROD = block("threewheelbogeyrod"),
    FOUR_WHEEL_DRIVE_ROD = block("four_wheel/large/driverod"),
    FOUR_WHEEL_DRIVE_PIN = block("four_wheel/large/drivepin"),
    FOUR_WHEEL_CONNECTING_ROD = block("four_wheel/large/connectingrod"),
    FOUR_WHEEL_DRIVE_FRAME = block("four_wheel/large/frame"),
    SMALL_FOUR_WHEEL_FRAME = block("four_wheel/small/frame"),
    SMALL_FOUR_WHEEL_PIN = block("four_wheel/small/pin")

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
