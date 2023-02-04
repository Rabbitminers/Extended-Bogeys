package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.contraptions.wrench.WrenchItem;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(StandardBogeyBlock.class)
public abstract class MixinStandardBogeyBlock extends Block implements IStyledStandardBogeyBlock {
    @Shadow @Final private boolean large;
    @Shadow public abstract EnumSet<Direction> getStickySurfaces(BlockGetter world, BlockPos pos, BlockState state);
    @Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

    @Shadow protected abstract void renderBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air);

    @Shadow protected abstract void renderLargeBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air);
    public MixinStandardBogeyBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player,
                                          InteractionHand interactionHand, BlockHitResult blockHitResult) {

        BlockEntity be = level.getBlockEntity(blockPos);
        IStyledStandardBogeyTileEntity te = (IStyledStandardBogeyTileEntity) be;
        if (be == null)
            return InteractionResult.FAIL;
        CompoundTag tileData = be.getTileData();

        if (player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().getItem() == Items.AIR) {
            BlockState unlinkedBlockState = large
                    ? ExtendedBogeysBlocks.LARGE_UNLINKED_BOGEY.getDefaultState()
                    : ExtendedBogeysBlocks.SMALL_UNLINKED_BOGEY.getDefaultState();

            level.setBlock(blockPos, unlinkedBlockState
                    .setValue(AXIS, state.getValue(AXIS)), 3);

            IStyledStandardBogeyTileEntity usbte =
                    (IStyledStandardBogeyTileEntity) level.getBlockEntity(blockPos);

            CompoundTag unlinkedTileData = ((BlockEntity) usbte).getTileData();
            usbte.setBogeyStyle(unlinkedTileData, te.getBogeyStyle(tileData));
            usbte.setIsFacingForwards(unlinkedTileData, te.getIsFacingForwards(tileData));

            player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.unlink"), true);

            return InteractionResult.CONSUME;
        }

        if (!player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().getItem() == Items.AIR) {

            te.setIsFacingForwards(tileData, !te.getIsFacingForwards(tileData));
            be.setChanged();

            player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.rotation"), true);
            return InteractionResult.CONSUME;
        }

        if (!player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && DyeColor.getColor(player.getMainHandItem()) != null) {

        }

        if (!level.isClientSide && player.getMainHandItem().getItem() instanceof WrenchItem wrenchItem
                && !player.getCooldowns().isOnCooldown(wrenchItem) && interactionHand == InteractionHand.MAIN_HAND) {
            player.getCooldowns().addCooldown(wrenchItem, 20);

            int bogeyStyle = te.getBogeyStyle(tileData);
            bogeyStyle = bogeyStyle >= BogeyStyles.getNumberOfBogeyStyleVariations()-1 ? 0 : bogeyStyle + 1;

            IBogeyStyle style = BogeyStyles.getBogeyStyle(bogeyStyle);

            te.setBogeyStyle(tileData, bogeyStyle);
            be.setChanged();

            player.displayClientMessage(new TextComponent("Bogey Style: " + bogeyStyle + " \"" + style.getStyleName() + "\""), true);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderWithTileEntity(BlockState state, BlockEntity te, float wheelAngle, PoseStack ms,
                                     float partialTicks, MultiBufferSource buffers, int light, int overlay) {
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
        }
        if (te == null)
            return;

        CompoundTag tileData = te.getTileData();

        IStyledStandardBogeyTileEntity ssbte = (IStyledStandardBogeyTileEntity) te;

        boolean isFacingForward = ssbte.getIsFacingForwards(tileData);
        int style = ssbte.getBogeyStyle(tileData);

        ms.translate(0, -1.5 - 1 / 128f, 0);

        VertexConsumer vb = buffers.getBuffer(RenderType.cutoutMipped());

        BlockState air = Blocks.AIR.defaultBlockState();
        IBogeyStyle bogeyStyle = BogeyStyles.getBogeyStyle(style);

        if (bogeyStyle.shouldRenderInnerShaft())
            for (int i : Iterate.zeroAndOne)
                CachedBufferer.block(AllBlocks.SHAFT.getDefaultState()
                                .setValue(ShaftBlock.AXIS, Direction.Axis.Z))
                        .translate(-.5f, .25f, i * -1)
                        .centre()
                        .rotateZ(wheelAngle)
                        .unCentre()
                        .light(light)
                        .renderInto(ms, vb);

        if (!bogeyStyle.shouldRenderDefault(large)) {
            bogeyStyle.renderInWorld(large, isFacingForward, wheelAngle, ms, light, vb, air);
        } else if (large) {
            renderLargeBogey(wheelAngle, ms, light, vb, air);
        } else {
            renderBogey(wheelAngle, ms, light, vb, air);
        }
    }
}
