logback-mongo
=============

Simple Logback plugin to send data to MongoDB

I created this as an experiment during Sailthru Hackathon for writing plugin for [logback](http://logback.qos.ch/) which is pretty awesome logging framework for Java.

### Using with logback.xml

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <!-- encoders are assigned the type
     ch.qos.logback.classic.encoder.PatternLayoutEncoder by default -->
        <encoder>
            <pattern>%date [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender name="mongodb" class="com.infynyxx.logback.mongo.MongoAppender">
        <facility>logback-mongo-test</facility>
        <mongoHost>localhost</mongoHost>
        <mongoPort>27017</mongoPort>
        <databaseName>logback_db</databaseName>
        <collectionName>logs</collectionName>
        <safeInsert>true</safeInsert> <!-- by default, it's false -->
    </appender>

    <root>
        <level value="debug" />
        <appender-ref ref="STDOUT" />
        <appender-ref ref="mongodb" />
    </root>
</configuration>
```

But seriously, why do you want to use MongoDB for storing logs? You should probably use systems like [Scribe](https://github.com/facebook/scribe) or [Syslog](http://www.syslog.org/) or [Graylog](http://graylog2.org/) or any other systems except MongoDB.