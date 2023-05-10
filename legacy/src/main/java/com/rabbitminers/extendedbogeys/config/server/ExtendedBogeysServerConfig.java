package com.rabbitminers.extendedbogeys.config.server;

import com.rabbitminers.extendedbogeys.config.ExtendedBogeysConfigBase;

public class ExtendedBogeysServerConfig extends ExtendedBogeysConfigBase {
    @Override
    public String getName() {
        return "server";
    }

    public final ConfigGroup client = group(0, "server",
            Comments.server);

    public final ConfigBool trainsRequireFuel = b(false, "trainsRequireFuel",
            Comments.trainsRequireFuel);
    public final ConfigBool trainsConsumeWater = b(false, "trainsConsumeWater",
            Comments.trainsConsumeWater);
    public final ConfigBool shouldApplyMaximumSpeed = b(false, "enableBogeySpeedLimitations",
            Comments.maximumSpeed);
    public final ConfigBool shouldDerailTrainsWhenGoingOverShortCurve = b(false, "derailTrainOnCurve",
            Comments.minimumTurn);
    public final ConfigBool shouldApplyAccellerationModification = b(false, "modifyAccelleration",
            Comments.modifyAccelleration);

    private static class Comments {
        static String server = "Server-only settings";
        static String trainsRequireFuel = "With this enabled your train must have fuel to run";
        static String trainsConsumeWater = "Water and fuel will be required to achieve the acellerated speed, this can be combined with trains requiring fuel to need both for your train to start";
        static String maximumSpeed = "Some bogey styles come with optional speed limitations, toggle this to apply them";
        static String minimumTurn = "Longer bogeys may clip tracks on shorter turns, toggle this to derail trains that have bogeys that are too wide for the turn";
        static String modifyAccelleration = "Some bogeys may modify their trains accelleration, this can make your train reach its maximum speed faster or slower making different bogeys more useful";
    }
}
