package com.rabbitminers.extendedbogeys.bogeys.common;

import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static com.rabbitminers.extendedbogeys.base.Constants.BOGEY_DIRECTION_KEY;
import static com.rabbitminers.extendedbogeys.base.Constants.BOGEY_PAINT_KEY;

public class CommonBogeyFunctionality {
    public static void onInteractWithBogey(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return;

        AbstractBogeyBlockEntity be = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);

        ItemStack heldItem = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(heldItem.getItem()))
            return;

        if (be != null && heldItem.getItem() instanceof DyeItem dyeItem && !player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            BogeyPaintColour colour = BogeyPaintColour.of(dyeItem.getDyeColor());

            colour.dyeColour.ifPresent(color -> player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.dyed")
                    .append(colour.dyeColour.get().getName())
                    .withStyle(Style.EMPTY.withColor(color.getTextColor())), true));

            NBTHelper.writeEnum(bogeyData, BOGEY_PAINT_KEY, colour);

            be.setBogeyData(bogeyData);

            if (!player.isCreative())
                heldItem.shrink(1);

            return;
        }

        if (be != null && heldItem.getItem() instanceof AxeItem && !player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            if (!bogeyData.contains(BOGEY_PAINT_KEY)) {
                NBTHelper.writeEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.UNPAINTED);
                player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.already_clean"), true);
                be.setBogeyData(bogeyData);
                return;
            }

            BogeyPaintColour colour = NBTHelper.readEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.class);
            if (colour == BogeyPaintColour.UNPAINTED) {
                player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.already_clean"), true);
                return;
            }

            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.cleaned"), true);
            NBTHelper.writeEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.UNPAINTED);

            be.setBogeyData(bogeyData);
            return;
        }

        if (be != null && heldItem.isEmpty() && !player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);

            CompoundTag bogeyData = be.getBogeyData();
            if (!bogeyData.contains(BOGEY_DIRECTION_KEY)) {
                bogeyData.putBoolean(BOGEY_DIRECTION_KEY, false);
                be.setBogeyData(bogeyData);
                return;
            }

            boolean isForwards = bogeyData.getBoolean(BOGEY_DIRECTION_KEY);
            bogeyData.putBoolean(BOGEY_DIRECTION_KEY, !isForwards);

            be.setBogeyData(bogeyData);
            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.rotation"), true);

            return;
        }
    }
}
