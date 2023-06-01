package com.rabbitminers.extendedbogeys.multiloader.forge;

import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;

public class CarriageContraptionHelperImpl {
    // Why do we need this? I have no clue, for some reason doing this in common causes forge specific imports to be loaded?
    public static double getDistanceTo(CarriageContraptionEntity cce) {
        if (cce instanceof ICarriageContraptionEntity carriageContraptionEntity)
            return carriageContraptionEntity.getDistanceTo();
        return 0;
    }
}
