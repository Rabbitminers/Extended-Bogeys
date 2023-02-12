package com.rabbitminers.extendedbogeys.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.styles.BogeyStyles;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.BogeySizeUtils;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyBlock;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StandardBogeyBlock.class)
public abstract class MixinStandardBogeyBlock extends Block implements IStyledStandardBogeyBlock {
    @Shadow @Final private boolean large;

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

        // Unlink bogey
        if (player.isShiftKeyDown() && !level.isClientSide && interactionHand == InteractionHand.MAIN_HAND
                && player.getMainHandItem().getItem() == Items.AIR) {
            BlockState unlinkedBlockState = large ? ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(BogeySize.LARGE).getDefaultState()
                    : ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(BogeySize.SMALL).getDefaultState();

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
                && player.getMainHandItem().getItem() instanceof DyeItem) {
            ItemStack mainHandItem = player.getMainHandItem();
            DyeColor dyeColor = DyeColor.getColor(mainHandItem);

            if (dyeColor == null)
                return InteractionResult.FAIL;

            te.setPaintColour(tileData, dyeColor);
            be.setChanged();

            player.displayClientMessage(new TranslatableComponent("extendedbogeys.tooltips.dyed").append(dyeColor.getName())
                    .withStyle(Style.EMPTY.withColor(dyeColor.getTextColor())), true);

            if (!player.isCreative())
                mainHandItem.shrink(1);
        }

        if (!level.isClientSide && player.getMainHandItem().getItem() instanceof WrenchItem wrenchItem
                && !player.getCooldowns().isOnCooldown(wrenchItem) && interactionHand == InteractionHand.MAIN_HAND) {
            player.getCooldowns().addCooldown(wrenchItem, 20);

            int bogeyStyle = te.getBogeyStyle(tileData);
            bogeyStyle = bogeyStyle >= BogeyStyles.getNumberOfBogeyStyleVariations()-1 ? 0 : bogeyStyle + 1;

            BogeySize bogeySize = large ? BogeySize.LARGE : BogeySize.SMALL;
            IBogeyStyle style = BogeyStyles.getBogeyStyle(bogeyStyle);

            if (style.implemntedSizes().contains(bogeySize)) {
                te.setBogeyStyle(tileData, bogeyStyle);
                be.setChanged();

                player.displayClientMessage(new TextComponent("Bogey Style: " + bogeyStyle + " \"" + style.getStyleName() + "\""), true);
            } else {
                // Changed from recursive implementation
                for (int i = 0; i < BogeySize.values().length; i++) {
                    bogeySize = bogeySize.increment();
                    boolean success = updateSize(bogeySize, state, level, blockPos, te, tileData, bogeyStyle, style);
                    if (success) break;
                }
                player.displayClientMessage(new TextComponent("Updated Size & Set Bogey Style: " + bogeyStyle + " \"" + style.getStyleName() + "\""), true);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private boolean updateSize(BogeySize size, BlockState state, Level level, BlockPos blockPos, IStyledStandardBogeyTileEntity te,
                               CompoundTag tileData, int bogeyStyle, IBogeyStyle style) {
        if (style.implemntedSizes().contains(size)) {
            BlockState newBogeyState = BogeySizeUtils.blockStateFromBogeySize(size);
            level.setBlock(blockPos, newBogeyState.setValue(AXIS, state.getValue(AXIS)), 3);
            IStyledStandardBogeyTileEntity newBlockEntity =
                    (IStyledStandardBogeyTileEntity) level.getBlockEntity(blockPos);
            CompoundTag newTileData = ((BlockEntity) newBlockEntity).getTileData();

            newBlockEntity.setPaintColour(newTileData, te.getPaintColour(tileData));
            newBlockEntity.setIsFacingForwards(newTileData, te.getIsFacingForwards(tileData));

            newBlockEntity.setBogeyStyle(newTileData, bogeyStyle);
            return true;
        }
        return false;
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
        DyeColor dyeColor = ssbte.getPaintColour(tileData);

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
            bogeyStyle.renderInWorld(large, isFacingForward, wheelAngle, ms, light, vb, air, dyeColor);
        } else if (large) {
            renderLargeBogey(wheelAngle, ms, light, vb, air);
        } else {
            renderBogey(wheelAngle, ms, light, vb, air);
        }
    }

    @Override
    public BogeySize getSize() {
        return large ? BogeySize.LARGE : BogeySize.SMALL;
    }
}
