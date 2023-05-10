package com.rabbitminers.extendedbogeys.client;

import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ExtendedBogeysClientEvents {
    private static CarriageContraptionEntity carriageContraptionEntity;

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if(!isGameActive())
            return;

        ClientLevel level = Minecraft.getInstance().level;
        if (event.phase == TickEvent.Phase.START)
            return;

        CameraRotationModifier.tick();

        if (CameraRotationModifier.shouldApply()) {
            float xRot = carriageContraptionEntity.yaw;
            float yRot = carriageContraptionEntity.pitch;

            CameraRotationModifier.rotateTowards(new Vec2(xRot, yRot));
        }
    }


    @SubscribeEvent
    public static void onMount(EntityMountEvent event) {
        if (event.getEntityMounting() != Minecraft.getInstance().player)
            return;

        if (event.isDismounting()) {
            CameraRotationModifier.reset();
            return;
        }

        if (!(event.getEntityBeingMounted() instanceof CarriageContraptionEntity carriage) || !event.isMounting())
            return;

        carriageContraptionEntity = carriage;

        float xRot = carriage.yaw;
        float yRot = carriage.pitch;

        CameraRotationModifier.setShouldApply(true);
        CameraRotationModifier.rotateTowards(new Vec2(xRot, yRot));
    }



    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
