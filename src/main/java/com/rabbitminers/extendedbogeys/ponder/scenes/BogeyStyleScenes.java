package com.rabbitminers.extendedbogeys.ponder.scenes;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.unlinked.IUnlinkedBogeyBlock;
import com.rabbitminers.extendedbogeys.bogey.unlinked.UnlinkedBogeyTileEntity;
import com.rabbitminers.extendedbogeys.index.ExtendedBogeysBlocks;
import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.atomic.AtomicReference;

public class BogeyStyleScenes {
    public static void styling(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("bogey_styling", "Stylising Bogeys");
        scene.configureBasePlate(1, 0, 12);
        scene.scaleSceneView(.65f);
        scene.setSceneOffsetY(-1);
        scene.showBasePlate();

        // Set tracks
        for (int i = 13; i >= 0; i--) {
            scene.world.showSection(util.select.position(i, 1, 6), Direction.DOWN);
            scene.idle(1);
        }


        ItemStack casing = AllBlocks.RAILWAY_CASING.asStack();
        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(11, 0, 6), Pointing.RIGHT).rightClick()
                .withItem(casing), 80);
        scene.idle(6);
        scene.world.restoreBlocks(util.select.position(10, 2, 6));
        ElementLink<WorldSectionElement> trainElement1 =
                scene.world.showIndependentSection(util.select.position(10, 2, 6), Direction.DOWN);
        scene.idle(20);
        scene.overlay.showText(70)
                .pointAt(util.vector.blockSurface(util.grid.at(10, 2, 6), Direction.WEST))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Create new bogeys by using Train Casing on Tracks");
        scene.idle(55);

        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(4, 0, 6), Pointing.RIGHT).rightClick()
                .withItem(casing), 15);
        scene.idle(6);
        scene.world.setBlock(util.grid.at(3, 2, 6), AllBlocks.SMALL_BOGEY.getDefaultState(), false);
        ElementLink<WorldSectionElement> trainElement2 =
                scene.world.showIndependentSection(util.select.position(3, 2, 6), Direction.DOWN);
        scene.idle(20);
        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(7, 0, 6), Pointing.RIGHT).rightClick()
                .withItem(casing), 15);
        scene.idle(6);
        scene.world.setBlock(util.grid.at(6, 2, 6), AllBlocks.SMALL_BOGEY.getDefaultState(), false);
        scene.world.showSectionAndMerge(util.select.position(6, 2, 6), Direction.DOWN, trainElement2);
        scene.idle(30);

        scene.overlay.showText(50)
                .pointAt(util.vector.topOf(3, 0, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Click the track again to cycle between bogey designs");
        scene.idle(35);

        /* Make Bogey Large */
        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(4, 0, 6), Pointing.RIGHT).rightClick(), 15);
        scene.idle(6);
        scene.world.restoreBlocks(util.select.position(3, 2, 6));
        scene.idle(20);
        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(7, 0, 6), Pointing.RIGHT).rightClick(), 15);
        scene.idle(6);
        scene.world.restoreBlocks(util.select.position(6, 2, 6));
        scene.idle(30);

        scene.world.setBlock(util.grid.at(10, 2, 6), Blocks.AIR.defaultBlockState(), true);
        scene.idle(10);

        scene.world.setBlock(util.grid.at(3, 2, 6), Blocks.AIR.defaultBlockState(), true);
        scene.idle(20);

        scene.overlay.showText(50)
                .pointAt(util.vector.topOf(6, 2, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Click again to use a small style");
        scene.idle(60);

        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(6, 1, 6), Pointing.RIGHT).rightClick(), 15);

        scene.world.setBlock(util.grid.at(6, 2, 6), AllBlocks.SMALL_BOGEY.getDefaultState(), false);
        scene.idle(60);

        scene.overlay.showText(50)
                .pointAt(util.vector.topOf(6, 2, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Click the track again with ye wrench to cycle between bogey designs");
        scene.idle(40);

        ItemStack wrench = AllItems.WRENCH.asStack();

        switchBogeyStyle(scene, new BlockPos(6, 2, 6), 1);
        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(6, 2, 6), Pointing.RIGHT).rightClick()
                .withItem(wrench), 15);
        scene.idle(80);

        scene.overlay.showText(50)
                .pointAt(util.vector.topOf(6, 2, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Click the Bogey with a bear hand to rotate it");
        scene.idle(60);

        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(6, 2, 6), Pointing.RIGHT).rightClick(), 15);

        switchBogeyDirection(scene, new BlockPos(6, 2, 6), false);
        scene.idle(40);

        scene.overlay.showText(30)
                .pointAt(util.vector.topOf(6, 2, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Shift right click the Bogey to unlink it");
        scene.idle(40);

        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(6, 2, 6), Pointing.RIGHT)
                .rightClick().whileSneaking(), 15);

        scene.world.setBlock(util.grid.at(6, 2, 6), ExtendedBogeysBlocks.UNLINKED_BOGEYS.get(BogeySize.SMALL).getDefaultState(), false);
        switchBogeyDirection(scene, new BlockPos(6, 2, 6), false);
        switchBogeyStyle(scene, new BlockPos(6, 2, 6), 1);

        scene.idle(60);

        scene.overlay.showText(80)
                .pointAt(util.vector.topOf(6, 2, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Unlinking a bogey allows you to have more than two bogeys on a carriage, the carriage will pivot around the linked bogeys");
        scene.idle(90);
    }

    public static void switchBogeyStyle(SceneBuilder scene, BlockPos pos, int style) {
        scene.world.modifyTileEntity(pos, StandardBogeyTileEntity.class, sbte -> {
            CompoundTag tileData = sbte.getTileData();
            if (sbte instanceof IStyledStandardBogeyTileEntity ssbte)
                ssbte.setBogeyStyle(tileData, style);
        });
    }

    public static void switchBogeyDirection(SceneBuilder scene, BlockPos pos, boolean isFacingForwards) {
        scene.world.modifyTileEntity(pos, StandardBogeyTileEntity.class, sbte -> {
            CompoundTag tileData = sbte.getTileData();
            if (sbte instanceof IStyledStandardBogeyTileEntity ssbte)
                ssbte.setIsFacingForwards(tileData, isFacingForwards);
        });
    }

    public static void switchBogeyPaintColour(SceneBuilder scene, BlockPos pos, DyeColor colour) {
        scene.world.modifyTileEntity(pos, StandardBogeyTileEntity.class, sbte -> {
            CompoundTag tileData = sbte.getTileData();
            if (sbte instanceof IStyledStandardBogeyTileEntity ssbte)
                ssbte.setPaintColour(tileData, colour);
        });
    }
}
