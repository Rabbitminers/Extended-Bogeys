package com.rabbitminers.extendedbogeys.bogey.sizes;

import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;

public class CustomSizeBogeyBlock extends StandardBogeyBlock {
    BogeySize size;
    public CustomSizeBogeyBlock(Properties p_i48440_1_, BogeySize size) {
        super(p_i48440_1_, false);
        this.size = size;
    }
    @Override
    public double getWheelRadius() {
        return size.getWheelRadius();
    }
}
