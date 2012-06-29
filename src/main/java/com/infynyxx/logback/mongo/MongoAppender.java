package com.infynyxx.logback.mongo;

import ch.qos.logback.core.AppenderBase;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Prajwal Tuladhar <praj@infynyxx.com>
 */
public class MongoAppender<E> extends AppenderBase<E>{

    // The following are configurable via logback configuration
    private String facility = "MONGO";
    private String mongoHost = "localhost";
    private int mongoPort = 27017;
    private String databaseName = "logback_db";
    private String collectionName = "logs";
    private boolean safeInsert = false;
    private Map<String, String> additionalFields = new HashMap<String, String>();

    // hidden fields
    private Mongo mongo;
    private DBCollection dbCollection;
    private AppenderExecutor<E> appenderExecutor;

    @Override
    protected void append(E logEvent) {
        try {
            appenderExecutor.append(logEvent);
        } catch (RuntimeException e) {
            System.out.println(getStringStackTrace(e));
            addError("Error occured: ", e);
            throw e;
        }
    }

    private String getStringStackTrace(Exception e) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        return result.toString();
    }

    @Override
    public void start() {
        super.start();
        initExecutor();
    }

    private void initExecutor() {
        try {
            addInfo("connecting to MongoDB");
            mongo = new Mongo(mongoHost, mongoPort);
            dbCollection = mongo.getDB(databaseName).getCollection(collectionName);

            String hostname = InetAddress.getLocalHost().getHostName();

            MongoConverter converter = new MongoConverter(facility, additionalFields, hostname);

            appenderExecutor = new AppenderExecutor<E>(converter, dbCollection, safeInsert);

        } catch (Exception e) {
            throw new RuntimeException("Error initialising appender appenderExecutor", e);
        }
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String host) {
        this.mongoHost = host;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public void setSafeInsert(boolean safeInsert) {
        this.safeInsert = safeInsert;
    }

    public boolean getSafeInsert() {
        return safeInsert;
    }

    public void addAdditionalField(String keyValue) {
        String[] splitted = keyValue.split(":");
        if (splitted.length != 2) {
            throw new IllegalArgumentException("additionalField must be of the format key:value, where key is the MDC "
                    + "key, and value is the GELF field name. But found '" + keyValue + "' instead.");
        }

        additionalFields.put(splitted[0], splitted[1]);
    }
}
