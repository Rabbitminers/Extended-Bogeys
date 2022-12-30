package com.rabbitminers.extendedbogeys.bogey;

import com.simibubi.create.content.logistics.trains.IBogeyBlock;

public interface ICustomBogeyBlock extends IBogeyBlock {
    default int getStyle() {
        return 0;
    }
}
