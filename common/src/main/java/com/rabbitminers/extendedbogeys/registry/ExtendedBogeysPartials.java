package com.rabbitminers.extendedbogeys.registry;

import com.jozufozu.flywheel.core.PartialModel;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.trains.bogey.StandardBogeyRenderer;

import java.util.EnumMap;
import java.util.Map;

public class ExtendedBogeysPartials {
    public static final PartialModel

    // Single Axle

    // Small (2-0-0 / 0-0-2)

    SMALL_SINGLE_AXLE_FRAME = block("singleaxle/small/single_axel_leading_truck_frame"),

    SMALL_SINGLE_AXLE_PIN = block("singleaxle/small/single_axel_leading_truck_pin"),

    // Double Axle

    // Small (2-0-0 / 0-0-2)
    SMALL_DOUBLE_AXLE_FRAME = block("doubleaxle/small/frame"),
    SMALL_DOUBLE_AXLE_PIN = block("doubleaxle/small/pin"),

    // Large (0-4-0)
    LARGE_DOUBLE_AXLE_DRIVER_ROD = block("doubleaxle/large/driverod"),
            LARGE_DOUBLE_AXLE_DRIVER_PIN = block("doubleaxle/large/drivepin"),
            LARGE_DOUBLE_AXLE_DRIVER_CONNECTING_ROD = block("doubleaxle/large/connectingrod"),

    // Triple Axle

    // Large (0-6-0)
    LARGE_TRIPLE_AXLE_DRIVER_CONNECTING_ROD = block("tripleaxle/large/connectingrod"),
            LARGE_TRIPLE_AXLE_DRIVER_PIN = block("tripleaxle/large/drivepin"),
            LARGE_TRIPLE_AXLE_DRIVER_ROD = block("tripleaxle/large/driverod"),
            LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC = block("tripleaxle/large/eccentric"),
            LARGE_TRIPLE_AXLE_DRIVER_ECCENTRIC_ROD = block("tripleaxle/large/eccentricrod"),
            LARGE_TRIPLE_AXLE_DRIVER_RADIUSROD = block("tripleaxle/large/radiusrod")
    ;

    public static final Map<BogeyPaintColour, PartialModel>
            // Overwrites of existing models

            SMALL_WHEELS = new EnumMap<>(BogeyPaintColour.class),
            LARGE_WHEELS = new EnumMap<>(BogeyPaintColour.class),
            BOGEY_DRIVES = new EnumMap<>(BogeyPaintColour.class),
            BOGEY_FRAMES = new EnumMap<>(BogeyPaintColour.class),

            // Large (0-4-0)

            LARGE_DOUBLE_AXLE_DRIVER_FRAMES = dyed("doubleaxle/large/frame"),

            // Triple Axle

            // Large (0-6-0)
            LARGE_TRIPLE_AXLE_DRIVER_FRAMES = dyed("tripleaxle/large/frame")
    ;

    static {
        for (BogeyPaintColour colour : BogeyPaintColour.values()) {
            if (colour == BogeyPaintColour.UNPAINTED) {
                SMALL_WHEELS.put(BogeyPaintColour.UNPAINTED, AllPartialModels.SMALL_BOGEY_WHEELS);
                LARGE_WHEELS.put(BogeyPaintColour.UNPAINTED, AllPartialModels.LARGE_BOGEY_WHEELS);
                BOGEY_DRIVES.put(BogeyPaintColour.UNPAINTED, AllPartialModels.BOGEY_DRIVE);
                BOGEY_FRAMES.put(BogeyPaintColour.UNPAINTED, AllPartialModels.BOGEY_FRAME);
            } else {
                SMALL_WHEELS.put(colour, dyed(colour, "/bogey/wheel"));
                LARGE_WHEELS.put(colour, dyed(colour,"/bogey/drive_wheel"));
                BOGEY_DRIVES.put(colour, dyed(colour, "/bogey/bogey_drive"));
                BOGEY_FRAMES.put(colour, dyed(colour, "/bogey/bogey_frame"));
            }
        }
    }

    private static Map<BogeyPaintColour, PartialModel> dyed(String path) {
        Map<BogeyPaintColour, PartialModel> MAP = new EnumMap<>(BogeyPaintColour.class);
        for(BogeyPaintColour colour : BogeyPaintColour.values())
            MAP.put(colour, dyed(colour, path));
        return MAP;
    }

    private static PartialModel dyed(BogeyPaintColour colour, String path) {
        return new PartialModel(ExtendedBogeys.asResource(colour.name + "/block/" + path));
    }

    private static PartialModel block(String path) {
        return new PartialModel(ExtendedBogeys.asResource("unpainted/block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(ExtendedBogeys.asResource("entity/" + path));
    }



    public static void init() {

    }
}
