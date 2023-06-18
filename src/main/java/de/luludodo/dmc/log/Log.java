package de.luludodo.dmc.log;

import de.luludodo.dmc.client.DefinitelyMyCoordsClient;

public class Log {
    public static void info(Object o) {
        DefinitelyMyCoordsClient.log.info(o.toString());
    }

    public static void info(String s, Object... args) {
        DefinitelyMyCoordsClient.log.info(s.formatted(args));
    }

    public static void warn(Object o) {
        DefinitelyMyCoordsClient.log.warn(o.toString());
    }

    public static void warn(String s, Object... args) {
        DefinitelyMyCoordsClient.log.info(s.formatted(args));
    }

    public static void err(Object o) {
        DefinitelyMyCoordsClient.log.error(o.toString());
    }

    public static void err(String s, Object args) {
        DefinitelyMyCoordsClient.log.error(s.formatted(args));
    }
}
