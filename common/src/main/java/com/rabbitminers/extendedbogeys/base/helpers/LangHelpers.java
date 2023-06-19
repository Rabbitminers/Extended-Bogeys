package com.rabbitminers.extendedbogeys.base.helpers;

import java.util.Arrays;
import java.util.stream.Collectors;

public class LangHelpers {
    public static String capitalize(String line) {
        return Arrays.stream(line.split("_"))
                .filter(word -> !word.isEmpty())
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    public static String removeUnderscores(String input) {
        return input.replace("_", "");
    }
}
