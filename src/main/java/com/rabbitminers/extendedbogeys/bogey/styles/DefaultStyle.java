package com.rabbitminers.extendedbogeys.bogey.styles;

public class DefaultStyle implements IBogeyStyle {
    @Override
    public String getStyleName() {
        // TODO: Lang keys for goodness sake
        return "Default";
    }

    @Override
    public boolean shouldRenderDefault(boolean isLarge) {
        return true;
    }
}
