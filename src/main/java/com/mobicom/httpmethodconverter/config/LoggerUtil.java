package com.mobicom.httpmethodconverter.config;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

/**
 *
 * @author uitumen.t
 */
public class LoggerUtil {

    private static Logger logger = null;

    public static void initialLogger(String loggerPath) throws IOException {

        Configurator.initialize(null, loggerPath);

        ThreadContext.put("rid", " httpMethodConverter");
        logger = LogManager.getRootLogger();
        logger.info("Logger successfully loaded.");
    }

    public static Logger getLogger() {
        return logger;
    }
}
