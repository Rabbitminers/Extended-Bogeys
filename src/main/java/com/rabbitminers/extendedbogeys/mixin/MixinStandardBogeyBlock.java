package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.BlockStates;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.contraptions.wrench.WrenchItem;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    private static final Property<Integer> STYLE = BlockStates.STYLE;
    private static final Property<Boolean> IS_FACING_FORWARD = BlockStates.IS_FACING_FOWARD;
    private static final EnumProperty<DyeColor> PAINT_COLOUR = BlockStates.PAINT_COLOUR;
    public MixinStandardBogeyBlock(Properties pProperties) {
        super(pProperties);
    }

    @Inject(at = @At("HEAD"), method = "createBlockStateDefinition")
    public void createBlockStateDefenition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(STYLE);
        builder.add(IS_FACING_FORWARD);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player,
                                          InteractionHand interactionHand, BlockHitResult blockHitResult) {

        IStyledStandardBogeyTileEntity te = (IStyledStandardBogeyTileEntity) level.getBlockEntity(blockPos);
        if (te == null)
            return InteractionResult.FAIL;

        if (player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().getItem() == Items.AIR) {
            BlockState unlinkedBlockState = large ? ExtendedBogeysBlocks.LARGE_UNLINKED_BOGEY.getDefaultState() : ExtendedBogeysBlocks.SMALL_UNLINKED_BOGEY.getDefaultState();

            level.setBlock(blockPos, unlinkedBlockState
                    .setValue(STYLE, state.getValue(STYLE))
                    .setValue(AXIS, state.getValue(AXIS)), 3);

            player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.unlink"), true);

            return InteractionResult.CONSUME;
        }

        if (!player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().getItem() == Items.AIR) {
            boolean facing = state.getValue(IS_FACING_FORWARD);

            te.setIsFacingForwards(!te.getIsFacingForwards());

            level.setBlock(blockPos, state.setValue(IS_FACING_FORWARD, !facing), 3);
            player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.rotation"), true);
            return InteractionResult.CONSUME;
        }

        if (!player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && DyeColor.getColor(player.getMainHandItem()) != null) {

        }

        if (!level.isClientSide && player.getMainHandItem().getItem() instanceof WrenchItem wrenchItem
                && !player.getCooldowns().isOnCooldown(wrenchItem) && interactionHand == InteractionHand.MAIN_HAND) {
            player.getCooldowns().addCooldown(wrenchItem, 20);

            int bogeyStyle = te.getBogeyStyle();
            bogeyStyle = bogeyStyle >= BogeyStyles.getNumberOfBogeyStyleVariations()-1 ? 0 : bogeyStyle + 1;

            IBogeyStyle style = BogeyStyles.getBogeyStyle(bogeyStyle);

            te.setBogeyStyle(bogeyStyle);
            System.out.println("te = " + te);
            player.displayClientMessage(new TextComponent("Bogey Style: " + bogeyStyle + " \"" + style.getStyleName() + "\""), true);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderWithTileEntity(BlockState state, IStyledStandardBogeyTileEntity te, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers, int light, int overlay) {

        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        boolean isFacingForward = te.getIsFacingForwards();
        int style = te.getBogeyStyle();

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

    /**
     * @author Rabbitminers / Extended Bogeys
     * @reason Replace rendering with style based system
     */
    @OnlyIn(Dist.CLIENT)
    // @Overwrite(remap = false)
    public void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers,
                       int light, int overlay) {
        boolean isFacingForward = true;

        int style = 0;
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
            isFacingForward = state.getValue(IS_FACING_FORWARD);
            style = state.getValue(STYLE);
        }

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
