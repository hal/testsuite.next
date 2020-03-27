package org.jboss.hal.logging.profile.demo;

import org.jboss.logging.Logger;

public class LoggingExample {
    
    private static final Logger logger = Logger.getLogger(LoggingExample.class);

public static void logDebug() {
    logger.debug("DEBUG");
}
public static void logError() {
    logger.error("ERROR");
}
public static void logFatal() {
    logger.fatal("FATAL");
}
public static void logInfo() {
    logger.info("INFO");
}
public static void logTrace() {
    logger.trace("TRACE");
}
public static void logWarn() {
    logger.warn("WARN");
}

}