package com.rabbitminers.extendedbogeys.bogey.styles;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;
import com.rabbitminers.extendedbogeys.bogey.styles.content.DefaultStyle;

import java.util.*;

public class BogeyStyles {
    private static Map<Integer, Class<? extends IBogeyStyle>> bogeyStyleMap = new HashMap<>();
    public static void addBogeyStyle(Class<? extends IBogeyStyle> customBogeyStyle, String modId) {
        bogeyStyleMap.put(bogeyStyleMap.size(), customBogeyStyle);
    }
    public static Collection<Class<? extends IBogeyStyle>> getAllBogeyStyles() {
        List<IBogeyStyle> bogeyStyleList = new ArrayList<>();
        for (int i = 0; i < bogeyStyleMap.size(); i++)
            bogeyStyleList.add(getBogeyStyle(i));
        return bogeyStyleMap.values();
    }
    public static int getNumberOfBogeyStyleVariations() {
        return bogeyStyleMap.size();
    }
    public static IBogeyStyle getBogeyStyle(int id) {
        try {
            Class<? extends IBogeyStyle> cls = bogeyStyleMap.get(id);
            if (cls == null) return DefaultStyle.class.newInstance();
            return (IBogeyStyle) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            ExtendedBogeys.LOGGER.error("Invalid bogey style added, this may be due to a broken mod, contact the author");
            return new DefaultStyle();
        }
    }

    // Serialise Load Order Of Mods

}
