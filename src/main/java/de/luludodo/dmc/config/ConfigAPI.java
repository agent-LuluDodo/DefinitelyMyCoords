package de.luludodo.dmc.config;

import de.luludodo.dmc.client.DefinitelyMyCoordsClient;
import de.luludodo.dmc.coords.Mode;

public class ConfigAPI {
    public static Mode getMode() {
        return (Mode) DefinitelyMyCoordsClient.config.get("mode");
    }

    public static long getOffsetX() {
        return (long) DefinitelyMyCoordsClient.config.get("offset-x");
    }

    public static long getOffsetY() {
        return (long) DefinitelyMyCoordsClient.config.get("offset-y");
    }

    public static long getOffsetZ() {
        return (long) DefinitelyMyCoordsClient.config.get("offset-z");
    }

    public static boolean getObscureRotations() {
        return (boolean) DefinitelyMyCoordsClient.config.get("obscure-rotations");
    }

    public static void setMode(Mode mode) {
        DefinitelyMyCoordsClient.config.set("mode", mode);
    }

    public static void setOffsetX(long offsetX) {
        DefinitelyMyCoordsClient.config.set("offset-x", offsetX);
    }

    public static void setOffsetY(long offsetY) {
        DefinitelyMyCoordsClient.config.set("offset-y", offsetY);
    }

    public static void setOffsetZ(long offsetZ) {
        DefinitelyMyCoordsClient.config.set("offset-z", offsetZ);
    }

    public static void setObscureRotations(boolean obscureRotations) {
        DefinitelyMyCoordsClient.config.set("obscure-rotations", obscureRotations);
    }

    public static void setOffsetRotations(int offsetRotations) {
        DefinitelyMyCoordsClient.config.set("offset-rotations", offsetRotations);
    }
}
