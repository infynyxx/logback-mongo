package com.infynyxx.logback.mongo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import com.mongodb.BasicDBObject;

import java.util.Map;

/**
 * @author Prajwal Tuladhar <praj@infynyxx.com>
 */
public class MongoConverter<E> {

    private final String facility;
    private final Map<String, String> addStringStringFields;
    private final String hostname;

    public MongoConverter(String facility, Map<String, String> additionalFields, String hostName) {
        this.facility = facility;
        this.addStringStringFields = additionalFields;
        this.hostname = hostName;
    }

    private BasicDBObject mapFields(E logEvent) {
        BasicDBObject document = new BasicDBObject();
        document.append("f", facility)
                .append("h", hostname);

        ILoggingEvent eventObject = (ILoggingEvent) logEvent;

        final String message = eventObject.getFormattedMessage();

        // format up the stack trace
        IThrowableProxy proxy = eventObject.getThrowableProxy();
        if (proxy != null) {
            document.put("m", message + "\n" + proxy.getClassName() + ": " + proxy.
                    getMessage() + "\n" + toStackTraceString(proxy.getStackTraceElementProxyArray()));
        } else {
            document.append("m", message);
        }

        double logEventTimeTimeStamp = ((ILoggingEvent) logEvent).getTimeStamp() / 1000.0;
        document.put("ts", logEventTimeTimeStamp);
        document.put("l", LevelToSyslogSeverity.convert(eventObject));

        return document;
    }

    private String toStackTraceString(StackTraceElementProxy[] elements) {
        StringBuilder str = new StringBuilder();
        for (StackTraceElementProxy element : elements) {
            str.append(element.getSTEAsString());
        }
        return str.toString();
    }

    public BasicDBObject toBSON(E logEvent) {
        return  mapFields(logEvent);
    }
}
