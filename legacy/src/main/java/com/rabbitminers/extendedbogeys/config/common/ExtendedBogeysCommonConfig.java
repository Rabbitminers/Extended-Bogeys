package com.rabbitminers.extendedbogeys.config.common;

import com.rabbitminers.extendedbogeys.config.ExtendedBogeysConfigBase;

public class ExtendedBogeysCommonConfig extends ExtendedBogeysConfigBase {
    @Override
    public String getName() {
        return "common";
    }

    public final ConfigGroup client = group(0, "common",
            Comments.common);
    public final ConfigBool displayBogeySelectionOnHover = b(true, "enableBogeySelectionOnHover",
            Comments.display);
    private static class Comments {
        static String common = "Common settings - If you're looking for general settings, look inside your worlds serverconfig folder!";
        static String display = "Show all available styles of bogey when hovering over one";
    }
}
