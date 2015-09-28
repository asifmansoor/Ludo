package com.logicanvas.frameworks.boardgamesgdk.core.utility;


import com.logicanvas.frameworks.boardgamesgdk.core.config.BasicGameConfiguration;
import playn.core.Log;

/**
 * Created by amansoor on 24-08-2015.
 */
public class BoardGamesLogger {

    private static Log log;

    public static void initialize(Log frameworkLog) {
        log = frameworkLog;
    }
    public static void log(String logStr) {
        System.out.println(logStr);
    }

    public static void info(String logStr) {
        if (log != null) {
            log.info(logStr);
        } else {
            System.out.println("[INFO]: " + logStr);
        }
    }

    public static void warn(String logStr) {
        if (log != null) {
            log.warn(logStr);
        } else {
            System.out.println("[WARN]: " + logStr);
        }
    }

    public static void error(String logStr) {
        if (log != null) {
            log.error(logStr);
        } else {
            System.out.println("[ERROR]: " + logStr);
        }
    }

    public static void debug(String logStr) {
        if (BasicGameConfiguration.DEBUG) {
            if (log != null) {
                log.info("[DEBUG]: " + logStr);
            } else {
                System.out.println("[DEBUG]: " + logStr);
            }
        }
    }

    public static void debug(Object obj) {
        if (BasicGameConfiguration.DEBUG) {
            if (log != null) {
                log.info("[DEBUG]: ", obj);
            } else {
                System.out.println("[DEBUG]: ");
                System.out.println(obj);
            }
        }
    }
}
