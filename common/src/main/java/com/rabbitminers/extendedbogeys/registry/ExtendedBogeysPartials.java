package com.rabbitminers.extendedbogeys.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;

public class ExtendedBogeysPartials {
    public static final PartialModel

            // Single Axle

            // Small (2-0-0 / 0-0-2)
            SMALL_SINGLE_AXLE_FRAME = block("singleaxle/small/single_axel_leading_truck_frame"),

            SMALL_SINGLE_AXLE_PIN = block("singleaxle/small/single_axel_leading_truck_pin"),

            // Double Axle

            // Small (4-0-0 / 0-0-4)
            SMALL_DOUBLE_AXLE_FRAME = block("doubleaxle/small/frame"),
            SMALL_DOUBLE_AXLE_PIN = block("doubleaxle/small/pin"),

            // Large (0-4-0)
            LARGE_DOUBLE_AXLE_DRIVER_ROD = block("doubleaxle/large/driverod"),
            LARGE_DOUBLE_AXLE_DRIVER_PIN = block("doubleaxle/large/drivepin"),
            LARGE_DOUBLE_AXLE_DRIVER_CONNECTING_ROD = block("doubleaxle/large/connectingrod"),
            LARGE_DOUBLE_AXLE_DRIVER_FRAME = block("doubleaxle/large/frame"),

            // Triple Axle

            // Large (0-6-0)
            LARGE_TRIPLE_AXLE_DRIVER_FRAME = block("tripleaxle/large/frame"),
            LARGE_TRIPLE_AXLE_DRIVER_CONNECTING_ROD = block("tripleaxle/large/connectingrod"),
            LARGE_TRIPLE_AXLE_DRIVER_PIN = block("tripleaxle/large/drivepin"),
            LARGE_TRIPLE_AXLE_DRIVER_ROD = block("tripleaxle/large/driverod"),
            LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC = block("tripleaxle/large/eccentric"),
            LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC_ROD = block("tripleaxle/large/eccentricrod"),
            LARGE_TRIPLE_AXLE_DRIVER_RADIUSROD = block("tripleaxle/large/radiusrod")

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
