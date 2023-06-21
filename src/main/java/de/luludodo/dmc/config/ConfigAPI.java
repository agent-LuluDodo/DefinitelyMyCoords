package de.luludodo.dmc.config;

import de.luludodo.dmc.client.DefinitelyMyCoordsClient;
import de.luludodo.dmc.coords.Mode;

public class ConfigAPI {
    public static Mode getMode() {
        return (Mode) DefinitelyMyCoordsClient.config.get("mode");
    }

    public static int getOffsetX() {
        return (int) DefinitelyMyCoordsClient.config.get("offset-x");
    }

    public static int getOffsetY() {
        return (int) DefinitelyMyCoordsClient.config.get("offset-y");
    }

    public static int getOffsetZ() {
        return (int) DefinitelyMyCoordsClient.config.get("offset-z");
    }

    public static boolean getObscureRotations() {
        return (boolean) DefinitelyMyCoordsClient.config.get("obscure-rotations");
    }

    public static int getOffsetRotations() {
        return (int) DefinitelyMyCoordsClient.config.get("offset-rotations");
    }
    public static void setMode(Mode mode) {
        DefinitelyMyCoordsClient.config.set("mode", mode);
    }

    public static void setOffsetX(int offsetX) {
        DefinitelyMyCoordsClient.config.set("offset-x", offsetX);
    }

    public static void setOffsetY(int offsetY) {
        DefinitelyMyCoordsClient.config.set("offset-y", offsetY);
    }

    public static void setOffsetZ(int offsetZ) {
        DefinitelyMyCoordsClient.config.set("offset-z", offsetZ);
    }

    public static void setObscureRotations(boolean obscureRotations) {
        DefinitelyMyCoordsClient.config.set("obscure-rotations", obscureRotations);
    }

    public static void setOffsetRotations(int offsetRotations) {
        DefinitelyMyCoordsClient.config.set("offset-rotations", offsetRotations);
    }
}
