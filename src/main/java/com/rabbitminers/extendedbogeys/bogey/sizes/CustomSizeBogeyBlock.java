package com.rabbitminers.extendedbogeys.bogey.sizes;

import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CustomSizeBogeyBlock extends StandardBogeyBlock {
    BogeySize size;
    public CustomSizeBogeyBlock(Properties p_i48440_1_, BogeySize size) {
        super(p_i48440_1_, size.isDriver());
        this.size = size;
    }

    // TODO: SPECIFY SIZE HERE
    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public double getWheelRadius() {
        return size.getWheelRadius();
    }
}
