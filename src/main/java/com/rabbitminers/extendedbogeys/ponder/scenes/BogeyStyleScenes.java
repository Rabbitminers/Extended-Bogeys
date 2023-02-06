package com.rabbitminers.extendedbogeys.ponder.scenes;

import com.rabbitminers.extendedbogeys.mixin_interface.IStyledStandardBogeyTileEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.content.trains.TrainStationScenes;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

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

        scene.overlay.showControls(new InputWindowElement(util.vector.topOf(3, 2, 6), Pointing.RIGHT).rightClick(), 15);
        switchBogeyStyle(scene, new BlockPos(3, 2, 6), 2);
        scene.overlay.showText(50)
                .pointAt(util.vector.topOf(3, 0, 6))
                .placeNearTarget()
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Click the track again with ye wrench to cycle between bogey designs");
        scene.idle(30);
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
