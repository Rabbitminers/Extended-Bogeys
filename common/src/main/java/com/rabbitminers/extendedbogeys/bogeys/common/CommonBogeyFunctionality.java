package com.rabbitminers.extendedbogeys.bogeys.common;

import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.rabbitminers.extendedbogeys.base.Constants.*;

public class CommonBogeyFunctionality {
    public static InteractionResult onInteractWithBogey(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.FAIL;

        AbstractBogeyBlockEntity be = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);

        ItemStack heldItem = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(heldItem.getItem()))
            return InteractionResult.PASS;

        if (be != null && heldItem.getItem() instanceof DyeItem dyeItem && !player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            BogeyPaintColour colour = BogeyPaintColour.of(dyeItem.getDyeColor());

            colour.dyeColour.ifPresent(color -> player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.dyed")
                    .append(capitalize(colour.dyeColour.get().getName()))
                    .withStyle(Style.EMPTY.withColor(color.getTextColor())), true));

            NBTHelper.writeEnum(bogeyData, BOGEY_PAINT_KEY, colour);

            be.setBogeyData(bogeyData);

            if (!player.isCreative())
                heldItem.shrink(1);

            return InteractionResult.CONSUME;
        }

        if (be != null && heldItem.getItem() instanceof AxeItem && !player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            if (!bogeyData.contains(BOGEY_PAINT_KEY)) {
                NBTHelper.writeEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.UNPAINTED);
                player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.already_clean"), true);
                be.setBogeyData(bogeyData);
                return InteractionResult.CONSUME;
            }

            BogeyPaintColour colour = NBTHelper.readEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.class);
            if (colour == BogeyPaintColour.UNPAINTED) {
                player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.already_clean"), true);
                return InteractionResult.CONSUME;
            }

            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.cleaned"), true);
            NBTHelper.writeEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.UNPAINTED);

            be.setBogeyData(bogeyData);
            return InteractionResult.CONSUME;
        }

        if (be != null && heldItem.isEmpty() && !player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);

            CompoundTag bogeyData = be.getBogeyData();

            boolean isForwards = bogeyData.getBoolean(BOGEY_DIRECTION_KEY);
            Direction assemblyDirection = NBTHelper.readEnum(bogeyData, BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.class);

            if (!bogeyData.contains(BOGEY_DIRECTION_KEY)) {
                bogeyData.putBoolean(BOGEY_DIRECTION_KEY, isDirectionPosotive(assemblyDirection));
                be.setBogeyData(bogeyData);
                return InteractionResult.CONSUME;
            }

            if (!bogeyData.contains(BOGEY_ASSEMBLY_DIRECTION_KEY)) {
                NBTHelper.writeEnum(bogeyData, BOGEY_ASSEMBLY_DIRECTION_KEY, Direction.SOUTH);
                be.setBogeyData(bogeyData);
                return InteractionResult.CONSUME;
            }

            bogeyData.putBoolean(BOGEY_DIRECTION_KEY, !isForwards);

            be.setBogeyData(bogeyData);
            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.rotation"), true);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
    private static boolean isDirectionPosotive(Direction direction) {
        return switch (direction) { case NORTH, WEST, UP -> true; case SOUTH, DOWN, EAST -> false; };
    }

    private static String capitalize(final String line) {
        return Arrays.stream(line.split("_"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
