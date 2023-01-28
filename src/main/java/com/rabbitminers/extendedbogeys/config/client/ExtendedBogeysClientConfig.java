package com.rabbitminers.extendedbogeys.config.client;

import com.rabbitminers.extendedbogeys.ExtendedBogeysClient;
import com.rabbitminers.extendedbogeys.config.ExtendedBogeysConfigBase;
import com.rabbitminers.extendedbogeys.config.common.ExtendedBogeysCommonConfig;

public class ExtendedBogeysClientConfig extends ExtendedBogeysConfigBase {
    @Override
    public String getName() {
        return "client";
    }

    public final ConfigGroup client = group(0, "common",
            ExtendedBogeysClientConfig.Comments.client);
    public final ConfigBool displayBogeySelectionOnHover = b(true, "enableBogeySelectionOnHover",
            Comments.client);

    private static class Comments {
        static String client = "Client-only settings - If you're looking for general settings, look inside your worlds serverconfig folder!";
    }
}
