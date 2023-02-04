package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(Contraption.class)
public class MixinContraption {

    @Shadow public Map<BlockPos, BlockEntity> presentTileEntities;
    private StructureTemplate.StructureBlockInfo block;

    @ModifyVariable(method = "addBlocksToWorld", at = @At(value = "STORE"), name = "block", remap = false)
    public StructureTemplate.StructureBlockInfo captureStructureBlockInfo(StructureTemplate.StructureBlockInfo block) {
        this.block = block;
        return block;
    }

    @Redirect(
            method = "addBlocksToWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            ),
            remap = false
    )
    public boolean appendBogeyStyleToTileEntity(Level world, BlockPos targetPos, BlockState state, int p_46603_) {
        boolean returnValue =
                world.setBlock(targetPos, state, Block.UPDATE_MOVE_BY_PISTON | Block.UPDATE_ALL);

        BlockEntity inContraptionTileEntity = presentTileEntities.get(block.pos);
        BlockEntity inWorldTileEntity = world.getBlockEntity(targetPos);

        return returnValue;
    }

}
