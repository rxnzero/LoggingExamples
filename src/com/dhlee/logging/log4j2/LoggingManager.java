package com.dhlee.logging.log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.async.AsyncLoggerContext;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.FormattedMessageFactory;
import org.apache.logging.log4j.message.MessageFactory;

/**
 * Represents a manager for custom log files stored inside a log folder.
 */
public class LoggingManager {
    /** The default log file extension */
    public static final String FILE_EXTENSION = "log";

    /** The global context used for all loggers */
    private final LoggerContext context;

    /** The global message factory used for all loggers */
    private final MessageFactory msgFactory;

    /** A map of all created logs */
    private final Map<String, Logger> logCache;

    /** The folder containing the log files */
    private final File logFolder;


    public LoggingManager(String name, File logFolder) throws IOException {
        this.logFolder = logFolder;

        if(!logFolder.exists()) {
            if(!logFolder.mkdirs()) {
                throw new IOException("Could not create log folder");
            }
        }

        this.logCache = new HashMap<String, Logger>();

        // Create logger context
        this.context = new AsyncLoggerContext(name);

        // Create formatted message factory
        this.msgFactory = new FormattedMessageFactory();
    }

    public Logger getLogger(String name) {
        Logger logger = logCache.get(name);

        // Create a new one
        if(logger == null) {
            logger = new SimpleLogger(name);

            FileAppender appender = FileAppender.createAppender(
                    new File(logFolder, name + "." + FILE_EXTENSION).getAbsolutePath(),
                    "true",
                    "false",
                    "file_appender-" + name,
                    "true",
                    "false",
                    "true",
                    "8192", 
                    PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, null, null, null, Charset.defaultCharset(), true, true, null, null),                    
                    null,
                    "false",
                    null,
                    null
            );

            appender.start();
            logger.getContext().getConfiguration().getLoggerConfig("root").addAppender(appender, Level.ALL, null);

            // Add to log cache
            logCache.put(name, logger);
        }

        // Return the logger
        return logger;
    }

    private class SimpleLogger extends Logger {

        public SimpleLogger(String name) {
            super(context, name, msgFactory);

            // Set to all levels
            this.setLevel(Level.ALL);
        }
    }
    
	public static void main(String[] args) {
		String logPath = "D:/log4j2logs";
		String logName = "LoggingManager";
		File logPathFile = new File(logPath);
		try {
			LoggingManager logManager = new LoggingManager(logName, logPathFile);
			Logger log = logManager.getLogger(logName);
			log.info("LoggingManager test started!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
}