package eu.felicianware.core.util;

import java.util.logging.Logger;

public class log {
    private static final Logger LOGGER = Logger.getLogger(log.class.getName());

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warning(String message) {
        LOGGER.warning(message);
    }

    public static void error(String message) {
        LOGGER.severe(message);
    }

    public static void severe(String message) {
        LOGGER.severe(message);
    }
}
