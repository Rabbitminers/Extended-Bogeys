package com.rabbitminers.extendedbogeys.data;

/*
Replacement for DyeColor Enum to be able to handle null values
 */
public enum BogeyPaintColour {
    UNPAINTED("unpainted", 16383998),
    WHITE("white", 16383998),
    ORANGE("orange", 16738335),
    MAGENTA("magenta",  16711935),
    LIGHT_BLUE("light_blue",  10141901),
    YELLOW("yellow",  16776960),
    LIME("lime", 12582656),
    PINK("pink", 16738740),
    GRAY("gray",  8421504),
    LIGHT_GRAY("light_gray", 13882323),
    CYAN("cyan", 65535),
    PURPLE("purple",  10494192),
    BLUE("blue",  255),
    BROWN("brown", 9127187),
    GREEN("green", 65280),
    RED("red", 16711680),
    BLACK("black", 0);

    public final String name;
    public final int textColor;

    BogeyPaintColour(String name, int textColor) {
        this.name = name;
        this.textColor = textColor;
    }
}
