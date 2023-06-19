package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.bogeys.common.CommonBogeyFunctionality;
import com.rabbitminers.extendedbogeys.data.ExtendedBogeysBogeySize;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.simibubi.create.content.trains.bogey.*;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBogeyBlock.class)
public abstract class MixinAbstractBogeyBlock {

    @Shadow protected abstract BlockState copyProperties(BlockState source, BlockState target);

    @Inject(method = "use", at = @At("TAIL"), cancellable = true)
    public void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit,
                               CallbackInfoReturnable<InteractionResult> cir) {
        CommonBogeyFunctionality.onInteractWithBogey(state, level, pos, player, hand, hit);
        ItemStack heldItem = player.getItemInHand(hand);

        AbstractBogeyBlockEntity be = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);
        if (be == null) return;

        if (!level.isClientSide && !player.getCooldowns().isOnCooldown(heldItem.getItem()) && heldItem.is(Items.AIR)
                && player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            if (!(state.getBlock() instanceof StandardBogeyBlock unlinkedBogeyBlock))
                return;
            ExtendedBogeysBogeySize supported = ExtendedBogeysBogeySize.of(unlinkedBogeyBlock.size);
            if (supported == null)
                return;
            BlockState newState = ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(supported).getDefaultState();
            level.setBlock(pos, copyProperties(state, newState), 3);
            AbstractBogeyBlockEntity newBlockEntity = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);
            if (newBlockEntity == null) return;
            bogeyData.putBoolean(Constants.BOGEY_LINK_KEY, false);
            newBlockEntity.setBogeyData(bogeyData);
            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.unlink")
                    .withStyle(ChatFormatting.GREEN), true);
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    public void fixReturnValue(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit,
                               CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.CONSUME);
    }
}
