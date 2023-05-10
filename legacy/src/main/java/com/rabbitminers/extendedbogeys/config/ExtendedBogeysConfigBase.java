package com.rabbitminers.extendedbogeys.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ExtendedBogeysConfigBase {
    public ForgeConfigSpec specification;

    protected int depth;
    protected List<ExtendedBogeysConfigBase.CValue<?, ?>> allValues;
    protected List<ExtendedBogeysConfigBase> children;

    protected void registerAll(final ForgeConfigSpec.Builder builder) {
        for (ExtendedBogeysConfigBase.CValue<?, ?> cValue : allValues)
            cValue.register(builder);
    }

    public void onLoad() {
        if (children != null)
            children.forEach(ExtendedBogeysConfigBase::onLoad);
    }

    public void onReload() {
        if (children != null)
            children.forEach(ExtendedBogeysConfigBase::onReload);
    }

    public abstract String getName();

    @FunctionalInterface
    protected interface IValueProvider<V, T extends ForgeConfigSpec.ConfigValue<V>>
            extends Function<ForgeConfigSpec.Builder, T> {
    }

    protected ExtendedBogeysConfigBase.ConfigBool b(boolean current, String name, String... comment) {
        return new ExtendedBogeysConfigBase.ConfigBool(name, current, comment);
    }

    protected ExtendedBogeysConfigBase.ConfigFloat f(float current, float min, float max, String name, String... comment) {
        return new ExtendedBogeysConfigBase.ConfigFloat(name, current, min, max, comment);
    }

    protected ExtendedBogeysConfigBase.ConfigFloat f(float current, float min, String name, String... comment) {
        return f(current, min, Float.MAX_VALUE, name, comment);
    }

    protected ExtendedBogeysConfigBase.ConfigInt i(int current, int min, int max, String name, String... comment) {
        return new ExtendedBogeysConfigBase.ConfigInt(name, current, min, max, comment);
    }

    protected ExtendedBogeysConfigBase.ConfigInt i(int current, int min, String name, String... comment) {
        return i(current, min, Integer.MAX_VALUE, name, comment);
    }

    protected ExtendedBogeysConfigBase.ConfigInt i(int current, String name, String... comment) {
        return i(current, Integer.MIN_VALUE, Integer.MAX_VALUE, name, comment);
    }

    protected <T extends Enum<T>> ExtendedBogeysConfigBase.ConfigEnum<T> e(T defaultValue, String name, String... comment) {
        return new ExtendedBogeysConfigBase.ConfigEnum<>(name, defaultValue, comment);
    }

    protected ExtendedBogeysConfigBase.ConfigGroup group(int depth, String name, String... comment) {
        return new ExtendedBogeysConfigBase.ConfigGroup(name, depth, comment);
    }

    protected <T extends ExtendedBogeysConfigBase> T nested(int depth, Supplier<T> constructor, String... comment) {
        T config = constructor.get();
        new ExtendedBogeysConfigBase.ConfigGroup(config.getName(), depth, comment);
        new ExtendedBogeysConfigBase.CValue<Boolean, ForgeConfigSpec.BooleanValue>(config.getName(), builder -> {
            config.depth = depth;
            config.registerAll(builder);
            if (config.depth > depth)
                builder.pop(config.depth - depth);
            return null;
        });
        if (children == null)
            children = new ArrayList<>();
        children.add(config);
        return config;
    }

    public class CValue<V, T extends ForgeConfigSpec.ConfigValue<V>> {
        protected ForgeConfigSpec.ConfigValue<V> value;
        protected String name;
        private final ExtendedBogeysConfigBase.IValueProvider<V, T> provider;

        public CValue(String name, ExtendedBogeysConfigBase.IValueProvider<V, T> provider, String... comment) {
            this.name = name;
            this.provider = builder -> {
                addComments(builder, comment);
                return provider.apply(builder);
            };
            if (allValues == null)
                allValues = new ArrayList<>();
            allValues.add(this);
        }

        public void addComments(ForgeConfigSpec.Builder builder, String... comment) {
            if (comment.length > 0) {
                String[] comments = new String[comment.length + 1];
                comments[0] = " ";
                System.arraycopy(comment, 0, comments, 1, comment.length);
                builder.comment(comments);
            } else
                builder.comment(" ");
        }

        public void register(ForgeConfigSpec.Builder builder) {
            value = provider.apply(builder);
        }

        public V get() {
            return value.get();
        }

        public void set(V value) {
            this.value.set(value);
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Marker for config subgroups
     */
    public class ConfigGroup extends ExtendedBogeysConfigBase.CValue<Boolean, ForgeConfigSpec.BooleanValue> {

        private final int groupDepth;
        private final String[] comment;

        public ConfigGroup(String name, int depth, String... comment) {
            super(name, builder -> null, comment);
            groupDepth = depth;
            this.comment = comment;
        }

        @Override
        public void register(ForgeConfigSpec.Builder builder) {
            if (depth > groupDepth)
                builder.pop(depth - groupDepth);
            depth = groupDepth;
            addComments(builder, comment);
            builder.push(getName());
            depth++;
        }

    }

    public class ConfigBool extends ExtendedBogeysConfigBase.CValue<Boolean, ForgeConfigSpec.BooleanValue> {

        public ConfigBool(String name, boolean def, String... comment) {
            super(name, builder -> builder.define(name, def), comment);
        }
    }

    public class ConfigEnum<T extends Enum<T>> extends ExtendedBogeysConfigBase.CValue<T, ForgeConfigSpec.EnumValue<T>> {

        public ConfigEnum(String name, T defaultValue, String[] comment) {
            super(name, builder -> builder.defineEnum(name, defaultValue), comment);
        }

    }

    public class ConfigFloat extends ExtendedBogeysConfigBase.CValue<Double, ForgeConfigSpec.DoubleValue> {

        public ConfigFloat(String name, float current, float min, float max, String... comment) {
            super(name, builder -> builder.defineInRange(name, current, min, max), comment);
        }

        public float getF() {
            return get().floatValue();
        }
    }

    public class ConfigInt extends ExtendedBogeysConfigBase.CValue<Integer, ForgeConfigSpec.IntValue> {

        public ConfigInt(String name, int current, int min, int max, String... comment) {
            super(name, builder -> builder.defineInRange(name, current, min, max), comment);
        }
    }

}
