package com.rabbitminers.extendedbogeys.registry;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import net.minecraft.world.item.DyeColor;

import java.util.EnumMap;
import java.util.Map;

public class ExtendedBogeysSpriteShifts {
    public static final Map<DyeColor, CTSpriteShiftEntry>
            PAINTED_RAILWAY_CASING = new EnumMap<>(DyeColor.class),
            PAINTED_RAILWAY_CASING_SIDE = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            PAINTED_RAILWAY_CASING.put(color, omni(color.getName() + "/block/railway_casing"));
            PAINTED_RAILWAY_CASING_SIDE.put(color, omni(color.getName() + "/block/railway_casing_side"));
        }
    }

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }
    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Create.asResource("block/" + blockTextureName),
                Create.asResource(connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}
