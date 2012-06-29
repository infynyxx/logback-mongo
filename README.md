logback-mongo
=============

Simple Logback plugin to send data to MongoDB

### Using with logback.xml

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <property name="LOG_DIRECTORY" value="/tmp" />

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