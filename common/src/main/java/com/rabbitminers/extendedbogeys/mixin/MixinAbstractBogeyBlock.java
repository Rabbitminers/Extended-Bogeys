package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.data.BogeyPaintColour;
import com.simibubi.create.content.trains.bogey.*;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
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

import static com.rabbitminers.extendedbogeys.base.Constants.BOGEY_PAINT_KEY;
import static com.rabbitminers.extendedbogeys.base.Constants.BOGEY_DIRECTION_KEY;

@Mixin(AbstractBogeyBlock.class)
public abstract class MixinAbstractBogeyBlock {
    @Shadow public abstract void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers, int light, int overlay, BogeyStyle style, CompoundTag bogeyData);

    @Inject(method = "use", at=@At("TAIL"))
    public void onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit,
                      CallbackInfoReturnable<InteractionResult> cir) {
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

            colour.dyeColour.ifPresent(color -> player.displayClientMessage(Lang.translateDirect("extendedbogeys.tooltips.dyed")
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
                player.displayClientMessage(Lang.translateDirect("extendedbogeys.tooltips.already_clean"), true);
                be.setBogeyData(bogeyData);
                return;
            }

            BogeyPaintColour colour = NBTHelper.readEnum(bogeyData, BOGEY_PAINT_KEY, BogeyPaintColour.class);
            if (colour == BogeyPaintColour.UNPAINTED) {
                player.displayClientMessage(Lang.translateDirect("extendedbogeys.tooltips.already_clean"), true);
                return;
            }

            player.displayClientMessage(Lang.translateDirect("extendedbogeys.tooltips.cleaned"), true);
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
            player.displayClientMessage(Lang.translateDirect("extendedbogeys.tooltips.rotation"), true);

            return;
        }
    }
}
