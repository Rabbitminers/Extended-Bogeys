package com.rabbitminers.extendedbogeys.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.RegistrateLangProvider;

import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class ExtendedBogeysLanguageProvider {
    private static final String PATH = "assets/extendedbogeys/lang/default/";

    public static void generate(RegistrateLangProvider provider) {
        BiConsumer<String, String> langConsumer = provider::add;

        provideDefaultLang("en_us", langConsumer);
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = formatPath(fileName);
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            consumer.accept(key, value);
        }
    }

    private static String formatPath(String fileName) {
        return PATH + fileName + ".json";
    }
}
