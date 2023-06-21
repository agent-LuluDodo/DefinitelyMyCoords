package de.luludodo.dmc.coords;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.TranslatableOption;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.Comparator;

@Environment(value= EnvType.CLIENT)
public enum Mode implements TranslatableOption {
    RELATIVE(0, "options.dmc.relative"),
    ABSOLUTE(1, "options.dmc.absolute"),
    CUSTOM(2, "options.dmc.custom");

    private static final Mode[] VALUES;
    private final int id;
    private final String translationKey;

    Mode(int id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    public static Mode valueOfName(String name) {
        for (Mode mode:VALUES) {
            if (mode.toString().equals(name)) {
                return mode;
            }
        }
        return null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    public String toString() {
        return switch (this) {
            case RELATIVE -> "relative";
            case ABSOLUTE -> "absolute";
            case CUSTOM -> "custom";
        };
    }

    public static Mode byId(int id) {
        return VALUES[MathHelper.floorMod(id, VALUES.length)];
    }

    static {
        VALUES = Arrays.stream(Mode.values()).sorted(Comparator.comparingInt(Mode::getId)).toArray(Mode[]::new);
    }
}
