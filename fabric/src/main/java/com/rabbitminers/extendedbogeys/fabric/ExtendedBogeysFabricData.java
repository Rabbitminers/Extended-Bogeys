package com.rabbitminers.extendedbogeys.fabric;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class ExtendedBogeysFabricData implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        Path resources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(resources), Set.of("create"), false, null, null
        );
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        ExtendedBogeys.registrate().setupDatagen(pack, helper);
        ExtendedBogeys.gatherData(pack);
    }
}
