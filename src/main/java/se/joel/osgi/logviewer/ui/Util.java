/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-30
 * Time: 23:14
 */
package se.joel.osgi.logviewer.ui;

import org.osgi.framework.Bundle;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilities for the UI.
 */
public class Util {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private static final Date date = new Date();


    private Util() {
    }

    public static String levelToString(LogEntry logEntry) {
        if (logEntry == null) {
            return "N/A";
        }

        switch (logEntry.getLevel()) {
            case LogService.LOG_DEBUG:
                return "DEBUG";
            case LogService.LOG_INFO:
                return "INFO";
            case LogService.LOG_WARNING:
                return "WARNING";
            case LogService.LOG_ERROR:
                return "ERROR";

        }
        return "";
    }

    public static String stateToString(LogEntry logEntry) {
        if (logEntry == null || logEntry.getBundle() == null) {
            return "N/A";
        }
        switch (logEntry.getBundle().getState()) {
            case Bundle.ACTIVE:
                return "ACTIVE";
            case Bundle.INSTALLED:
                return "INSTALLED";
            case Bundle.RESOLVED:
                return "RESOLVED";
            case Bundle.STARTING:
                return "STARTING";
            case Bundle.STOPPING:
                return "STOPPING";
        }
        return "";
    }

    public static String timeToString(LogEntry logEntry) {
        if (logEntry == null) {
            return "N/A";
        }
        date.setTime(logEntry.getTime());
        return simpleDateFormat.format(date);
    }

    public static String getLocation(LogEntry logEntry) {
        if (logEntry == null || logEntry.getBundle() == null) {
            return "N/A";
        }

        return logEntry.getBundle().getLocation();
    }

    public static String getBundleId(LogEntry logEntry) {
        if (logEntry == null || logEntry.getBundle() == null) {
            return "N/A";
        }

        return logEntry.getBundle().getBundleId() + "";
    }

    public static String getBundleName(LogEntry logEntry) {
        if (logEntry == null || logEntry.getBundle() == null) {
            return "N/A";
        }

        return logEntry.getBundle().getSymbolicName();
    }

    public static String getService(LogEntry logEntry) {
        if (logEntry == null || logEntry.getBundle() == null || logEntry.getBundle().getBundleContext() == null ||
            logEntry.getServiceReference() == null) {
            return "N/A";
        }

        Object service = logEntry.getBundle().getBundleContext().getService(logEntry.getServiceReference());
        if (service == null) {
            return "N/A";
        }

        return service.getClass().getName();
    }

    static String makeString(LogEntry logEntry) {
        return "time="+timeToString(logEntry) + ";" +
            "level="+levelToString(logEntry) + ";" +
            "bundlename="+getBundleName(logEntry) + ";" +
            "bundleid="+getBundleId(logEntry) + ";" +
            "location="+getLocation(logEntry) + ";" +
            "service="+getService(logEntry) + ";" +
            "message="+logEntry.getMessage()
            ;
    }

    public static String exceptionString(LogEntry logEntry) {
        if (logEntry == null || logEntry.getException() == null) {
            return "N/A";
        }

        Throwable exception = logEntry.getException();
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        sw.flush();
        return sw.toString();
    }
}
