package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysTileEntities;
import com.rabbitminers.extendedbogeys.mixin_interface.BlockStates;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.contraptions.components.actors.DrillRenderer;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.content.schematics.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.ItemRequirement;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class UnlinkedStandardBogeyBlock extends Block
        implements ITE<UnlinkedBogeyTileEntity>, ProperWaterloggedBlock, ISpecialBlockItemRequirement, IUnlinkedBogeyBlock {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final IntegerProperty STYLE = BlockStates.STYLE;
    public static final BooleanProperty IS_FACING_FORWARD = BlockStates.IS_FACING_FOWARD;
    private final boolean large;

    static final EnumSet<Direction> STICKY_X = EnumSet.of(Direction.EAST, Direction.WEST);
    static final EnumSet<Direction> STICKY_Z = EnumSet.of(Direction.SOUTH, Direction.NORTH);

    public UnlinkedStandardBogeyBlock(Properties p_49795_, boolean large) {
        super(p_49795_);
        this.large = large;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
        builder.add(STYLE);
        builder.add(IS_FACING_FORWARD);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player,
                                          InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND) {
            BlockState unlinkedBlockState = large
                    ? AllBlocks.LARGE_BOGEY.getDefaultState()
                    : AllBlocks.SMALL_BOGEY.getDefaultState();

            level.setBlock(blockPos, unlinkedBlockState.setValue(STYLE, state.getValue(STYLE)).setValue(AXIS, state.getValue(AXIS)), 3);

            player.displayClientMessage(new TextComponent("Linked Bogey!").withStyle(ChatFormatting.GREEN), true);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers,
                       int light, int overlay) {
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        ms.translate(0, -1.5 - 1 / 128f, 0);

        VertexConsumer vb = buffers.getBuffer(RenderType.cutoutMipped());
        BlockState air = Blocks.AIR.defaultBlockState();

        assert state.hasProperty(BlockStates.STYLE);
        int styleId = state.getValue(BlockStates.STYLE);
        IBogeyStyle style = BogeyStyles.getBogeyStyle(styleId);

        boolean isFacingForward = state.getValue(BlockStates.IS_FACING_FOWARD);

        if (style.shouldRenderInnerShaft())
            for (int i : Iterate.zeroAndOne)
                CachedBufferer.block(AllBlocks.SHAFT.getDefaultState()
                        .setValue(ShaftBlock.AXIS, Direction.Axis.Z))
                        .translate(-.5f, .25f, i * -1)
                        .centre()
                        .rotateZ(wheelAngle)
                        .unCentre()
                        .light(light)
                        .renderInto(ms, vb);

        style.renderInWorld(large, isFacingForward,wheelAngle, ms, light, vb, air);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return switch (pRotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> pState.cycle(AXIS);
            default -> pState;
        };
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos,
                                       Player player) {
        return AllBlocks.RAILWAY_CASING.asStack();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, AllBlocks.RAILWAY_CASING.asStack());
    }

    @Override
    public Class<UnlinkedBogeyTileEntity> getTileEntityClass() {
        return UnlinkedBogeyTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends UnlinkedBogeyTileEntity> getTileEntityType() {
        return ExtendedBogeysTileEntities.UNLINKED_BOGEY_TILE_ENTITY.get();
    }
}
