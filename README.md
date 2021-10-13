# mgw-client

## Overview

Sample java client application using [SWIFT Microgateway](https://developer.swift.com/swift-microgateway) to call SWIFT APIS.
Supported method: GET HTTP Method.

## Getting Started

### Prerequisites

* Java 1.8 and above
* maven 3.5.* and above

### Build package

```
mvn clean install -DskipTests
```

### Configuring Properties

Create an application.properties file and add the below content and place it along with the executable jar file.<br/>
You have all options to load spring boot application.properties file (ex: -Dspring.config.file=\<location\>)
```
mgw.client.tls.hostname.verification.ignore=true/false
mgw.server.tls.truststore.location=E:/mgw_truststore.jks
mgw.server.tls.truststore.password=WhyYouNeedPassword>
mgw.api.appName=demoApp
mgw.api.sharedKey=Abcd1234Abcd1234Abcd1234Abcd1234
mgw.api.profileId=trackerProfileChannel
mgw.api.url=https://localhost:9003/swift/mgw/swift-apitracker/v4/payments/changed/transactions
mgw.api.headerSignatureAdd=true
```

### Run the client

```
java -jar target\mgw.client-0.0.1-SNAPSHOT.jar
Enable tracing
java -Dlogging.level.com.samraj.swift.mgw.client=TRACE -jar target\mgw.client-0.0.1-SNAPSHOT.jar
```

A shell will be prompted. Type mgw-client, mgw or mgwc for making API call.
URL of the API call can be provided as well.
Param url (overrides the configuration provided in application.properties file)
Example:
```mgw https://localhost:9003/swift/mgw/swift-apitracker/v4/payments/changed/transactions?from_date_time=2020-05-15T00:00:00.000Z&to_date_time=2020-05-25T00:00:00.000Z&maximum_number=5&payment_scenario=CCTR```