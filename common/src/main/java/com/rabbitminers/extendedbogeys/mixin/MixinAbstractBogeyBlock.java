package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.bogeys.common.CommonBogeyFunctionality;
import com.rabbitminers.extendedbogeys.data.ExtendedBogeysBogeySize;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.trains.bogey.*;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(AbstractBogeyBlock.class)
public abstract class MixinAbstractBogeyBlock extends Block  {
    public MixinAbstractBogeyBlock(Properties properties) {
        super(properties);
    }

    @Shadow protected abstract BlockState copyProperties(BlockState source, BlockState target);

    @Shadow public abstract BogeySizes.BogeySize getSize();

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);

        if (!player.isShiftKeyDown() && stack.is(AllItems.WRENCH.get()) && !player.getCooldowns().isOnCooldown(stack.getItem())
                && AllBogeyStyles.BOGEY_STYLES.size() > 1) {

            BlockEntity be = level.getBlockEntity(pos);

            if (!(be instanceof AbstractBogeyBlockEntity sbbe))
                return InteractionResult.FAIL;

            player.getCooldowns().addCooldown(stack.getItem(), 20);
            BogeyStyle currentStyle = sbbe.getStyle();

            BogeySizes.BogeySize size = getSize();

            BogeyStyle style = this.getNextStyle(currentStyle);
            if (style == currentStyle)
                return InteractionResult.PASS;

            Set<BogeySizes.BogeySize> validSizes = style.validSizes();

            for (int i = 0; i < BogeySizes.count(); i++) {
                if (validSizes.contains(size)) break;
                size = size.increment();
            }

            sbbe.setBogeyStyle(style);

            CompoundTag defaultData = style.defaultData;
            sbbe.setBogeyData(sbbe.getBogeyData().merge(defaultData));

            if (size == getSize()) {
                player.displayClientMessage(Lang.translateDirect("bogey.style.updated_style")
                        .append(": ").append(style.displayName), true);
            } else {
                CompoundTag oldData = sbbe.getBogeyData();
                level.setBlock(pos, getStateOfSize(sbbe, size), 3);
                BlockEntity newBlockEntity = level.getBlockEntity(pos);
                if (!(newBlockEntity instanceof AbstractBogeyBlockEntity newBlockEntity1))
                    return InteractionResult.FAIL;
                newBlockEntity1.setBogeyData(oldData);
                player.displayClientMessage(Lang.translateDirect("bogey.style.updated_style_and_size")
                        .append(": ").append(style.displayName), true);
            }

            return InteractionResult.CONSUME;
        }

        CommonBogeyFunctionality.onInteractWithBogey(state, level, pos, player, hand, hit);
        ItemStack heldItem = player.getItemInHand(hand);

        AbstractBogeyBlockEntity be = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);
        if (be == null) return InteractionResult.PASS;

        if (!level.isClientSide && !player.getCooldowns().isOnCooldown(heldItem.getItem()) && heldItem.is(Items.AIR)
                && player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            if (!(state.getBlock() instanceof StandardBogeyBlock unlinkedBogeyBlock))
                return InteractionResult.PASS;
            ExtendedBogeysBogeySize supported = ExtendedBogeysBogeySize.of(unlinkedBogeyBlock.size);
            if (supported == null)
                return InteractionResult.PASS;
            BlockState newState = ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(supported).getDefaultState();
            level.setBlock(pos, copyProperties(state, newState), 3);
            AbstractBogeyBlockEntity newBlockEntity = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);
            if (newBlockEntity == null) return InteractionResult.FAIL;
            bogeyData.putBoolean(Constants.BOGEY_LINK_KEY, false);
            newBlockEntity.setBogeyData(bogeyData);
            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.unlink")
                    .withStyle(ChatFormatting.GREEN), true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public BlockState getStateOfSize(AbstractBogeyBlockEntity sbte, BogeySizes.BogeySize size) {
        BogeyStyle style = sbte.getStyle();
        BlockState state = style.getBlockOfSize(size).defaultBlockState();
        return copyProperties(sbte.getBlockState(), state);
    }

    public BogeyStyle getNextStyle(BogeyStyle style) {
        Collection<BogeyStyle> allStyles = style.getCycleGroup().values();
        if (allStyles.size() <= 1)
            return style;
        List<BogeyStyle> list = new ArrayList<>(allStyles);
        return Iterate.cycleValue(list, style);
    }
}
