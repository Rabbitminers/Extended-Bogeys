package com.rabbitminers.extendedbogeys.mixin.client;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogeys.unlinked.UnlinkedBogeyBlock;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlock;
import com.simibubi.create.content.trains.bogey.AbstractBogeyBlockEntity;
import com.simibubi.create.content.trains.bogey.BogeySizes;
import com.simibubi.create.content.trains.bogey.BogeyStyle;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(AbstractBogeyBlockEntity.class)
public class MixinAbstractBogeyBlockEntity extends BlockEntity implements IHaveGoggleInformation {
    public MixinAbstractBogeyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        LangBuilder builder = new LangBuilder(ExtendedBogeys.MOD_ID);
        builder.translate("bogeys.bogey").forGoggles(tooltip);
        if (level == null)
            return false;
        BlockState state = level.getBlockState(getBlockPos());
        AbstractBogeyBlockEntity bogeyBlockEntity = (AbstractBogeyBlockEntity) level.getBlockEntity(getBlockPos());
        if (!(state.getBlock() instanceof AbstractBogeyBlock<?> abb) || bogeyBlockEntity == null)
            return false;
        BogeyStyle style = bogeyBlockEntity.getStyle();
        Set<BogeySizes.BogeySize> implementedSizes = style.validSizes();

        tooltip.add(Components.translatable("extendedbogeys.bogeys.sizes.text")
                .withStyle(ChatFormatting.GRAY));

        for (BogeySizes.BogeySize value : BogeySizes.getAllSizesSmallToLarge()) {
            boolean isImplemented = implementedSizes.contains(value);
            boolean isActive = value == abb.getSize();

            tooltip.add(Components.literal(isActive ? "-> " : isImplemented ? "✔ " : "× ")
                    .append(capitalize(value.location().getPath()))
                    .withStyle(isActive ? ChatFormatting.GOLD : isImplemented
                            ? ChatFormatting.GREEN : ChatFormatting.RED));
        }
        return true;
    }

    private String capitalize(final String line) {
        return Arrays.stream(line.split("_"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    LerpedFloat virtualAnimation = LerpedFloat.angular();

    @Inject(method = "getVirtualAngle", at = @At("RETURN"), cancellable = true, remap = false)
    public void getVirtualAngle(float partialTicks, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(virtualAnimation.getValue(partialTicks));
    }

    /**
     * @author Rabbitminers
     * @reason Allow for unlinked bogeys to be animated
     */
    @Overwrite(remap = false)
    public void animate(float distanceMoved) {
        BlockState blockState = getBlockState();
        double wheelRadius;
        if (blockState.getBlock() instanceof AbstractBogeyBlock<?> type)
            wheelRadius = type.getWheelRadius();
        else if (blockState.getBlock() instanceof UnlinkedBogeyBlock type)
            wheelRadius = type.getWheelRadius();
        else return;
        double angleDiff = 360 * distanceMoved / (Math.PI * 2 * wheelRadius);
        double newWheelAngle = (virtualAnimation.getValue() - angleDiff) % 360;
        virtualAnimation.setValue(newWheelAngle);
    }
}
