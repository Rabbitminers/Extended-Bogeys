package com.rabbitminers.extendedbogeys.bogey.unlinked;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysTileEntities;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.contraptions.components.actors.DrillRenderer;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.contraptions.wrench.WrenchItem;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.content.schematics.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.ItemRequirement;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class UnlinkedStandardBogeyBlock extends Block implements ITE<UnlinkedBogeyTileEntity>, ProperWaterloggedBlock,
        ISpecialBlockItemRequirement, IUnlinkedBogeyBlock, IStyledStandardBogeyBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
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
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player,
                                          InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity be = level.getBlockEntity(blockPos);
        IStyledStandardBogeyTileEntity te = (IStyledStandardBogeyTileEntity) be;
        if (be == null)
            return InteractionResult.FAIL;
        CompoundTag tileData = be.getTileData();

        if (player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND) {
            BlockState unlinkedBlockState = large
                    ? AllBlocks.LARGE_BOGEY.getDefaultState()
                    : AllBlocks.SMALL_BOGEY.getDefaultState();

            level.setBlock(blockPos, unlinkedBlockState.setValue(AXIS, state.getValue(AXIS)), 3);
            IStyledStandardBogeyTileEntity linkedStandardBogeyTileEntity =
                    (IStyledStandardBogeyTileEntity) level.getBlockEntity(blockPos);

            CompoundTag unlinkedTileData =
                    ((BlockEntity) linkedStandardBogeyTileEntity).getTileData();
            linkedStandardBogeyTileEntity
                    .setBogeyStyle(unlinkedTileData, te.getBogeyStyle(tileData));
            linkedStandardBogeyTileEntity
                    .setIsFacingForwards(unlinkedTileData, te.getIsFacingForwards(tileData));

            player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.link").withStyle(ChatFormatting.GREEN), true);

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
    public void renderWithTileEntity(BlockState state, BlockEntity be, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers, int light, int overlay) {
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        ms.translate(0, -1.5 - 1 / 128f, 0);

        VertexConsumer vb = buffers.getBuffer(RenderType.cutoutMipped());
        BlockState air = Blocks.AIR.defaultBlockState();

        CompoundTag tileData = be.getTileData();

        IStyledStandardBogeyTileEntity te = (IStyledStandardBogeyTileEntity) be;

        boolean isFacingForward = te.getIsFacingForwards(tileData);

        int styleId = te.getBogeyStyle(tileData);
        IBogeyStyle style = BogeyStyles.getBogeyStyle(styleId);

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

        if (!style.shouldRenderDefault(large)) {
            style.renderInWorld(large, isFacingForward, wheelAngle, ms, light, vb, air);
        } else if (large) {
            renderLargeBogey(wheelAngle, ms, light, vb, air);
        } else {
            renderBogey(wheelAngle, ms, light, vb, air);
        }
    }

    private void renderBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        CachedBufferer.partial(AllBlockPartials.BOGEY_FRAME, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(AllBlockPartials.SMALL_BOGEY_WHEELS, air)
                    .translate(0, 12 / 16f, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }
    }

    private void renderLargeBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air) {
        for (int i : Iterate.zeroAndOne)
            CachedBufferer.block(AllBlocks.SHAFT.getDefaultState()
                            .setValue(ShaftBlock.AXIS, Direction.Axis.X))
                    .translate(-.5f, .25f, .5f + i * -2)
                    .centre()
                    .rotateX(wheelAngle)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);

        CachedBufferer.partial(AllBlockPartials.BOGEY_DRIVE, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);
        CachedBufferer.partial(AllBlockPartials.BOGEY_PISTON, air)
                .translate(0, 0, 1 / 4f * Math.sin(AngleHelper.rad(wheelAngle)))
                .light(light)
                .renderInto(ms, vb);

        ms.pushPose();
        CachedBufferer.partial(AllBlockPartials.LARGE_BOGEY_WHEELS, air)
                .translate(0, 1, 0)
                .rotateX(wheelAngle)
                .light(light)
                .renderInto(ms, vb);
        CachedBufferer.partial(AllBlockPartials.BOGEY_PIN, air)
                .translate(0, 1, 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle)
                .light(light)
                .renderInto(ms, vb);
        ms.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers,
                       int light, int overlay) {
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
