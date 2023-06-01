package com.rabbitminers.extendedbogeys.data;

import com.google.gson.JsonElement;
import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.simibubi.create.foundation.data.LangPartial;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.simibubi.create.foundation.utility.Lang;

import java.util.function.Supplier;

public enum ExtendedBogeysLanguageProvider implements LangPartial {
    EN_US("en_us"),

    ;

    private final String display;
    private final Supplier<JsonElement> provider;

    private ExtendedBogeysLanguageProvider(String display) {
        this.display = display;
        this.provider = this::fromResource;
    }

    private ExtendedBogeysLanguageProvider(String display, Supplier<JsonElement> customProvider) {
        this.display = display;
        this.provider = customProvider;
    }

    public String getDisplayName() {
        return display;
    }

    public JsonElement provide() {
        return provider.get();
    }

    private JsonElement fromResource() {
        String fileName = Lang.asId(name());
        String filepath = "assets/" + ExtendedBogeys.MOD_ID + "/lang/default/" + fileName + ".json";
        JsonElement element = FilesHelper.loadJsonResource(filepath);
        if (element == null)
            throw new IllegalStateException(String.format("Could not find default lang file: %s", filepath));
        return element;
    }
}
