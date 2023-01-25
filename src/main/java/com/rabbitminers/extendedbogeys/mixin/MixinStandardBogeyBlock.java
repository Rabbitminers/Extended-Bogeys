package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.BlockStates;
import com.rabbitminers.extendedbogeys.mixin_interface.ICarriageBogeyStyle;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.components.structureMovement.bearing.MechanicalBearingBlock;
import com.simibubi.create.content.contraptions.relays.elementary.CogwheelBlockItem;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.contraptions.wrench.WrenchItem;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
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
import java.util.List;

import static com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock.AXIS;

@Mixin(StandardBogeyBlock.class)
public abstract class MixinStandardBogeyBlock extends Block {
    @Shadow @Final private boolean large;

    @Shadow public abstract EnumSet<Direction> getStickySurfaces(BlockGetter world, BlockPos pos, BlockState state);

    private static final Property<Integer> STYLE = BlockStates.STYLE;
    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public MixinStandardBogeyBlock(Properties pProperties) {
        super(pProperties);
    }

    @Inject(at = @At("HEAD"), method = "createBlockStateDefinition", remap = false)
    public void createBlockStateDefenition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(STYLE);
        builder.add(FACING);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {

        if (player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND) {
            // EnumSet<Direction> stickySurfaces = getStickySurfaces(level, blockPos, state);
            // Direction facing = state.getValue(FACING);
            BlockState unlinkedBlockState = large ? ExtendedBogeysBlocks.LARGE_UNLINKED_BOGEY.getDefaultState() : ExtendedBogeysBlocks.SMALL_UNLINKED_BOGEY.getDefaultState();

            level.setBlock(blockPos, unlinkedBlockState.setValue(STYLE, state.getValue(STYLE)), 3);

            return InteractionResult.CONSUME;
        }

        if (!level.isClientSide && player.getMainHandItem().getItem() instanceof WrenchItem wrenchItem
                && !player.getCooldowns().isOnCooldown(wrenchItem) && interactionHand == InteractionHand.MAIN_HAND) {
            player.getCooldowns().addCooldown(wrenchItem, 20);

            int bogeyStyle = state.getValue(STYLE);
            bogeyStyle = bogeyStyle >= BogeyStyles.getNumberOfBogeyStyleVariations() ? 0 : bogeyStyle + 1;

            level.setBlock(blockPos, state.setValue(STYLE, bogeyStyle), 3);
            player.displayClientMessage(new TextComponent("Bogey Style: " + bogeyStyle), true);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    /**
     * @author Rabbitminers / Extended Bogeys
     * @reason Replace rendering with style based system
     */
    @OnlyIn(Dist.CLIENT)
    @Overwrite(remap = false)
    public void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers,
                       int light, int overlay) {
        int style = 0;
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
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

        bogeyStyle.renderInWorld(large, wheelAngle, ms, light, vb, air);
    }
}
