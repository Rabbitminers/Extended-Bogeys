package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.relays.elementary.ShaftBlock;
import com.simibubi.create.content.contraptions.wrench.WrenchItem;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.simibubi.create.content.logistics.trains.track.StandardBogeyBlock.AXIS;

@Mixin(StandardBogeyBlock.class)
public abstract class MixinBogeyRender extends Block {
    private static final Property<Integer> STYLE = IntegerProperty.create("style", 0, 5);
    @Shadow public abstract void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers, int light, int overlay);
    @Shadow @Final private boolean large;

    @Shadow protected abstract void renderLargeBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air);

    @Shadow protected abstract void renderBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air);

    public MixinBogeyRender(Properties pProperties) {
        super(pProperties);
    }

    @Inject(at = @At("HEAD"), method = "createBlockStateDefinition", remap = false)
    public void createBlockStateDefenition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(STYLE);
    }

    // This is just for testing, dont actually use it, it looks like genuine shit :)
    @Deprecated
    private void renderCustomBogey(float wheelAngle, PoseStack ms, int light, VertexConsumer vb, BlockState air,
                                   PartialModel frame, PartialModel wheels) {
        CachedBufferer.partial(frame, air)
                .scale(1 - 1 / 512f)
                .light(light)
                .renderInto(ms, vb);

        for (int side : Iterate.positiveAndNegative) {
            ms.pushPose();
            CachedBufferer.partial(wheels, air)
                    .translate(0, 12 / 16f, side)
                    .rotateX(wheelAngle)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide && player.getMainHandItem().getItem() instanceof WrenchItem wrenchItem
            && !player.getCooldowns().isOnCooldown(wrenchItem) && interactionHand == InteractionHand.MAIN_HAND) {

            player.getCooldowns().addCooldown(wrenchItem, 20);
            BlockEntity te = level.getBlockEntity(blockPos);

            if (!(te instanceof StandardBogeyTileEntity standardBogeyTileEntity))
                return InteractionResult.FAIL;

            CompoundTag td = standardBogeyTileEntity.getTileData();
            int bogeyType = td.contains("type") ? td.getInt("type") : 0;

            bogeyType = bogeyType >= 5 ? 0 : bogeyType + 1;
            level.setBlock(blockPos, state.setValue(STYLE, bogeyType), 3);

            standardBogeyTileEntity.getTileData().putInt("type", bogeyType);
            player.displayClientMessage(new TextComponent("Bogey Type: " + bogeyType), true);

            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @OnlyIn(Dist.CLIENT)
    @Inject(at = @At("HEAD"), method = "render", cancellable = true, remap = false)
    public void render(BlockState state, float wheelAngle, PoseStack ms, float partialTicks, MultiBufferSource buffers,
                       int light, int overlay, CallbackInfo ci) {
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

        for (int i : Iterate.zeroAndOne)
            CachedBufferer.block(AllBlocks.SHAFT.getDefaultState()
                    .setValue(ShaftBlock.AXIS, Direction.Axis.Z))
                    .translate(-.5f, .25f, i * -1)
                    .centre()
                    .rotateZ(wheelAngle)
                    .unCentre()
                    .light(light)
                    .renderInto(ms, vb);


        if (large)
            renderLargeBogey(wheelAngle, ms, light, vb, air);
        else
            switch (style) {
                case 0 -> renderBogey(wheelAngle, ms, light, vb, air);
                case 1 -> renderCustomBogey(wheelAngle, ms, light, vb, air, AllBlockPartials.BLAZE_ACTIVE, AllBlockPartials.BLAZE_BURNER_FLAME);
                case 2 -> renderCustomBogey(wheelAngle, ms, light, vb, air, AllBlockPartials.ANALOG_LEVER_INDICATOR, AllBlockPartials.BLAZE_ACTIVE);
                case 3 -> renderCustomBogey(wheelAngle, ms, light, vb, air, AllBlockPartials.BLAZE_BURNER_RODS, AllBlockPartials.BLAZE_BURNER_SUPER_RODS);
                case 4 -> renderCustomBogey(wheelAngle, ms, light, vb, air, AllBlockPartials.BOGEY_FRAME, AllBlockPartials.BOGEY_FRAME);
                case 5 -> renderCustomBogey(wheelAngle, ms, light, vb, air, AllBlockPartials.COUPLING_CONNECTOR, AllBlockPartials.BOGEY_DRIVE);
            }


        ci.cancel();
    }
}
