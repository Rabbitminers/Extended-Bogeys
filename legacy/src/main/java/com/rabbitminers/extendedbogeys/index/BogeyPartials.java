package com.rabbitminers.extendedbogeys.index;

import com.jozufozu.flywheel.core.PartialModel;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;

public class BogeyPartials {
    public static final PartialModel
    SINGLE_AXEL_LEADING_TRUCK_FRAME = block("single/single_axel_leading_truck_frame"),
    SINGLE_AXEL_LEADING_TRUCK_PIN = block("single/single_axel_leading_truck_pin"),
    TWO_WHEEL_BOGEY_ROD = block("twowheelbogeyrod"),
    THREE_WHEEL_BOGEY_ROD = block("threewheelbogeyrod"),

    /* 0-4-0 LARGE */
    doubleaxle_DRIVE_ROD = block("doubleaxle/large/driverod"),
    doubleaxle_DRIVE_PIN = block("doubleaxle/large/drivepin"),
    doubleaxle_CONNECTING_ROD = block("doubleaxle/large/connectingrod"),
    doubleaxle_DRIVE_FRAME = block("doubleaxle/large/frame"),

    /* 4-0-0 / 0-0-4 SMALL */
    SMALL_doubleaxle_FRAME = block("doubleaxle/small/frame"),
    SMALL_doubleaxle_PIN = block("doubleaxle/small/pin"),

    /* 0-6-0 LARGE */
    SIX_WHEEL_FRAME = block("tripleaxle/large/frame"),
    SIX_WHEEL_CONNECTING_ROD = block("tripleaxle/large/connectingrod"),
    SIX_WHEEL_DRIVE_PIN = block("tripleaxle/large/drivepin"),
    SIX_WHEEL_DRIVE_ROD = block("tripleaxle/large/driverod"),
    SIX_WHEEL_ECCENTRIC = block("tripleaxle/large/eccentric"),
    SIX_WHEEL_ECCENTRIC_ROD = block("tripleaxle/large/eccentricrod"),
    SIX_WHEEL_RADIUSROD = block("tripleaxle/large/radiusrod")

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
