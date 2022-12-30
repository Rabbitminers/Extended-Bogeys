package com.rabbitminers.extendedbogeys.mixin;

import com.rabbitminers.extendedbogeys.bogey.CustomCarriageBogey;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.components.structureMovement.AssemblyException;
import com.simibubi.create.content.logistics.trains.*;
import com.simibubi.create.content.logistics.trains.entity.*;
import com.simibubi.create.content.logistics.trains.management.edgePoint.TrackTargetingBehaviour;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.GlobalStation;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.content.logistics.trains.track.StandardBogeyTileEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.networking.AllPackets;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(StationTileEntity.class)
public abstract class MixinStationTileEntity extends SmartTileEntity {

    public MixinStationTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow public abstract void refreshAssemblyInfo();

    @Shadow private int[] bogeyLocations;

    @Shadow protected abstract void exception(AssemblyException exception, int carriage);

    @Shadow public TrackTargetingBehaviour<GlobalStation> edgePoint;

    @Shadow private IBogeyBlock[] bogeyTypes;

    @Shadow private Direction assemblyDirection;

    @Shadow private int assemblyLength;

    @Shadow private int bogeyCount;

    @Shadow public Component lastDisassembledTrainName;

    @Shadow protected abstract void clearException();

    @Shadow @Nullable public abstract GlobalStation getStation();


    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void assemble(UUID playerUUID) {
        refreshAssemblyInfo();

        if (bogeyLocations == null)
            return;

        if (bogeyLocations[0] != 0) {
            exception(new AssemblyException(Lang.translateDirect("train_assembly.frontmost_bogey_at_station")), -1);
            return;
        }

        if (!edgePoint.hasValidTrack())
            return;

        BlockPos trackPosition = edgePoint.getGlobalPosition();
        BlockState trackState = edgePoint.getTrackBlockState();
        ITrackBlock track = edgePoint.getTrack();
        BlockPos bogeyOffset = new BlockPos(track.getUpNormal(level, trackPosition, trackState));

        TrackNodeLocation location = null;
        Vec3 centre = Vec3.atBottomCenterOf(trackPosition)
                .add(0, track.getElevationAtCenter(level, trackPosition, trackState), 0);
        Collection<TrackNodeLocation.DiscoveredLocation> ends = track.getConnected(level, trackPosition, trackState, true, null);
        Vec3 targetOffset = Vec3.atLowerCornerOf(assemblyDirection.getNormal());
        for (TrackNodeLocation.DiscoveredLocation end : ends)
            if (Mth.equal(0, targetOffset.distanceToSqr(end.getLocation()
                    .subtract(centre)
                    .normalize())))
                location = end;
        if (location == null)
            return;

        List<Double> pointOffsets = new ArrayList<>();
        int iPrevious = -100;
        for (int i = 0; i < bogeyLocations.length; i++) {
            int loc = bogeyLocations[i];
            if (loc == -1)
                break;

            if (loc - iPrevious < 3) {
                exception(new AssemblyException(Lang.translateDirect("train_assembly.bogeys_too_close", i, i + 1)), -1);
                return;
            }

            double bogeySize = bogeyTypes[i].getWheelPointSpacing();
            pointOffsets.add(loc + .5 - bogeySize / 2);
            pointOffsets.add(loc + .5 + bogeySize / 2);
            iPrevious = loc;
        }

        List<TravellingPoint> points = new ArrayList<>();
        Vec3 directionVec = Vec3.atLowerCornerOf(assemblyDirection.getNormal());
        TrackGraph graph = null;
        TrackNode secondNode = null;

        for (int j = 0; j < assemblyLength * 2 + 40; j++) {
            double i = j / 2d;
            if (points.size() == pointOffsets.size())
                break;

            TrackNodeLocation currentLocation = location;
            location = new TrackNodeLocation(location.getLocation()
                    .add(directionVec.scale(.5))).in(location.dimension);

            if (graph == null)
                graph = Create.RAILWAYS.getGraph(level, currentLocation);
            if (graph == null)
                continue;
            TrackNode node = graph.locateNode(currentLocation);
            if (node == null)
                continue;

            for (int pointIndex = points.size(); pointIndex < pointOffsets.size(); pointIndex++) {
                double offset = pointOffsets.get(pointIndex);
                if (offset > i)
                    break;
                double positionOnEdge = i - offset;

                Map<TrackNode, TrackEdge> connectionsFromNode = graph.getConnectionsFrom(node);

                if (secondNode == null)
                    for (Map.Entry<TrackNode, TrackEdge> entry : connectionsFromNode.entrySet()) {
                        TrackEdge edge = entry.getValue();
                        TrackNode otherNode = entry.getKey();
                        if (edge.isTurn())
                            continue;
                        Vec3 edgeDirection = edge.getDirection(true);
                        if (Mth.equal(edgeDirection.normalize()
                                .dot(directionVec), -1d))
                            secondNode = otherNode;
                    }

                if (secondNode == null) {
                    Create.LOGGER.warn("Cannot assemble: No valid starting node found");
                    return;
                }

                TrackEdge edge = connectionsFromNode.get(secondNode);

                if (edge == null) {
                    Create.LOGGER.warn("Cannot assemble: Missing graph edge");
                    return;
                }

                points.add(new TravellingPoint(node, secondNode, edge, positionOnEdge));
            }

            secondNode = node;
        }

        if (points.size() != pointOffsets.size()) {
            Create.LOGGER.warn("Cannot assemble: Not all Points created");
            return;
        }

        if (points.size() == 0) {
            exception(new AssemblyException(Lang.translateDirect("train_assembly.no_bogeys")), -1);
            return;
        }

        List<CarriageContraption> contraptions = new ArrayList<>();
        List<Carriage> carriages = new ArrayList<>();
        List<Integer> spacing = new ArrayList<>();
        boolean atLeastOneForwardControls = false;

        for (int bogeyIndex = 0; bogeyIndex < bogeyCount; bogeyIndex++) {
            int pointIndex = bogeyIndex * 2;
            if (bogeyIndex > 0)
                spacing.add(bogeyLocations[bogeyIndex] - bogeyLocations[bogeyIndex - 1]);
            CarriageContraption contraption = new CarriageContraption(assemblyDirection);
            BlockPos bogeyPosOffset = trackPosition.offset(bogeyOffset);

            try {
                int offset = bogeyLocations[bogeyIndex] + 1;
                boolean success = contraption.assemble(level, bogeyPosOffset.relative(assemblyDirection, offset));
                atLeastOneForwardControls |= contraption.hasForwardControls();
                contraption.setSoundQueueOffset(offset);
                if (!success) {
                    exception(new AssemblyException(Lang.translateDirect("train_assembly.nothing_attached", bogeyIndex + 1)),
                            -1);
                    return;
                }
            } catch (AssemblyException e) {
                exception(e, contraptions.size() + 1);
                return;
            }

            BlockPos firstBogeyPos = contraption.anchor;
            int firstBogeyStyle = 0;
            if (level != null) {
                BlockEntity te = level.getBlockEntity(firstBogeyPos);
                if (!(te instanceof StandardBogeyTileEntity standardBogeyTileEntity))
                    return;
                CompoundTag td = standardBogeyTileEntity.getTileData();
                firstBogeyStyle = td.contains("type") ? td.getInt("type") : 0;
            }
            IBogeyBlock typeOfFirstBogey = bogeyTypes[bogeyIndex];
            CustomCarriageBogey firstBogey =
                    new CustomCarriageBogey(typeOfFirstBogey, points.get(pointIndex), points.get(pointIndex + 1), firstBogeyStyle);
            CustomCarriageBogey secondBogey = null;
            BlockPos secondBogeyPos = contraption.getSecondBogeyPos();
            int bogeySpacing = 0;

            if (secondBogeyPos != null) {
                if (bogeyIndex == bogeyCount - 1 || !secondBogeyPos
                        .equals(bogeyPosOffset.relative(assemblyDirection, bogeyLocations[bogeyIndex + 1] + 1))) {
                    exception(new AssemblyException(Lang.translateDirect("train_assembly.not_connected_in_order")),
                            contraptions.size() + 1);
                    return;
                }

                int secondBogeyStyle = 0;
                if (level != null) {
                    BlockEntity te = level.getBlockEntity(secondBogeyPos);
                    if (!(te instanceof StandardBogeyTileEntity standardBogeyTileEntity))
                        return;
                    CompoundTag td = standardBogeyTileEntity.getTileData();
                    secondBogeyStyle = td.contains("type") ? td.getInt("type") : 0;
                }

                bogeySpacing = bogeyLocations[bogeyIndex + 1] - bogeyLocations[bogeyIndex];
                secondBogey = new CustomCarriageBogey(bogeyTypes[bogeyIndex + 1], points.get(pointIndex + 2),
                        points.get(pointIndex + 3), secondBogeyStyle);
                bogeyIndex++;

            } else if (!typeOfFirstBogey.allowsSingleBogeyCarriage()) {
                exception(new AssemblyException(Lang.translateDirect("train_assembly.single_bogey_carriage")),
                        contraptions.size() + 1);
                return;
            }

            contraptions.add(contraption);
            carriages.add(new Carriage(firstBogey, secondBogey, bogeySpacing));
        }

        if (!atLeastOneForwardControls) {
            exception(new AssemblyException(Lang.translateDirect("train_assembly.no_controls")), -1);
            return;
        }

        for (CarriageContraption contraption : contraptions) {
            contraption.removeBlocksFromWorld(level, BlockPos.ZERO);
            contraption.expandBoundsAroundAxis(Direction.Axis.Y);
        }

        Train train = new Train(UUID.randomUUID(), playerUUID, graph, carriages, spacing, contraptions.stream()
                .anyMatch(CarriageContraption::hasBackwardControls));

        if (lastDisassembledTrainName != null) {
            train.name = lastDisassembledTrainName;
            lastDisassembledTrainName = null;
        }

        for (int i = 0; i < contraptions.size(); i++) {
            CarriageContraption contraption = contraptions.get(i);
            Carriage carriage = carriages.get(i);
            carriage.setContraption(level, contraption);
            if (contraption.containsBlockBreakers())
                award(AllAdvancements.CONTRAPTION_ACTORS);
        }

        GlobalStation station = getStation();
        if (station != null) {
            train.setCurrentStation(station);
            station.reserveFor(train);
        }

        train.collectInitiallyOccupiedSignalBlocks();
        Create.RAILWAYS.addTrain(train);
        AllPackets.channel.send(PacketDistributor.ALL.noArg(), new TrainPacket(train, true));
        clearException();

        award(AllAdvancements.TRAIN);
        if (contraptions.size() >= 6)
            award(AllAdvancements.LONG_TRAIN);
    }


}
