package com.infynyxx.logback.mongo;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import java.net.UnknownHostException;


/**
 * @author Prajwal Tuladhar <praj@infynyxx.com>
 */
public class IntegrationTest {

    private Mongo mongo;
    private DBCollection collection;

    @Before
    public void setUp() throws UnknownHostException {
        String mongoHost = "localhost";
        int mongoPort = 27017;
        mongo = new Mongo(mongoHost, mongoPort);
        collection = mongo.getDB("logback_db").getCollection("logs");
        collection.drop();
    }

    @Test
    public void test() throws InterruptedException {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.debug("hello world");
        logger.debug("hello world2");
        assertEquals(collection.count(), 2);
    }

    @After
    public void tearDown() {
        collection.drop();
    }
}
