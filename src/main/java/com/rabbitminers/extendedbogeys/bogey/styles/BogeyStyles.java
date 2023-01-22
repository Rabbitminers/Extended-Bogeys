package com.rabbitminers.extendedbogeys.bogey.styles;

import com.rabbitminers.extendedbogeys.ExtendedBogeys;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BogeyStyles {
    private static Map<Integer, Class<? extends IBogeyStyle>> bogeyStyleMap = new HashMap<>();
    public static void addBogeyStyle(Class<? extends IBogeyStyle> customBogeyStyle) {
        bogeyStyleMap.put(bogeyStyleMap.size()-1, customBogeyStyle);
    }
    public static Collection<Class<? extends IBogeyStyle>> getAllBogeyStyles() {
        return bogeyStyleMap.values();
    }
    public static int getNumberOfBogeyStyleVariations() {
        return bogeyStyleMap.size();
    }
    public static IBogeyStyle getBogeyStyle(int id) {
        try {
            Class<? extends IBogeyStyle> cls = bogeyStyleMap.get(id);
            if (cls == null) return TwoWheelBogey.class.newInstance();
            return (IBogeyStyle) cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            ExtendedBogeys.LOGGER.error("Invalid bogey style added, this may be due to a broken mod");
            return new TwoWheelBogey();
        }
    }
}
