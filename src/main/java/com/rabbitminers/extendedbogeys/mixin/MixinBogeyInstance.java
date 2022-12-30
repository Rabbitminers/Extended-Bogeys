package com.rabbitminers.extendedbogeys.mixin;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.rabbitminers.extendedbogeys.bogey.CustomCarriageBogey;
import com.rabbitminers.extendedbogeys.index.BogeyPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.logistics.trains.RailwaySavedData;
import com.simibubi.create.content.logistics.trains.entity.BogeyInstance;
import com.simibubi.create.content.logistics.trains.entity.Carriage;
import com.simibubi.create.content.logistics.trains.entity.CarriageBogey;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(BogeyInstance.Frame.class)
public class MixinBogeyInstance {
    @Mutable
    @Shadow
    @Final
    private ModelData frame;
    @Mutable
    @Shadow
    @Final
    private ModelData[] wheels;

    private int style = 0;
    private ModelData test;

    public void renderCustom(MaterialManager materialManager, PartialModel frameModel, PartialModel wheelModel) {
        frame = materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(frameModel)
                .createInstance();

        wheels = new ModelData[2];

        materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(wheelModel)
                .createInstances(wheels);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void Frame(CarriageBogey bogey, MaterialManager materialManager, CallbackInfo ci) {
        boolean isFrontBogey = (bogey == bogey.carriage.bogeys.get(true));

        System.out.println(bogey instanceof CustomCarriageBogey);

        ClientLevel level = Minecraft.getInstance().level;
        assert level != null;
        MinecraftServer server = level.getServer() != null
            ? level.getServer()
            : Minecraft.getInstance().getSingleplayerServer();

        if (server == null)
            return;

        RailwaySavedData sd = RailwaySavedData.load(server);
        CompoundTag nbt = sd.save(new CompoundTag());
        Train train = bogey.carriage.train;
        NBTHelper.iterateCompoundList(nbt.getList("Trains", Tag.TAG_COMPOUND), trainCompoundTagData -> {
            if (trainCompoundTagData.getUUID("Id").equals(train.id)) {
                NBTHelper.iterateCompoundList(trainCompoundTagData.getList("Carriages", Tag.TAG_COMPOUND), carriageCompoundTagData -> {
                    CompoundTag entity = carriageCompoundTagData.getCompound("Entity");
                    int index = entity.getInt("CarriageIndex");
                    List<Carriage> carriageList = bogey.carriage.train.carriages;
                    Carriage currentCarriage = carriageList.get(index);
                    if (currentCarriage == bogey.carriage) {
                        CompoundTag bogeyData = isFrontBogey
                                ? carriageCompoundTagData.getCompound("FirstBogey")
                                : carriageCompoundTagData.contains("SecondBogey")
                                ? carriageCompoundTagData.getCompound("SecondBogey")
                                : null;
                        if (bogeyData != null)
                            this.style = bogeyData.getInt("Style");
                    }
                });
            }
        });


        switch (this.style) {
            case 0 -> renderCustom(materialManager, AllBlockPartials.BOGEY_FRAME, AllBlockPartials.LARGE_BOGEY_WHEELS);
            case 1 -> renderCustom(materialManager, AllBlockPartials.BLAZE_ACTIVE, AllBlockPartials.BLAZE_BURNER_FLAME);
            case 2 -> renderCustom(materialManager, AllBlockPartials.ANALOG_LEVER_INDICATOR, AllBlockPartials.BLAZE_ACTIVE);
            case 3 -> renderCustom(materialManager, AllBlockPartials.BLAZE_BURNER_RODS, AllBlockPartials.BLAZE_BURNER_SUPER_RODS);
            case 4 -> renderCustom(materialManager, AllBlockPartials.BOGEY_FRAME, AllBlockPartials.BOGEY_FRAME);
            case 5 -> renderCustom(materialManager, AllBlockPartials.COUPLING_CONNECTOR, AllBlockPartials.BOGEY_DRIVE);
        }

        test = materialManager.defaultSolid().material(Materials.TRANSFORMED)
                .getModel(BogeyPartials.TWO_WHEEL_BOGEY_ROD)
                .createInstance();
    }

    @Inject(at = @At("HEAD"), method = "beginFrame", remap = false)
    public void beginFrame(float wheelAngle, PoseStack ms, CallbackInfo ci) {
        if (ms == null) {
            test.setEmptyTransform();
            return;
        }

        test.setTransform(ms)
                .translate(0, 0, 0)
                .rotateX(wheelAngle)
                .translate(0, 1 / 4f, 0)
                .rotateX(-wheelAngle);
    }

    @Inject(at = @At("HEAD"), method = "updateLight", remap = false)
    public void updateLight(int blockLight, int skyLight, CallbackInfo ci) {
        System.out.println("Test + " + this.style);
        test.setBlockLight(blockLight).setSkyLight(skyLight);
    }

    @Inject(at = @At("TAIL"), method = "remove", remap = false)
    public void remove(CallbackInfo callbackInfo) {
        test.delete();
    }
}
