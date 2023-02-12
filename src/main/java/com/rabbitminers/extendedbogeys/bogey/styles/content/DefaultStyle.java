package com.rabbitminers.extendedbogeys.bogey.styles.content;

import com.rabbitminers.extendedbogeys.bogey.sizes.BogeySize;
import com.rabbitminers.extendedbogeys.bogey.styles.IBogeyStyle;
import com.rabbitminers.extendedbogeys.bogey.util.LanguageKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultStyle implements IBogeyStyle {

    @Override
    public String getStyleName() {
        return LanguageKey.translateDirect("bogeys.styles.default").getString();
    }

    @Override
    public boolean shouldRenderDefault(boolean isLarge) {
        return true;
    }

    @Override
    public List<BogeySize> implemntedSizes() {
        return Arrays.asList(BogeySize.SMALL, BogeySize.LARGE);
    }
}
