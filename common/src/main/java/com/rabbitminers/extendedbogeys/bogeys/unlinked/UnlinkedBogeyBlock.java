package com.rabbitminers.extendedbogeys.bogeys.unlinked;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.rabbitminers.extendedbogeys.base.Constants;
import com.rabbitminers.extendedbogeys.bogeys.common.CommonBogeyFunctionality;
import com.rabbitminers.extendedbogeys.data.ExtendedBogeysBogeySize;
import com.rabbitminers.extendedbogeys.registry.ExtendedBogeysBlocks;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBogeyStyles;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.trains.bogey.*;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public class UnlinkedBogeyBlock extends Block implements IBE<StandardBogeyBlockEntity>, ProperWaterloggedBlock {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public final BogeySizes.BogeySize size;

    public UnlinkedBogeyBlock(Properties properties, BogeySizes.BogeySize size) {
        super(properties);
        this.size = size;
    }

    public double getWheelRadius() {
        return this.size.wheelRadius();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (level.isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = player.getItemInHand(hand);

        AbstractBogeyBlockEntity be = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);
        if (be == null) return InteractionResult.FAIL;


        if (!player.isShiftKeyDown() && stack.is(AllItems.WRENCH.get()) && !player.getCooldowns().isOnCooldown(stack.getItem())
                && AllBogeyStyles.BOGEY_STYLES.size() > 1) {

            player.getCooldowns().addCooldown(stack.getItem(), 20);
            BogeyStyle currentStyle = be.getStyle();

            BogeySizes.BogeySize size = this.size;

            BogeyStyle style = this.getNextStyle(currentStyle);
            if (style == currentStyle)
                return InteractionResult.PASS;

            Set<BogeySizes.BogeySize> validSizes = style.validSizes();

            for (int i = 0; i < BogeySizes.count(); i++) {
                if (validSizes.contains(size)) break;
                size = size.increment();
            }

            be.setBogeyStyle(style);

            CompoundTag defaultData = style.defaultData;
            be.setBogeyData(be.getBogeyData().merge(defaultData));

            if (size == this.size) {
                player.displayClientMessage(Components.translatable("create.bogey.style.updated_style")
                        .append(": ").append(style.displayName), true);
            } else {
                CompoundTag oldData = be.getBogeyData();
                level.setBlock(pos, this.getStateOfSize(be, size), 3);
                BlockEntity newBlockEntity = level.getBlockEntity(pos);
                if (!(newBlockEntity instanceof AbstractBogeyBlockEntity newTileEntity))
                    return InteractionResult.FAIL;
                newTileEntity.setBogeyData(oldData);
                player.displayClientMessage(Components.translatable("create.bogey.style.updated_style_and_size")
                        .append(": ").append(style.displayName), true);
            }

            return InteractionResult.CONSUME;
        }

        InteractionResult commonResult = CommonBogeyFunctionality.onInteractWithBogey(state, level, pos, player, hand, hit);

        if (commonResult != InteractionResult.PASS)
            return commonResult;

        if (!player.getCooldowns().isOnCooldown(stack.getItem()) && stack.is(Items.AIR) && player.isShiftKeyDown()) {
            player.getCooldowns().addCooldown(stack.getItem(), 20);
            CompoundTag bogeyData = be.getBogeyData();
            if (!(state.getBlock() instanceof UnlinkedBogeyBlock unlinkedBogeyBlock))
                return InteractionResult.FAIL;
            ExtendedBogeysBogeySize supported = ExtendedBogeysBogeySize.of(unlinkedBogeyBlock.size);
            if (supported == null)
                return InteractionResult.PASS; // Not really sure how this could happen but eh.
            BlockState newState = ExtendedBogeysBlocks.STANDARD_BOGEYS.get(supported).getDefaultState();
            level.setBlock(pos,  copyProperties(state, newState), 3);
            AbstractBogeyBlockEntity newBlockEntity = (AbstractBogeyBlockEntity) level.getBlockEntity(pos);
            if (newBlockEntity == null) return InteractionResult.FAIL;
            newBlockEntity.setBogeyData(bogeyData);
            player.displayClientMessage(Components.translatable("extendedbogeys.tooltips.link")
                    .withStyle(ChatFormatting.GREEN), true);
            bogeyData.putBoolean(Constants.BOGEY_LINK_KEY, true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public void render(@Nullable BlockState state, float wheelAngle, PoseStack ms, float partialTicks,
                       MultiBufferSource buffers, int light, int overlay, BogeyStyle style, CompoundTag bogeyData) {
        if (style == null)
            style = AllBogeyStyles.STANDARD;

        final Optional<BogeyRenderer.CommonRenderer> commonRenderer
                = style.getInWorldCommonRenderInstance();

        final BogeyRenderer renderer = style.getInWorldRenderInstance(size);
        if (state != null) {
            ms.translate(.5f, .5f, .5f);
            if (state.getValue(AXIS) == Direction.Axis.X)
                ms.mulPose(Axis.YP.rotationDegrees(90));
        }
        ms.translate(0, -1.5 - 1 / 128f, 0);
        VertexConsumer vb = buffers.getBuffer(RenderType.cutoutMipped());
        if (bogeyData == null)
            bogeyData = new CompoundTag();
        renderer.render(bogeyData, wheelAngle, ms, light, vb, state == null);
        CompoundTag finalBogeyData = bogeyData;
        commonRenderer.ifPresent(common ->
                common.render(finalBogeyData, wheelAngle, ms, light, vb, state == null));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    public BlockState getStateOfSize(AbstractBogeyBlockEntity sbte, BogeySizes.BogeySize size) {
        BlockState state;
        ExtendedBogeysBogeySize supported = ExtendedBogeysBogeySize.of(size);
        if (supported != null) {
            state = ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(supported).getDefaultState();
        } else {
            BogeyStyle style = sbte.getStyle();
            state = style.getBlockOfSize(size).defaultBlockState();
        }
        return copyProperties(sbte.getBlockState(), state);
    }

    private BlockState copyProperties(BlockState source, BlockState target) {
        for (Property<?> property : propertiesToCopy())
            target = copyProperty(source, target, property);
        return target;
    }

    public List<Property<?>> propertiesToCopy() {
        return ImmutableList.of(WATERLOGGED, AXIS);
    }

    private <V extends Comparable<V>> BlockState copyProperty(BlockState source, BlockState target, Property<V> property) {
        if (source.hasProperty(property) && target.hasProperty(property)) {
            return target.setValue(property, source.getValue(property));
        }
        return target;
    }

    public BogeyStyle getNextStyle(BogeyStyle style) {
        Collection<BogeyStyle> allStyles = style.getCycleGroup().values();
        if (allStyles.size() <= 1)
            return style;
        List<BogeyStyle> list = new ArrayList<>(allStyles);
        return Iterate.cycleValue(list, style);
    }

    @Override
    public Class<StandardBogeyBlockEntity> getBlockEntityClass() {
        return StandardBogeyBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends StandardBogeyBlockEntity> getBlockEntityType() {
        return AllBlockEntityTypes.BOGEY.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return AllBlockEntityTypes.BOGEY.create(pos, state);
    }
}
