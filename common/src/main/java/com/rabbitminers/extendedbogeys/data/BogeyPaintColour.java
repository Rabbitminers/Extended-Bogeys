package com.rabbitminers.extendedbogeys.data;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;

import java.util.Optional;

/*
Replacement for DyeColor Enum to be able to handle null values
 */
public enum BogeyPaintColour {
    UNPAINTED("unpainted", 16383998),
    WHITE("white", 16383998, DyeColor.WHITE),
    ORANGE("orange", 16738335, DyeColor.ORANGE),
    MAGENTA("magenta",  16711935, DyeColor.MAGENTA),
    LIGHT_BLUE("lightblue",  10141901, DyeColor.LIGHT_BLUE),
    YELLOW("yellow",  16776960, DyeColor.YELLOW),
    LIME("lime", 12582656, DyeColor.LIME),
    PINK("pink", 16738740, DyeColor.PINK),
    GRAY("gray",  8421504, DyeColor.GRAY),
    LIGHT_GRAY("lightgray", 13882323, DyeColor.LIGHT_GRAY),
    CYAN("cyan", 65535, DyeColor.CYAN),
    PURPLE("purple",  10494192, DyeColor.PURPLE),
    BLUE("blue",  255, DyeColor.BLUE),
    BROWN("brown", 9127187, DyeColor.BROWN),
    GREEN("green", 65280, DyeColor.GREEN),
    RED("red", 16711680, DyeColor.RED),
    BLACK("black", 0, DyeColor.BLACK);

    public final String name;
    public final int textColor;
    public final Optional<DyeColor> dyeColour;

    BogeyPaintColour(String name, int textColor, DyeColor dyeColour) {
        this.name = name;
        this.textColor = textColor;
        this.dyeColour = Optional.of(dyeColour);
    }

    BogeyPaintColour(String name, int textColor) {
        this.name = name;
        this.textColor = textColor;
        this.dyeColour = Optional.empty();
    }

    public static BogeyPaintColour of(DyeColor dyeColour) {
        for (BogeyPaintColour value : values()) {
            if (value.dyeColour.isPresent() && value.dyeColour.get() == dyeColour)
                return value;
        }
        return BogeyPaintColour.UNPAINTED;
    }
}
