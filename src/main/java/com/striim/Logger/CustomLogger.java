package com.striim.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private CustomLogger customLogger;
    private final Logger logger;

    private CustomLogger() {
        logger = LoggerFactory.getLogger("Application");

    }

    public CustomLogger getLogger() {
        try {
            if (customLogger == null) {
                customLogger = new CustomLogger();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customLogger;
    }
}
