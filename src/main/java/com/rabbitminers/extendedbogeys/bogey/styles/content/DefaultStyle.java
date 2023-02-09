package com.rabbitminers.extendedbogeys.bogey.styles.content;

import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;

public class DefaultStyle implements IBogeyStyle {
    @Override
    public String getStyleName() {
        return LanguageKey.translateDirect("bogeys.styles.default").getString();
    }

    @Override
    public boolean shouldRenderDefault(boolean isLarge) {
        return true;
    }
}
